package com.tim.gotthere_server.webserver;

import java.util.List;

import com.tim.gotthere_server.Util;
import com.tim.gotthere_server.database.GotthereDatabase;
import com.tim.gotthere_server.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
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

	/**
	 * Used by a browser to request all locations within the start date-time and the end date-time.
	 * A browser needs to send a POST request to the url <b>/locations?startDateTime={startDateTime}&endDateTime={endDateTime}</b>
	 * where the words in brackets are replaced by the corresponding value in the format of YYYY-MM-DDTHH:mm.
	 *
	 * @param startDateTime The start date-time for requesting the locations in the format of YYYY-MM-DDTHH:mm.
	 * @param endDateTime The start date-time for requesting the locations in the format of YYYY-MM-DDTHH:mm.
	 * @return An object containing all the locations between the two date-times that will be deserialized into JSON and sent as a POST response.
	 */
	@PostMapping("/locations")
	public LocationsResponse getLocationsFromDateTimes(@RequestParam String startDateTime, @RequestParam String endDateTime) {
		//Converts date-times from YYYY-MM-DDTHH:mm to YYYY-MM-DD HH:mm
		String startDateTimeF = Util.javaScriptDateTimeToSQL(startDateTime);
		String endDateTimeF = Util.javaScriptDateTimeToSQL(endDateTime);

		List<Location> locations = this.databaseController.getLocations(startDateTimeF, endDateTimeF);
		return new LocationsResponse(locations);
	}

	/**
	 * Sends location data to all browsers via a websocket. The websocket on the browser side retrieves it and adds it to the map.
	 *
	 * @param location The location to send to the websocket.
	 */
	public void autoSendingMessage(Location location) {
		this.template.convertAndSend("/topic/greetings", location);
	}
}
