package com.tim.gotthere_server;

import java.util.ArrayList;

public class Locations {

    private ArrayList<Location> locations;

    public Locations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Location> getBearing() {
        return this.locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }
}
