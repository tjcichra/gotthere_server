package com.tim.gotthere_server;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GotthereDatabase implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(GotthereDatabase.class);

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private GreetingController greeting;

	@Override
	public void run(String... args) throws Exception {
		ServerSocket server = new ServerSocket(2810);
		while(true) {
			Socket socket = server.accept();
			new ListenerThread(socket).start();
		}
	}

	/**
	 * Gets a list of locations that inserted 
	 * @param startDateTime The start date-time in the format of YYYY-MM-DD hh:mm
	 * @param endDateTime The end date-time in the format of YYYY-MM-DD hh:mm
	 */
	public List<Location> getLocations(String startDateTime, String endDateTime) {
		List<Location> locations = template.query("SELECT * from locations WHERE `insertion_datetime` BETWEEN ? and ?", new String[] {startDateTime, endDateTime}, (rs, rowNum) -> {
			return new Location(rs.getDouble("bearing"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getDouble("speed"), Util.sqlDateTimeToJavaScript(rs.getString("insertion_datetime")));
		});

		return locations;
	}

	public boolean validateLogin(String username, String password) {
		// WHERE username = '?' AND password = '?'
		int size = this.template.queryForObject("SELECT COUNT(*) FROM users WHERE username = ? AND password = ?", new String[] {username, password}, Integer.class);

		System.out.println(size);

		return size > 0;
	}

	public class ListenerThread extends Thread {

		private Socket socket;

		public ListenerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try(
				InputStream input = socket.getInputStream()
			) {
				while(true) {
					byte buffer[] = new byte[15];
					try {
						if(input.read(buffer) != -1) {
							double bearing = 0;
							if(buffer[0] < 0) {
								bearing = 256 + buffer[0];
							} else {
								bearing = buffer[0];
							}
							bearing = bearing + buffer[1];
							bearing = bearing + (buffer[2] / 100d);

							double latitude = buffer[3] + buffer[4] + (buffer[5] / 100d) + (buffer[6] / 10000d) + (buffer[7] / 1000000d);
							double longitude = buffer[8] + buffer[9] + (buffer[10] / 100d) + (buffer[11] / 10000d) + (buffer[12] / 1000000d);
							double speed = buffer[13] + (buffer[14] / 100d);

							System.out.println("Bearing: " + bearing + " Latitude: " + latitude + " Longitude: " + longitude + " Speed: " + speed);

							template.update("INSERT INTO locations (bearing, latitude, longitude, speed) VALUES (?, ?, ?, ?)", bearing, latitude, longitude, speed);

							SimpleDateFormat formatting = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
							Date d = new Date();
							greeting.autoSendingMessage(new Location(bearing, latitude, longitude, speed, formatting.format(d)));
						} else {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
