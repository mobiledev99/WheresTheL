package com.mobiledev.wheresthel;

import java.io.Serializable;

/**
 * Created by Terry on 5/1/2015.
 *
 * LStop describes an LStop object. It encapsulates the state information needed for
 * locating an L stop and displaying information related to an L stop. The state
 * information includes:
 * <ul>
 *     <li>The direction of the L stop</li>
 *     <li>The name of the L stop</li>
 *     <li>The name of the station</li>
 *     <li>A descriptive nme of the L stop</li>
 *     <li>The L stop's latitude</li>
 *     <li>The L stop's longitude</li>
 * </ul>
 * <p></p>
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class LStop implements Serializable{
    private String direction_id;
    private String stop_name;
    private String station_name;
    private String station_descriptive_name;
    private String latitude;
    private String longitude;

    /**
     * LStop constructor
     *
     * Creates a Test LStop object
     */
    public LStop() {

        direction_id = "E";
        stop_name = "Test Stop";
        station_name = "Test Station";
        station_descriptive_name = "Test Station Description";
        latitude = "41.04";
        longitude = "-87.09";

    }

    /*
     Getter and Setter methods for the LStop fields
     */
    public String getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(String direction_id) {
        this.direction_id = direction_id;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_descriptive_name() {
        return station_descriptive_name;
    }

    public void setStation_descriptive_name(String station_descriptive_name) {
        this.station_descriptive_name = station_descriptive_name;
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

    /**
     * Returns the station's descriptive name as the string value for the object.
     * @return  the station's descriptive name
     */
    @Override
    public String toString() {
        return station_descriptive_name;
    }
}
