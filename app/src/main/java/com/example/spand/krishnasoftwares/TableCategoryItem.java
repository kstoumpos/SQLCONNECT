package com.example.spand.krishnasoftwares;

public class TableCategoryItem {


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
