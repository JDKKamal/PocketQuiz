package com.jdkgroup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by imobdev123 on 6/13/17.
 */

public class SyncCountry extends Response{

    @SerializedName("timestamp")
    String timestamp;
    @SerializedName("data")
    SyncData<Country> data;

    public SyncData<Country> getData() {
        return data;
    }

    public void setData(SyncData<Country> data) {
        this.data = data;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
