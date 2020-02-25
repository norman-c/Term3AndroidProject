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

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public String getRecord_timestamp() {
        return record_timestamp;
    }

    public void setRecord_timestamp(String record_timestamp) {
        this.record_timestamp = record_timestamp;
    }
}
