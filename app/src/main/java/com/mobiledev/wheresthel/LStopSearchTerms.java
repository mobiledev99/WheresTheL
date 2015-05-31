package com.mobiledev.wheresthel;

import java.io.Serializable;

/**
 * Created by Terry on 5/23/2015.
 */
public class LStopSearchTerms implements Serializable {
    public static final String LSTOPSEARCHTERMS = "LSTOPSEARCHTERMS";

    private String city;
    private String direction;
    private String distance;
    private String latitude;
    private String longitude;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
