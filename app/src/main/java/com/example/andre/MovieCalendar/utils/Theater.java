package com.example.andre.MovieCalendar.utils;

/**
 * Created by ToZe on 20/05/2016.
 */
public class Theater {

    private double latitude;
    private double longitude;
    private String redirectUrl;
    private String name;

    public Theater(String name,String redirectUrl) {
        this.name = name;
        this.redirectUrl = redirectUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
