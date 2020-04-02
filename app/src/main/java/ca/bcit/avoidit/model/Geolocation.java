package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geolocation {

    @SerializedName("nhits")
    @Expose
    private int nhits;

    @SerializedName("parameters")
    @Expose
    private Parameters parameters;

    @SerializedName("records")
    @Expose
    private List<Record> records;

    @SerializedName("facet_groups")
    @Expose
    private List<FacetGroup> facetGroups;
}
