package com.jdkgroup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by imobdev123 on 6/13/17.
 */

public class SyncData<T> {

    @SerializedName("inserted")
    private List<T> inserted;
    @SerializedName("updated")
    private List<T> updated;

    public List<T> getUpdated() {
        return updated;
    }

    public void setUpdated(List<T> updated) {
        this.updated = updated;
    }

    public List<T> getInserted() {
        return inserted;
    }

    public void setInserted(List<T> inserted) {
        this.inserted = inserted;
    }

    public T getT(int i){
        return inserted.get(i);
    }
}
