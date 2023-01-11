package com.kamaii.rider.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kamaii.rider.DTO.ArtistBooking;

import java.util.List;

public class ArtistBookingNew {

    @SerializedName("end_time_tracker")
    @Expose
    private Integer endTimeTracker;
    @SerializedName("end_time_diff")
    @Expose
    private String endTimeDiff;
    @SerializedName("slot_active")
    @Expose
    private String slotActive;
    @SerializedName("slot_time")
    @Expose
    private String slotTime;
    @SerializedName("all_order")
    @Expose
    private List<ArtistBooking> allOrder = null;

    String customer_time_slot = "";

    public String getCustomer_time_slot() {
        return customer_time_slot;
    }

    public void setCustomer_time_slot(String customer_time_slot) {
        this.customer_time_slot = customer_time_slot;
    }

    public Integer getEndTimeTracker() {
        return endTimeTracker;
    }

    public void setEndTimeTracker(Integer endTimeTracker) {
        this.endTimeTracker = endTimeTracker;
    }

    public String getEndTimeDiff() {
        return endTimeDiff;
    }

    public void setEndTimeDiff(String endTimeDiff) {
        this.endTimeDiff = endTimeDiff;
    }

    public String getSlotActive() {
        return slotActive;
    }

    public void setSlotActive(String slotActive) {
        this.slotActive = slotActive;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public List<ArtistBooking> getAllOrder() {
        return allOrder;
    }

    public void setAllOrder(List<ArtistBooking> allOrder) {
        this.allOrder = allOrder;
    }

}
