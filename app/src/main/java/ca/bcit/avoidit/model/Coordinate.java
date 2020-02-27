package ca.bcit.avoidit.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Coordinate {

    private double latitude;

    private double longitude;

    private double[] latLng;

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
}
