package com.tim.gotthere_server.pojo;

public class LocationsRequest {
	
	private String startDateTime;
	private String endDateTime;

	public LocationsRequest() {
		
	}

	public LocationsRequest(String startDateTime, String endDateTime) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	public String getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return this.endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
}
