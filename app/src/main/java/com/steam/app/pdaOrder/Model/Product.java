package com.steam.app.pdaOrder.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private int id;
    private int catId;
    private String productName;
    private double price;
    private boolean selected;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product(String categoryName, double price, int id) {
        this.productName = categoryName;
        this.price = price;
        this.id = id;
    }

    protected Product(Parcel in) {
        productName = in.readString();
        price = in.readDouble();
        id = in.readInt();
    }

    public String getCategoryName() {
        return productName;
    }

    public void setCategoryName(String categoryName) {
        this.productName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductCategoryId() {
        return catId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeDouble(price);
        parcel.writeInt(id);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int i) {
            return new Product[0];
        }
    };
}

