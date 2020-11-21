package com.tim.gotthere_server.database;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import com.tim.gotthere_server.webserver.WebServerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class GotthereDatabase implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(GotthereDatabase.class);

	private LocationRepository locationRepository;

	private WebServerController greeting;

	@Autowired
	public GotthereDatabase(LocationRepository locationRepository, WebServerController greeting) {
		this.locationRepository = locationRepository;
		this.greeting = greeting;
	}

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
	 * @param startDateTime The start date-time.
	 * @param endDateTime The end date-time.
	 */
	public List<Location> getLocations(Date startDateTime, Date endDateTime) {
		System.out.println(startDateTime + " - " + endDateTime);
		return locationRepository.findLocationsBetweenDates(startDateTime, endDateTime);
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

							Date time = new Date(reconstructed * 1000);

							System.out.println("Time: " + time + " Latitude: " + latitude + " Longitude: " + longitude + "Bearing: " + bearing + " Speed: " + speed);
							Location l = new Location(time, new Date(), latitude, longitude, speed, bearing);

							locationRepository.save(l);
							greeting.sendLocationThroughWebSocket(l);
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
