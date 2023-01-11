package com.kamaii.rider.ui.models;

public class OfflineImageDataModal {

    String booking_id = "";
    String image_path = "";
    String tracker = "";
    String user_id = "";
    String amount = "";
    String approxdatetime = "";
    String request = "";
    String service_name = "";
    String qty = "";

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getApproxdatetime() {
        return approxdatetime;
    }

    public void setApproxdatetime(String approxdatetime) {
        this.approxdatetime = approxdatetime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public OfflineImageDataModal() {
    }

    public OfflineImageDataModal(String booking_id, String image_path, String tracker) {
        this.booking_id = booking_id;
        this.image_path = image_path;
        this.tracker = tracker;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }
}
