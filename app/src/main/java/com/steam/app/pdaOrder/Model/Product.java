package com.steam.app.pdaOrder.Model;

import android.os.Parcel;
import java.io.Serializable;

public class Product implements Serializable {

    private int id;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    private int catId;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    private String productName;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product(String categoryName, double price, int id, int catId) {
        this.productName = categoryName;
        this.price = price;
        this.id = id;
        this.catId = catId;
    }

    public Product() {
    }

    protected Product(Parcel in) {
        productName = in.readString();
        price = in.readDouble();
        id = in.readInt();
        catId = in.readInt();
    }

    public String getProductName() {
        return productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

