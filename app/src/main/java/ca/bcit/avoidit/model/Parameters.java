package ca.bcit.avoidit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parameters {

    @SerializedName("dataset")
    @Expose
    private String dataset;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("rows")
    @Expose
    private int rows;

    @SerializedName("format")
    @Expose
    private String format;

    @SerializedName("facet")
    @Expose
    private List<String> facet;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getFacet() {
        return facet;
    }

    public void setFacet(List<String> facet) {
        this.facet = facet;
    }
}
