package com.kamaii.rider.ui.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    String id;
    String cat_name="";
    String image = "";


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }


}
