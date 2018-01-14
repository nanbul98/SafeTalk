package com.nwhacks.safetalk;

import android.location.Location;

/**
 * Created by braeden on 14/01/18.
 */

public class MyLocation {
    private double longitude;
    private double latitude;
    private String address;
    private String city;

    public MyLocation(){
        this.longitude = 0.0;
        this.latitude = 0.0;
        this.address = "";
        this.city = "";
    }

    @Override
    public String toString() {
        return "Lat: "+ this.latitude + "Long" + this.longitude;
    }

    public MyLocation(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.address = "";
        this.city = "";
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
