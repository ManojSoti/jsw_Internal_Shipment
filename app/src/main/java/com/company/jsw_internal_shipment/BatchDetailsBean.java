package com.company.jsw_internal_shipment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BatchDetailsBean {
    @SerializedName("batch_id")
    String batchId;

    @SerializedName("batch_weight")
    String batchWeight;

    @SerializedName("load_yard_id")
    String loadYardId;

    @SerializedName("load_yard")
    String loadYard;

    @SerializedName("unload_yard")
    String unloadYard;

    @SerializedName("unload_yard_id")
    String unloadYardId;

    @SerializedName("unloading_point")
    String unloadingPoint;

    @SerializedName("customer")
    String customer;

    @SerializedName("trip_id")
    String tripId;


    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public String getBatchId() {
        return batchId;
    }

    public void setBatchWeight(String batchWeight) {
        this.batchWeight = batchWeight;
    }
    public String getBatchWeight() {
        return batchWeight;
    }

    public void setLoadYardId(String loadYardId) {
        this.loadYardId = loadYardId;
    }
    public String getLoadYardId() {
        return loadYardId;
    }

    public void setLoadYard(String loadYard) {
        this.loadYard = loadYard;
    }
    public String getLoadYard() {
        return loadYard;
    }

    public void setUnloadYard(String unloadYard) {
        this.unloadYard = unloadYard;
    }
    public String getUnloadYard() {
        return unloadYard;
    }

    public void setUnloadYardId(String unloadYardId) {
        this.unloadYardId = unloadYardId;
    }
    public String getUnloadYardId() {
        return unloadYardId;
    }

    public void setUnloadingPoint(String unloadingPoint) {
        this.unloadingPoint = unloadingPoint;
    }
    public String getUnloadingPoint() {
        return unloadingPoint;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getCustomer() {
        return customer;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    public String getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "BatchDetailsBean{" +
                "batchId='" + batchId + '\'' +
                ", batchWeight='" + batchWeight + '\'' +
                ", loadYardId='" + loadYardId + '\'' +
                ", loadYard='" + loadYard + '\'' +
                ", unloadYard='" + unloadYard + '\'' +
                ", unloadYardId='" + unloadYardId + '\'' +
                ", unloadingPoint='" + unloadingPoint + '\'' +
                ", customer='" + customer + '\'' +
                ", tripId='" + tripId + '\'' +
                '}';
    }
}
