package com.steam.app.pdaOrder.Model;

import android.os.Parcel;
import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private int mAmount;
    private int mQuantity;
    private String productName;
    private double price;
    private String comment;
    private int catId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public Product(String categoryName, double price, int id, int catId, String comment) {
        this.productName = categoryName;
        this.price = price;
        this.id = id;
        this.catId = catId;
        this.comment = comment;
    }

    public Product() {
    }

    public Product(Parcel in) {
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

    public int getmAmount() {
        return mAmount;
    }

    public int getmQuantity(){
        return mQuantity;
    }

    public void addToQuantity(){
        this.mQuantity += 1;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void removeFromQuantity(){
        if(this.mQuantity > 1){
            this.mQuantity -= 1;
        }
    }
}

