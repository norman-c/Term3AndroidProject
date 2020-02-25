package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Records {

    @SerializedName("nhits")
    @Expose
    private int nhits;

    @SerializedName("parameters")
    @Expose
    private String[] parameters;

    @SerializedName("records")
    @Expose
    private Records records;

    @SerializedName("facet_groups")
    @Expose
    private String facet_groups;

    public int getNhits() {
        return nhits;
    }

    public void setNhits(int nhits) {
        this.nhits = nhits;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }

    public String getFacet_groups() {
        return facet_groups;
    }

    public void setFacet_groups(String facet_groups) {
        this.facet_groups = facet_groups;
    }
}
