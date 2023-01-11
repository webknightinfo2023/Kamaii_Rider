package com.kamaii.rider.ui.models;

public class VehicleModel {

    String image = "";
    String vehicle_id = "";
    String vehicle_no = "";
    String vehicle_name = "";
    String vehicle_image = "";
    String charges = "";
    String approve_status = "";
    String vstatus = "";
    String rider_charges = "";

    public String getRider_charges() {
        return rider_charges;
    }

    public void setRider_charges(String rider_charges) {
        this.rider_charges = rider_charges;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(String vehicle_image) {
        this.vehicle_image = vehicle_image;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getVstatus() {
        return vstatus;
    }

    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }
}
