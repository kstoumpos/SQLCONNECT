package com.steam.app.pdaOrder.Model;

import java.io.Serializable;

public class TableCategoryItem implements Serializable {


        public int catId; //id
        public String name; //Name

        public TableCategoryItem(String name, int catId)
        {
            this.catId = catId;
            this.name = name;
        }

    public int getId() {
            return catId;
        }

        public String getName() {
            return name;
        }
}
