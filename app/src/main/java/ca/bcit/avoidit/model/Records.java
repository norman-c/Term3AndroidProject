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
}
