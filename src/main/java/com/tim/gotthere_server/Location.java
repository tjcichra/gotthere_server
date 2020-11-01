package com.tim.gotthere_server;

public class Location {

    private double bearing;
    private double latitude;
    private double longitude;
    private double speed;
    private String dateTime;

    public Location(double bearing, double latitude, double longitude, double speed, String dateTime) {
        this.bearing = bearing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.dateTime = dateTime;
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

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
