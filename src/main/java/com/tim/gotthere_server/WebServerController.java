package com.tim.gotthere_server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class manages all RESTful and websocket communications.
 * @author Timothy Cichra
 */
@RestController
public class WebServerController {
	
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private GotthereDatabase databaseController;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings2")
	public LocationsResponse greeting(LocationsRequest request) throws Exception {
		//Converts date-times from YYYY-MM-DDTHH:mm to YYYY-MM-DD HH:mm
		String startDateTime = Util.javaScriptDateTimeToSQL(request.getStartDateTime());
		String endDateTime = Util.javaScriptDateTimeToSQL(request.getEndDateTime());

		List<Location> locations = this.databaseController.getLocations(startDateTime, endDateTime);
		return new LocationsResponse(locations);
	}

	@MessageMapping("/hello2")
	@SendTo("/topic/greetings3")
	public LoginResponse getLogin(LoginRequest request) throws Exception {
		return new LoginResponse(this.databaseController.validateLogin(request.getUsername(), request.getPassword()));
	}

	/**
	 * Verifies the username and password from POST. Called when the server recieves a POST request after a user clicks the "Login" button.
	 * @param request The serialized object that contains the username and password sent.
	 * @return An object that tells whether the login was accepted or not. Spring automatically deserializes it into JSON and sends it as a POST response.
	 */
	@PostMapping("/login")
	public LoginResponse verifyLogin(@RequestBody LoginRequest request) {
		return new LoginResponse(this.databaseController.validateLogin(request.getUsername(), request.getPassword()));
	}

	@PostMapping("/locations")
	public LocationsResponse getLocationsFromDateTimes(@RequestParam String startDateTime, @RequestParam String endDateTime) {
		//Converts date-times from YYYY-MM-DDTHH:mm to YYYY-MM-DD HH:mm
		String startDateTimeF = Util.javaScriptDateTimeToSQL(startDateTime);
		String endDateTimeF = Util.javaScriptDateTimeToSQL(endDateTime);

		List<Location> locations = this.databaseController.getLocations(startDateTimeF, endDateTimeF);
		return new LocationsResponse(locations);
	}

	public void autoSendingMessage(Location location) throws Exception {
		this.template.convertAndSend("/topic/greetings", location);
	}
}
