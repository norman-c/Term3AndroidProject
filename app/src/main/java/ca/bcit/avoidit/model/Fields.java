package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("project")
    @Expose
    private String project;

    @SerializedName("geom")
    @Expose
    private Geom geom;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("url_link")
    @Expose
    private String url_link;

    @SerializedName("comp_date")
    @Expose
    private String comp_date;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Geom getGeom() {
        return geom;
    }

    public void setGeom(Geom geom) {
        this.geom = geom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
    }

    public String getComp_date() {
        return comp_date;
    }

    public void setComp_date(String comp_date) {
        this.comp_date = comp_date;
    }
}
