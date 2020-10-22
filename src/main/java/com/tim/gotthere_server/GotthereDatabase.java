package com.tim.gotthere_server;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

	@Override
	public void run(String... args) throws Exception {
		try(
			ServerSocket server = new ServerSocket(2810);
			Socket socket = server.accept();
			InputStream input = socket.getInputStream()
		) {
			while(true) {
				byte buffer[] = new byte[10];
				input.read(buffer);

				double bearing = 0;
				if(buffer[0] < 0) {
					bearing = 256 + buffer[0];
				} else {
					bearing = buffer[0];
				}
				bearing = bearing + buffer[1];
				bearing = bearing + (buffer[2] / 100d);
				

				double latitude = buffer[3] + (buffer[4] / 100d);
				double longitude = buffer[5] + buffer[6] + (buffer[7] / 100d);
				double speed = buffer[8] + (buffer[9] / 100d);

				System.out.println("Bearing: " + bearing + " Latitude: " + latitude + " Longitude: " + longitude + " Speed: " + speed);

				template.update("INSERT INTO locations (bearing, latitude, longitude, speed) VALUES (?, ?, ?, ?)", bearing, latitude, longitude, speed);
			}
		}
		
	}
}
