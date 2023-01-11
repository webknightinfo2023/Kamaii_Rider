
package com.kamaii.rider.ui.models.neworder;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBooking {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("app_flag")
    @Expose
    private String appFlag;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idwithhas")
    @Expose
    private String idwithhas;
    @SerializedName("partner_lat")
    @Expose
    private String partnerLat;
    @SerializedName("partner_long")
    @Expose
    private String partnerLong;
    @SerializedName("user_lat")
    @Expose
    private String userLat;
    @SerializedName("user_long")
    @Expose
    private String userLong;
    @SerializedName("order_date_timedate")
    @Expose
    private String orderDateTimedate;
    @SerializedName("shipping_charge")
    @Expose
    private String shippingCharge;
    @SerializedName("order_enable")
    @Expose
    private String orderEnable;
    @SerializedName("customer_timeslot")
    @Expose
    private String customerTimeslot;
    @SerializedName("partner_timeslot")
    @Expose
    private String partnerTimeslot;
    @SerializedName("partner_street_address")
    @Expose
    private String partnerStreetAddress;
    @SerializedName("partner_society_name")
    @Expose
    private String partnerSocietyName;
    @SerializedName("partner_house_no")
    @Expose
    private String partnerHouseNo;
    @SerializedName("customer_house_no")
    @Expose
    private String customerHouseNo;
    @SerializedName("customer_street_address")
    @Expose
    private String customerStreetAddress;
    @SerializedName("customer_society_name")
    @Expose
    private String customerSocietyName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_mobile_no")
    @Expose
    private String customerMobileNo;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("pay_type")
    @Expose
    private String payType;
    @SerializedName("partner_name")
    @Expose
    private String partnerName;
    @SerializedName("partner_mobile")
    @Expose
    private String partnerMobile;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("cust_address_id")
    @Expose
    private String custAddressId;
    @SerializedName("artist_id")
    @Expose
    private String artistId;
    @SerializedName("partner_address_id")
    @Expose
    private String partnerAddressId;
    @SerializedName("cash_deposite")
    @Expose
    private String cashDeposite;
    @SerializedName("partner_id")
    @Expose
    private String partnerId;
    @SerializedName("myamount")
    @Expose
    private String myamount;
    private String pickupBtnTracker;
    private String dropBtnTracker;
    private String id2="";
    private String id3="";
    private String partner_late_button_show="";

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getPartner_late_button_show() {
        return partner_late_button_show;
    }

    public void setPartner_late_button_show(String partner_late_button_show) {
        this.partner_late_button_show = partner_late_button_show;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getPickupBtnTracker() {
        return pickupBtnTracker;
    }

    public void setPickupBtnTracker(String pickupBtnTracker) {
        this.pickupBtnTracker = pickupBtnTracker;
    }

    public String getDropBtnTracker() {
        return dropBtnTracker;
    }

    public void setDropBtnTracker(String dropBtnTracker) {
        this.dropBtnTracker = dropBtnTracker;
    }

    public String getPartner_visible() {
        return partner_visible;
    }

    public void setPartner_visible(String partner_visible) {
        this.partner_visible = partner_visible;
    }

    @SerializedName("partner_image")
    @Expose
    private String partnerImage;

    public String getCustomer_visible() {
        return customer_visible;
    }

    public void setCustomer_visible(String customer_visible) {
        this.customer_visible = customer_visible;
    }

    private String partner_visible;
    private String customer_visible;

    public String getChangeTracker() {
        return changeTracker;
    }

    public void setChangeTracker(String changeTracker) {
        this.changeTracker = changeTracker;
    }

    private String changeTracker;

    public String getBeforeText() {
        return beforeText;
    }

    public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }

    @SerializedName("handover_text")
    @Expose
    private String handoverText;
    @SerializedName("userMobile")
    @Expose
    private String userMobile;
    private String km = "";
    private String map = "";
    private String beforeText;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(String appFlag) {
        this.appFlag = appFlag;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdwithhas() {
        return idwithhas;
    }

    public void setIdwithhas(String idwithhas) {
        this.idwithhas = idwithhas;
    }

    public String getPartnerLat() {
        return partnerLat;
    }

    public void setPartnerLat(String partnerLat) {
        this.partnerLat = partnerLat;
    }

    public String getPartnerLong() {
        return partnerLong;
    }

    public void setPartnerLong(String partnerLong) {
        this.partnerLong = partnerLong;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLong() {
        return userLong;
    }

    public void setUserLong(String userLong) {
        this.userLong = userLong;
    }

    public String getOrderDateTimedate() {
        return orderDateTimedate;
    }

    public void setOrderDateTimedate(String orderDateTimedate) {
        this.orderDateTimedate = orderDateTimedate;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getOrderEnable() {
        return orderEnable;
    }

    public void setOrderEnable(String orderEnable) {
        this.orderEnable = orderEnable;
    }

    public String getCustomerTimeslot() {
        return customerTimeslot;
    }

    public void setCustomerTimeslot(String customerTimeslot) {
        this.customerTimeslot = customerTimeslot;
    }

    public String getPartnerTimeslot() {
        return partnerTimeslot;
    }

    public void setPartnerTimeslot(String partnerTimeslot) {
        this.partnerTimeslot = partnerTimeslot;
    }

    public String getPartnerStreetAddress() {
        return partnerStreetAddress;
    }

    public void setPartnerStreetAddress(String partnerStreetAddress) {
        this.partnerStreetAddress = partnerStreetAddress;
    }

    public String getPartnerSocietyName() {
        return partnerSocietyName;
    }

    public void setPartnerSocietyName(String partnerSocietyName) {
        this.partnerSocietyName = partnerSocietyName;
    }

    public String getPartnerHouseNo() {
        return partnerHouseNo;
    }

    public void setPartnerHouseNo(String partnerHouseNo) {
        this.partnerHouseNo = partnerHouseNo;
    }

    public String getCustomerHouseNo() {
        return customerHouseNo;
    }

    public void setCustomerHouseNo(String customerHouseNo) {
        this.customerHouseNo = customerHouseNo;
    }

    public String getCustomerStreetAddress() {
        return customerStreetAddress;
    }

    public void setCustomerStreetAddress(String customerStreetAddress) {
        this.customerStreetAddress = customerStreetAddress;
    }

    public String getCustomerSocietyName() {
        return customerSocietyName;
    }

    public void setCustomerSocietyName(String customerSocietyName) {
        this.customerSocietyName = customerSocietyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerMobile() {
        return partnerMobile;
    }

    public void setPartnerMobile(String partnerMobile) {
        this.partnerMobile = partnerMobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustAddressId() {
        return custAddressId;
    }

    public void setCustAddressId(String custAddressId) {
        this.custAddressId = custAddressId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getPartnerAddressId() {
        return partnerAddressId;
    }

    public void setPartnerAddressId(String partnerAddressId) {
        this.partnerAddressId = partnerAddressId;
    }

    public String getCashDeposite() {
        return cashDeposite;
    }

    public void setCashDeposite(String cashDeposite) {
        this.cashDeposite = cashDeposite;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getMyamount() {
        return myamount;
    }

    public void setMyamount(String myamount) {
        this.myamount = myamount;
    }

    public String getPartnerImage() {
        return partnerImage;
    }

    public void setPartnerImage(String partnerImage) {
        this.partnerImage = partnerImage;
    }

    public String getHandoverText() {
        return handoverText;
    }

    public void setHandoverText(String handoverText) {
        this.handoverText = handoverText;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }


}
