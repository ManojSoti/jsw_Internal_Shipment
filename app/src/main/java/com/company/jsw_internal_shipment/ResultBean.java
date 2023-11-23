package com.company.jsw_internal_shipment;

import com.google.gson.annotations.SerializedName;

public class ResultBean {
    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;


    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
