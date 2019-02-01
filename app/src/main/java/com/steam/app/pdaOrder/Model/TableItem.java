package com.steam.app.pdaOrder.Model;

import java.io.Serializable;

public class TableItem implements Serializable {

    private int TableId;
    private String TableName;

    public TableItem(int TableId, String TableName)
    {
        this.TableId = TableId;
        this.TableName = TableName;
    }

    public int getTableId() {
        return TableId;
    }

    public String getTableName() {
        return TableName;
    }
}
