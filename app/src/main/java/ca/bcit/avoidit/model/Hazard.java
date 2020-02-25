package ca.bcit.avoidit.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hazard {

    @SerializedName("datasetid")
    @Expose
    private String datasetid;

    @SerializedName("recordid")
    @Expose
    private String recordid;

    @SerializedName("fields")
    @Expose
    private Fields fields;

    @SerializedName("record_timestamp")
    @Expose
    private String record_timestamp;
}
