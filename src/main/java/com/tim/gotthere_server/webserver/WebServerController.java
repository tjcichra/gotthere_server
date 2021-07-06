package com.tim.gotthere_server.webserver;

import com.tim.gotthere_server.database.BookmarkRepository;
import com.tim.gotthere_server.database.Location;
import com.tim.gotthere_server.database.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This "bean" manages all RESTful and WebSocket communications.
 * Lombok will automatically generate a constructor that allows Spring to inject the "beans" for the location repository,
 * the bookmark repository, and the simple messaging template into the fields.
 * @author Timothy Cichra
 */
@RestController
public class WebServerController {

	private final LocationRepository locationRepository;

	private final BookmarkRepository bookmarkRepository;

	private final SimpMessagingTemplate simpMessagingTemplate;	

	@Autowired
	public WebServerController(LocationRepository locationRepository, BookmarkRepository bookmarkRepository, SimpMessagingTemplate simpMessagingTemplate) {
		this.locationRepository = locationRepository;
		this.bookmarkRepository = bookmarkRepository;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	/**
	 * Used by a browser to request all locations within the start date-time and the end date-time.
	 * A browser needs to send a POST request to the url <b>/locations?startDateTime={startDateTime}&endDateTime={endDateTime}</b>
	 *
	 * @param startDateTime The start date-time for requesting the locations in the format of YYYY-MM-DDTHH:mm (or longer).
	 * @param endDateTime The start date-time for requesting the locations in the format of YYYY-MM-DDTHH:mm (or longer).
	 * @return A list of all the locations between the two date-times that will be deserialized into JSON and sent as a POST response.
	 */
	@PostMapping("/locations")
	public List<Location> getLocationsFromDateTimes(@RequestParam String startDateTime, @RequestParam String endDateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		return this.locationRepository.findLocationsBetweenDates(sdf.parse(startDateTime), sdf.parse(endDateTime));
	}

	@PostMapping("/startDateTimeNumber")
	public Date getStartTimeDateOfPastLocations(@RequestParam Integer locationNumber) {
		if (locationNumber != null) {
			List<Date> dateOfPastLocations = this.locationRepository.getDateOfPastLocations();
			Calendar cal = Calendar.getInstance();
			if(dateOfPastLocations.size() >= locationNumber) {
				cal.setTime(dateOfPastLocations.get(locationNumber - 1));
			} else {
				cal.setTime(dateOfPastLocations.get(dateOfPastLocations.size() - 1));
			}
			cal.add(Calendar.HOUR, -5);
			return cal.getTime();
		} else {
			return null;
		}
	}

	/**
	 * Sends location data to all open browsers via a WebSocket. The WebSocket on the browser side retrieves it and adds it to the map.
	 *
	 * @param location The location to send to the WebSocket. It should be the latest location.
	 */
	public void sendLocationThroughWebSocket(Location location) {
		this.simpMessagingTemplate.convertAndSend("/web-socket/location", location);
	}
}
