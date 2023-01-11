package com.kamaii.rider.ui.models.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewOrderModel {

    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("show_print_button")
    @Expose
    private String showPrintButton;
    @SerializedName("end_time_tracker")
    @Expose
    private Integer endTimeTracker;

    public String getBeforeText() {
        return beforeText;
    }

    public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }

    @SerializedName("end_time_diff")
    @Expose
    private String endTimeDiff;
    @SerializedName("slot_time")
    @Expose
    private String slotTime;
    @SerializedName("all_booking")
    @Expose
    private List<AllBooking> allBooking = null;

    String rider_end_slot = "";
    String slot_active = "";
    String beforeText = "";

    public String getSlot_active() {
        return slot_active;
    }

    public void setSlot_active(String slot_active) {
        this.slot_active = slot_active;
    }

    public String getRider_end_slot() {
        return rider_end_slot;
    }

    public void setRider_end_slot(String rider_end_slot) {
        this.rider_end_slot = rider_end_slot;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getShowPrintButton() {
        return showPrintButton;
    }

    public void setShowPrintButton(String showPrintButton) {
        this.showPrintButton = showPrintButton;
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

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public List<AllBooking> getAllBooking() {
        return allBooking;
    }

    public void setAllBooking(List<AllBooking> allBooking) {
        this.allBooking = allBooking;
    }


}
