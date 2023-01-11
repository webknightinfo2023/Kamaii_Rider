
package com.kamaii.rider.ui.models.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("desc_type_name")
    @Expose
    private String descTypeName;
    @SerializedName("cooking_instruction")
    @Expose
    private String cookingInstruction;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("qty_type")
    @Expose
    private String qtyType;
    @SerializedName("qty_description")
    @Expose
    private String qtyDescription;
    @SerializedName("show_print_button")
    @Expose
    private Integer showPrintButton;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescTypeName() {
        return descTypeName;
    }

    public void setDescTypeName(String descTypeName) {
        this.descTypeName = descTypeName;
    }

    public String getCookingInstruction() {
        return cookingInstruction;
    }

    public void setCookingInstruction(String cookingInstruction) {
        this.cookingInstruction = cookingInstruction;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQtyType() {
        return qtyType;
    }

    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }

    public String getQtyDescription() {
        return qtyDescription;
    }

    public void setQtyDescription(String qtyDescription) {
        this.qtyDescription = qtyDescription;
    }

    public Integer getShowPrintButton() {
        return showPrintButton;
    }

    public void setShowPrintButton(Integer showPrintButton) {
        this.showPrintButton = showPrintButton;
    }

}
