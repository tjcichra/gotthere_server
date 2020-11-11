package com.tim.gotthere_server.pojo;

import java.util.List;

public class LocationsResponse {

    private List<Location> locations;

    public LocationsResponse(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
