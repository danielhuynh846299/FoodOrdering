package com.example.android.restaurant;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private String price;
    private String favourite;
    private String category;
    private Bitmap image;

    public Item(String name, String price, String favourite, String category, Bitmap image) {
        this.name = name;
        this.price = price;
        this.favourite = favourite;
        this.category = category;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getFavourite() {
        return favourite;
    }

    public String getCategory() {
        return category;
    }

    public Bitmap getImage() {
        return image;
    }

}
