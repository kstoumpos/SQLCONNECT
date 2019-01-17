package com.example.spand.krishnasoftwares;

public class TableCategoryItem {

    public String name; //Name
    public String id; //ID

    public TableCategoryItem(String name, String id)
    {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}