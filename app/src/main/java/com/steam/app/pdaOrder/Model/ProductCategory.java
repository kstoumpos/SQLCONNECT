package com.steam.app.pdaOrder.Model;

import java.io.Serializable;

public class ProductCategory implements Serializable {

    private int id;
    private int catId;
    private String categoryName;

    public ProductCategory(String categoryName)
    {
        this.categoryName = categoryName;
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

    public int getProductCategoryId() {
        return catId;
    }

}
