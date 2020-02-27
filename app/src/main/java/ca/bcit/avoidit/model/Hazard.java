package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hazard {

    @SerializedName("nhits")
    @Expose
    private int nhits;

    @SerializedName("parameters")
    @Expose
    private Parameters parameters;

    @SerializedName("records")
    @Expose
    private ArrayList<Record> records;

    @SerializedName("facet_groups")
    @Expose
    private FacetGroups facet_groups;

    public int getNhits() {
        return nhits;
    }

    public void setNhits(int nhits) {
        this.nhits = nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public FacetGroups getFacet_groups() {
        return facet_groups;
    }

    public void setFacet_groups(FacetGroups facet_groups) {
        this.facet_groups = facet_groups;
    }
}
