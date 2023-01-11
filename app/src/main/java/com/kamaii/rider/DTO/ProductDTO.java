package com.kamaii.rider.DTO;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ProductDTO implements Serializable, Parcelable {


    String id = "";
    String user_id = "";
    String product_name = "";
    String product_image = "";
    String price = "";
    String created_at = "";
    String updated_at = "";
    String currency_type = "";
    String description = "";
    String rate = "";
    boolean isSelected;
    boolean isdisplay=false;
    String service_charge = "";
    String category_id="";
    String sub_category_id="";
    String is_used="";
    String quantitydays="";
    String maxmiumvalue="";
    String quantityvalue="";
    String roundtrip="";
    String sublevel_category="";
    String status="";
    String change_price = "";
    String vehicle_number = "";
    String is_visible = "";
    String short_description = "";
    String qty = "";
    String variation = "";
    String discount = "";
    String product_price = "";
    String qty_description = "";
    String cooking_instruction = "";


    public String getCooking_instruction() {
        return cooking_instruction;
    }

    public void setCooking_instruction(String cooking_instruction) {
        this.cooking_instruction = cooking_instruction;
    }

    public String getQty_description() {
        return qty_description;
    }

    public void setQty_description(String qty_description) {
        this.qty_description = qty_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public static Creator<ProductDTO> getCREATOR() {
        return CREATOR;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(String is_visible) {
        this.is_visible = is_visible;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getChange_price() {
        return change_price;
    }

    public void setChange_price(String change_price) {
        this.change_price = change_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSublevel_category() {
        return sublevel_category;
    }

    public void setSublevel_category(String sublevel_category) {
        this.sublevel_category = sublevel_category;
    }

    public String getRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(String roundtrip) {
        this.roundtrip = roundtrip;
    }

    public String getQuantityvalue() {
        return quantityvalue;
    }

    public void setQuantityvalue(String quantityvalue) {
        this.quantityvalue = quantityvalue;
    }

    public String getMaxmiumvalue() {
        return maxmiumvalue;
    }

    public void setMaxmiumvalue(String maxmiumvalue) {
        this.maxmiumvalue = maxmiumvalue;
    }

    public String getQuantitydays() {
        return quantitydays;
    }

    public void setQuantitydays(String quantitydays) {
        this.quantitydays = quantitydays;
    }

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public ProductDTO() {

    }



    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public ProductDTO(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        product_name = in.readString();
        product_image = in.readString();
        price = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        currency_type = in.readString();
        description = in.readString();
        rate = in.readString();
        category_id = in.readString();
        sub_category_id = in.readString();
        isSelected = in.readByte() != 0;
        isdisplay = in.readByte() != 0;

        service_charge = in.readString();
        quantitydays = in.readString();
        maxmiumvalue = in.readString();
        roundtrip = in.readString();
    }
    public boolean isIsdisplay() {
        return isdisplay;
    }


    public void setIsdisplay(boolean isdisplay) {
        this.isdisplay = isdisplay;
    }
    public static final Creator<ProductDTO> CREATOR = new Creator<ProductDTO>() {
        @Override
        public ProductDTO createFromParcel(Parcel in) {
            return new ProductDTO(in);
        }

        @Override
        public ProductDTO[] newArray(int size) {
            return new ProductDTO[size];
        }
    };

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return product_name.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(product_name);
        dest.writeString(product_image);
        dest.writeString(price);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(currency_type);
        dest.writeString(description);
        dest.writeString(rate);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isdisplay ? 1 : 0));
        dest.writeString(service_charge);
        dest.writeString(category_id);
        dest.writeString(sub_category_id);
        dest.writeString(quantitydays);
        dest.writeString(maxmiumvalue);
        dest.writeString(roundtrip);
    }
}
