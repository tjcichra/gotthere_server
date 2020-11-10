package com.tim.gotthere_server;

public class Location {

    private double bearing;
    private double latitude;
    private double longitude;
    private double speed;
    private String insertionDateTime;
    private String realDateTime;

    public Location(double bearing, double latitude, double longitude, double speed, String insertionDateTime, String realDateTime) {
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.insertionDateTime = insertionDateTime;
        this.realDateTime = realDateTime;
    }

    public double getBearing() {
        return this.bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getInsertionDateTime() {
        return this.insertionDateTime;
    }

    public void setInsertionDateTime(String insertionDateTime) {
        this.insertionDateTime = insertionDateTime;
    }

    public String getRealDateTime() {
        return this.realDateTime;
    }

    public void setRealDateTime(String realDateTime) {
        this.realDateTime = realDateTime;
    }
}
