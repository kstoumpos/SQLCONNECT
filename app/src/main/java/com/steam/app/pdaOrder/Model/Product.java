package com.steam.app.pdaOrder.Model;

public class Product {

    private int id;
    private int catId;
    private String productName;
    private double price;

    public Product(String categoryName, double price)
    {
        this.productName = categoryName;
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

    public void getProductprice(double price) {
        this.price = price;
    }

    public int getProductCategoryId() {
        return catId;
    }
}

