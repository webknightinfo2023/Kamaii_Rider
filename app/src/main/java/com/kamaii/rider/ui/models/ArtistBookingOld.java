package com.kamaii.rider.ui.models;

import com.kamaii.rider.DTO.ProductDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistBookingOld implements Serializable {
    String rider_booking_flag = "";
    String id = "";
    String user_id = "";
    String artist_id = "";
    String booking_time = "";
    String start_time = "";
    String description = "";
    String category_price = "";
    String booking_date = "";
    String end_time = "";
    String price = "";
    String status = "";
    String booking_flag = "";
    String decline_by = "";
    String time_zone = "";
    String decline_reason = "";
    String booking_timestamp = "";
    String created_at = "";
    String updated_at = "";
    String artistImage = "";
    String category_name = "";
    String artistName = "";
    String artistLocation = "";
    String userName = "";
    String address = "";
    String ava_rating = "";
    String c_latitude = "";
    String c_longitude = "";
    String working_min = "";
    String userImage = "";
    String currency_type = "";
    String booking_type = "";
    boolean isSection = false;
    String section_name = "";
    String latitude="";
    String longitude="";
    String location_status="";
    String userMobile="";
    String image="";
    String approxdatetime = "";
    String destinationaddress = "";
    String km = "";
    String second = "";
    String distance_time = "";
    String pay_type = "";
    String partner_no = "";
    String preparation_time = "";
    String customor_rider_booking_flag = "";
    String customerMobile = "";
    String art_lat = "";
    String art_long = "";
    String starttimer = "";
    String pickup_status = "";
    String fulladdress = "";
    String actuallcollectamount = "";
    String ridertotalkm = "";
    String order_enable = "";
    String cust_house_no = "";
    String cust_society_no = "";
    String cust_address_id = "";
    String part_house_no = "";
    String part_society_no = "";
    String part_address_id = "";
    String cust_street_address = "";
    String cust_address_type = "";
    String part_street_address = "";
    String part_address_type = "";
    String cash_deposite = "";
    String myamount = "";
    String partner_image = "";
    String partner_name = "";
    String partner_id = "";
    String handover_text = "";


    public String getMyamount() {
        return myamount;
    }

    public void setMyamount(String myamount) {
        this.myamount = myamount;
    }

    public String getPartner_image() {
        return partner_image;
    }

    public void setPartner_image(String partner_image) {
        this.partner_image = partner_image;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getHandover_text() {
        return handover_text;
    }

    public void setHandover_text(String handover_text) {
        this.handover_text = handover_text;
    }

    public String getCash_deposite() {
        return cash_deposite;
    }

    public void setCash_deposite(String cash_deposite) {
        this.cash_deposite = cash_deposite;
    }

    public String getCust_street_address() {
        return cust_street_address;
    }

    public void setCust_street_address(String cust_street_address) {
        this.cust_street_address = cust_street_address;
    }

    public String getCust_address_type() {
        return cust_address_type;
    }

    public void setCust_address_type(String cust_address_type) {
        this.cust_address_type = cust_address_type;
    }

    public String getPart_street_address() {
        return part_street_address;
    }

    public void setPart_street_address(String part_street_address) {
        this.part_street_address = part_street_address;
    }

    public String getPart_address_type() {
        return part_address_type;
    }

    public void setPart_address_type(String part_address_type) {
        this.part_address_type = part_address_type;
    }

    public String getPart_house_no() {
        return part_house_no;
    }

    public void setPart_house_no(String part_house_no) {
        this.part_house_no = part_house_no;
    }

    public String getPart_society_no() {
        return part_society_no;
    }

    public void setPart_society_no(String part_society_no) {
        this.part_society_no = part_society_no;
    }

    public String getPart_address_id() {
        return part_address_id;
    }

    public void setPart_address_id(String part_address_id) {
        this.part_address_id = part_address_id;
    }

    public String getCust_house_no() {
        return cust_house_no;
    }

    public void setCust_house_no(String cust_house_no) {
        this.cust_house_no = cust_house_no;
    }

    public String getCust_society_no() {
        return cust_society_no;
    }

    public void setCust_society_no(String cust_society_no) {
        this.cust_society_no = cust_society_no;
    }

    public String getCust_address_id() {
        return cust_address_id;
    }

    public void setCust_address_id(String cust_address_id) {
        this.cust_address_id = cust_address_id;
    }

    public String getOrder_enable() {
        return order_enable;
    }

    public void setOrder_enable(String order_enable) {
        this.order_enable = order_enable;
    }



    public String getRidertotalkm() {
        return ridertotalkm;
    }

    public void setRidertotalkm(String ridertotalkm) {
        this.ridertotalkm = ridertotalkm;
    }
    public String getActuallcollectamount() {
        return actuallcollectamount;
    }

    public void setActuallcollectamount(String actuallcollectamount) {
        this.actuallcollectamount = actuallcollectamount;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getPickup_status() {
        return pickup_status;
    }

    public void setPickup_status(String pickup_status) {
        this.pickup_status = pickup_status;
    }

    public String getStarttimer() {
        return starttimer;
    }

    public void setStarttimer(String starttimer) {
        this.starttimer = starttimer;
    }

    public String getArt_lat() {
        return art_lat;
    }

    public void setArt_lat(String art_lat) {
        this.art_lat = art_lat;
    }

    public String getArt_long() {
        return art_long;
    }

    public void setArt_long(String art_long) {
        this.art_long = art_long;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomor_rider_booking_flag() {
        return customor_rider_booking_flag;
    }

    public void setCustomor_rider_booking_flag(String customor_rider_booking_flag) {
        this.customor_rider_booking_flag = customor_rider_booking_flag;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getPartner_no() {
        return partner_no;
    }

    public void setPartner_no(String partner_no) {
        this.partner_no = partner_no;
    }

    public String getRider_booking_flag() {
        return rider_booking_flag;
    }

    public void setRider_booking_flag(String rider_booking_flag) {
        this.rider_booking_flag = rider_booking_flag;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getDistance_time() {
        return distance_time;
    }

    public void setDistance_time(String distance_time) {
        this.distance_time = distance_time;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getDestinationaddress() {
        return destinationaddress;
    }

    public void setDestinationaddress(String destinationaddress) {
        this.destinationaddress = destinationaddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getApproxdatetime() {
        return approxdatetime;
    }

    public void setApproxdatetime(String approxdatetime) {
        this.approxdatetime = approxdatetime;
    }
    public String getLocation_status() {
        return location_status;
    }

    public void setLocation_status(String location_status) {
        this.location_status = location_status;
    }

    ArrayList<ProductDTO> product = new ArrayList<>();

    public ArrayList<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<ProductDTO> product) {
        this.product = product;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public String getC_latitude() {
        return c_latitude;
    }

    public String getC_longitude() {
        return c_longitude;
    }

    public void setC_longitude(String c_longitude) {
        this.c_longitude = c_longitude;
    }

    public void setC_latitude(String c_latitude) {
        this.c_latitude = c_latitude;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean section) {
        isSection = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getCategory_price() {
        return category_price;
    }

    public void setCategory_price(String category_price) {
        this.category_price = category_price;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooking_flag() {
        return booking_flag;
    }

    public void setBooking_flag(String booking_flag) {
        this.booking_flag = booking_flag;
    }

    public String getDecline_by() {
        return decline_by;
    }

    public void setDecline_by(String decline_by) {
        this.decline_by = decline_by;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getDecline_reason() {
        return decline_reason;
    }

    public void setDecline_reason(String decline_reason) {
        this.decline_reason = decline_reason;
    }

    public String getBooking_timestamp() {
        return booking_timestamp;
    }

    public void setBooking_timestamp(String booking_timestamp) {
        this.booking_timestamp = booking_timestamp;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistLocation() {
        return artistLocation;
    }

    public void setArtistLocation(String artistLocation) {
        this.artistLocation = artistLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAva_rating() {
        return ava_rating;
    }

    public void setAva_rating(String ava_rating) {
        this.ava_rating = ava_rating;
    }



    public String getWorking_min() {
        return working_min;
    }

    public void setWorking_min(String working_min) {
        this.working_min = working_min;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }
}
