package com.kamaii.rider.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomePageModel {

    @SerializedName("catelog_msg")
    @Expose
    private String catelogMsg;
    @SerializedName("catelog")
    @Expose
    private ArrayList<SiderModel> catelog = null;
    @SerializedName("cat_image")
    @Expose
    private String catImage;
    @SerializedName("total_cat")
    @Expose
    private String totalCat;

    @SerializedName("yt_image")
    @Expose
    private String youtubeimage;
    @SerializedName("refer_img")
    @Expose
    private String refer_img;
    @SerializedName("cat_image")
    @Expose
    private String cat_image;
    @SerializedName("share_profile")
    @Expose
    private String share_profile;


    @SerializedName("total_item")
    @Expose
    private String totalItem;
    @SerializedName("total_visit_profile")
    @Expose
    private String totalVisitProfile;
    @SerializedName("item_visit")
    @Expose
    private String itemVisit;

@SerializedName("yt_image_link")
    @Expose
    private String ytimagelink;

@SerializedName("share_profile_link")
    @Expose
    private String shareprofilelink;

@SerializedName("refer_img_link")
    @Expose
    private String referimglink;

@SerializedName("catimagelink")
    @Expose
    private String catimagelink;

@SerializedName("area_name")
    @Expose
    private String area_name;

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getYtimagelink() {
        return ytimagelink;
    }

    public void setYtimagelink(String ytimagelink) {
        this.ytimagelink = ytimagelink;
    }

    public String getShareprofilelink() {
        return shareprofilelink;
    }

    public void setShareprofilelink(String shareprofilelink) {
        this.shareprofilelink = shareprofilelink;
    }

    public String getReferimglink() {
        return referimglink;
    }

    public void setReferimglink(String referimglink) {
        this.referimglink = referimglink;
    }

    public String getCatimagelink() {
        return catimagelink;
    }

    public void setCatimagelink(String catimagelink) {
        this.catimagelink = catimagelink;
    }

    public String getYoutubeimage() {
        return youtubeimage;
    }

    public void setYoutubeimage(String youtubeimage) {
        this.youtubeimage = youtubeimage;
    }

    public String getRefer_img() {
        return refer_img;
    }

    public void setRefer_img(String refer_img) {
        this.refer_img = refer_img;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getShare_profile() {
        return share_profile;
    }

    public void setShare_profile(String share_profile) {
        this.share_profile = share_profile;
    }

    public String getCatelogMsg() {
        return catelogMsg;
    }

    public void setCatelogMsg(String catelogMsg) {
        this.catelogMsg = catelogMsg;
    }

    public ArrayList<SiderModel> getCatelog() {
        return catelog;
    }

    public void setCatelog(ArrayList<SiderModel> catelog) {
        this.catelog = catelog;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getTotalCat() {
        return totalCat;
    }

    public void setTotalCat(String totalCat) {
        this.totalCat = totalCat;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    public String getTotalVisitProfile() {
        return totalVisitProfile;
    }

    public void setTotalVisitProfile(String totalVisitProfile) {
        this.totalVisitProfile = totalVisitProfile;
    }

    public String getItemVisit() {
        return itemVisit;
    }

    public void setItemVisit(String itemVisit) {
        this.itemVisit = itemVisit;
    }

}
