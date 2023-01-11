
package com.kamaii.rider.ui.models.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingData {

    @SerializedName("total_order")
    @Expose
    private String totalOrder;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("seconds")
    @Expose
    private String seconds;
    @SerializedName("total_min")
    @Expose
    private String totalMin;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTotalMin() {
        return totalMin;
    }

    public void setTotalMin(String totalMin) {
        this.totalMin = totalMin;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

}
