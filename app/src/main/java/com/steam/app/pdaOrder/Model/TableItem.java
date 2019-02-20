package com.steam.app.pdaOrder.Model;

import java.io.Serializable;

public class TableItem implements Serializable {

    public TableItem()
    {
    }

    private int TableId;

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    private String TableName;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    private int catId;

    public void setTableId(int tableId) {
        TableId = tableId;
    }

    public TableItem(int TableId, String TableName, int catId)
    {
        this.TableId = TableId;
        this.TableName = TableName;
        this.catId = catId;
    }

    public int getTableId() {
        return TableId;
    }

    public String getTableName() {
        return TableName;
    }
}
