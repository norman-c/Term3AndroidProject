package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Geom {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("coordinates")
    @Expose
    private java.lang.Object coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public java.lang.Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(java.lang.Object coordinates) {
        this.coordinates = coordinates;
    }
}
