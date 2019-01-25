package com.steam.app.pdaOrder.Model;

public class Product {

    private int id;
    private int catId;
    private String productName;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product(String categoryName, double price)
    {
        this.productName = categoryName;
        this.price = price;
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
}

