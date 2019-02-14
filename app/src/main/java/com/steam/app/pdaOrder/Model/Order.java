package com.steam.app.pdaOrder.Model;

import java.util.ArrayList;

public class Order {

    private double OderPrice;

    public void setOderProducts(ArrayList<Product> oderProducts) {
        this.oderProducts = oderProducts;
    }

    private ArrayList<Product> oderProducts;
    private boolean orderSent;

    public Order(ArrayList<Product> oderProducts, double OderPrice, boolean orderSent)
    {
        this.oderProducts = oderProducts;
        this.OderPrice = OderPrice;
        this.orderSent = orderSent;
    }

    public double getOderPrice() {
        return OderPrice;
    }

    public ArrayList<Product> getOderProducts() {
        return oderProducts;
    }

    public boolean isOrderSent() {
        return orderSent;
    }

    public double getOrderPrice() {
        return OderPrice;
    }
}
