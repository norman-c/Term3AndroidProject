package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FacetGroups {

    @SerializedName("facets")
    @Expose
    private ArrayList<Facet> facets;

    @SerializedName("name")
    @Expose
    private String name;

    public ArrayList<Facet> getFacets() {
        return facets;
    }

    public void setFacets(ArrayList<Facet> facets) {
        this.facets = facets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
