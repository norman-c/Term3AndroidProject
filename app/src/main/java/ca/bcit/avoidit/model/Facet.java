package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Facet {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("name")
    @Expose
    private String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

