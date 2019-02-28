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
    private int ProductType;
    private String order_printer;
    private int order_enb;
    private int BonusActive;
    private int BonusType;
    private double bonus_quantity;
    private int DiscountActive;
    private int DiscGenActive;
    private String guid;
    private int shopType;
    private int isCompl;
    private boolean mayNeedExtra;
    private String ProductCategory;

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

    public Product(String categoryName, double price, int id, int catId, int ProductType, String order_printer, int order_enb, String guid, int BonusActive, int BonusType, double bonus_quantity,
                   int DiscountActive, int DiscGenActive, int shopType, int isCompl, String ProductCategory) {
        this.productName = categoryName;
        this.price = price;
        this.id = id;
        this.catId = catId;
        this.ProductType = ProductType;
        this.order_printer = order_printer;
        this.order_enb = order_enb;
        this.guid = guid;
        this.BonusActive = BonusActive;
        this.BonusType = BonusType;
        this.bonus_quantity = bonus_quantity;
        this.DiscountActive = DiscountActive;
        this.DiscGenActive = DiscGenActive;
        this.shopType = shopType;
        this.isCompl = isCompl;
        this.ProductCategory = ProductCategory;
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

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public String getOrder_printer() {
        return order_printer;
    }

    public void setOrder_printer(String order_printer) {
        this.order_printer = order_printer;
    }

    public int getOrder_enb() {
        return order_enb;
    }

    public void setOrder_enb(int order_enb) {
        this.order_enb = order_enb;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getBonusActive() {
        return BonusActive;
    }

    public void setBonusActive(int bonusActive) {
        BonusActive = bonusActive;
    }

    public int getBonusType() {
        return BonusType;
    }

    public void setBonusType(int bonusType) {
        BonusType = bonusType;
    }

    public double getBonus_quantity() {
        return bonus_quantity;
    }

    public void setBonus_quantity(double bonus_quantity) {
        this.bonus_quantity = bonus_quantity;
    }

    public int getDiscountActive() {
        return DiscountActive;
    }

    public void setDiscountActive(int discountActive) {
        DiscountActive = discountActive;
    }

    public int getDiscGenActive() {
        return DiscGenActive;
    }

    public void setDiscGenActive(int discGenActive) {
        DiscGenActive = discGenActive;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public int getIsCompl() {
        return isCompl;
    }

    public void setIsCompl(int isCompl) {
        this.isCompl = isCompl;
    }

    public boolean isMayNeedExtra() {
        return mayNeedExtra;
    }

    public void setMayNeedExtra(boolean mayNeedExtra) {
        this.mayNeedExtra = mayNeedExtra;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

}

