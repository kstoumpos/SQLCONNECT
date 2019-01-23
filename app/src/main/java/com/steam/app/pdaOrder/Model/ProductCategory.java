package com.steam.app.pdaOrder.Model;

public class ProductCategory {

    private int id;
    private String productName;
    private String categoryName;

    public ProductCategory(String categoryName, String productName)
    {
        this.id = id;
        this.categoryName = categoryName;
        this.productName = productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }
}
