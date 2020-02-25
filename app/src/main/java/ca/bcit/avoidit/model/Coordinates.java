package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Coordinates {

    @SerializedName("project")
    @Expose
    private ArrayList<int[]> coords;

    public ArrayList<int[]> getCoords() {
        return coords;
    }

    public void setCoords(ArrayList<int[]> coords) {
        this.coords = coords;
    }
}
