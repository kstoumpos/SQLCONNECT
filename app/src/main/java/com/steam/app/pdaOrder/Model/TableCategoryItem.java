package com.steam.app.pdaOrder.Model;

import android.os.Parcel;
import java.io.Serializable;

public class TableCategoryItem implements Serializable {


        public int catId; //id
        public String name; //Name

        public TableCategoryItem(String name, int catId)
        {
            this.catId = catId;
            this.name = name;
        }

    protected TableCategoryItem(Parcel in) {
        catId = in.readInt();
        name = in.readString();
    }

    public int getId() {
            return catId;
        }

        public String getName() {
            return name;
        }
}
