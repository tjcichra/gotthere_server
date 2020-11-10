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
	private WebServerController greeting;

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
		List<Location> locations = template.query("SELECT * FROM locations WHERE `real_datetime` BETWEEN ? and ? ORDER BY real_datetime", new String[] {startDateTime, endDateTime}, (rs, rowNum) -> {
			return new Location(rs.getDouble("bearing"), rs.getDouble("latitude"), rs.getDouble("longitude"), Util.metersPerSecondToMilesPerHour(rs.getDouble("speed")), Util.sqlDateTimeToJavaScript(rs.getString("insertion_datetime")), Util.sqlDateTimeToJavaScript(rs.getString("real_datetime")));
		});

		return locations;
	}

	public List<LoginInformation> getLoginInformation() {
		List<LoginInformation> loginInfo = template.query("SELECT * FROM users", new Object[] {}, (rs, rowNum) -> {
			return new LoginInformation(rs.getString("username"), rs.getString("password"));
		});

		return loginInfo;
	}

	public boolean validateLogin(String username, String password) {
		int size = this.template.queryForObject("SELECT COUNT(*) FROM users WHERE username = ? AND password = ?", new String[] {username, password}, Integer.class);
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
					byte buffer[] = new byte[20];
					try {
						if(input.read(buffer) != -1) {
							long reconstructed = ((buffer[0] & 0xFF) << 32) | ((buffer[1] & 0xFF) << 24) | ((buffer[2] & 0xFF) << 16) | ((buffer[3] & 0xFF) << 8) | (buffer[4] & 0xFF);

							double latitude = buffer[5] + buffer[6] + (buffer[7] / 100d) + (buffer[8] / 10000d) + (buffer[9] / 1000000d);
							double longitude = buffer[10] + buffer[11] + (buffer[12] / 100d) + (buffer[13] / 10000d) + (buffer[14] / 1000000d);
							
							double speed = buffer[15] + (buffer[16] / 100d);

							double bearing = 0;
							if(buffer[17] < 0) {
								bearing = 256 + buffer[17];
							} else {
								bearing = buffer[17];
							}
							bearing = bearing + buffer[18];
							bearing = bearing + (buffer[19] / 100d);

							SimpleDateFormat formatting2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
							Date time = new Date(reconstructed * 1000);
							String ftime = formatting2.format(time);

							System.out.println("Time: " + ftime + " Latitude: " + latitude + " Longitude: " + longitude + "Bearing: " + bearing + " Speed: " + speed);

							template.update("INSERT INTO locations (bearing, latitude, longitude, speed, real_datetime) VALUES (?, ?, ?, ?, ?)", bearing, latitude, longitude, speed, ftime);

							SimpleDateFormat formatting3 = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
							greeting.autoSendingMessage(new Location(bearing, latitude, longitude, speed, formatting3.format(new Date()), formatting3.format(new Date(reconstructed * 1000))));
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
