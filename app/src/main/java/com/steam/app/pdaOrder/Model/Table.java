package com.steam.app.pdaOrder.Model;

public class Table {

    private int TableId;
    private String TableName;
    private int TableCapacity;
    private int TableCatId;
    private int TableVisPriority;
    private int TableShopType;
    private int TableState;
    private int TableCurPeople;

    public Table(int TableId, String TableName, int TableCapacity, int TableCatId, int TableVisPriority, int TableShopType, int TableState, int TableCurPeople)
    {
        this.TableCapacity = TableCapacity;
        this.TableId = TableId;
        this.TableName = TableName;
        this.TableCatId = TableCatId;
        this.TableCurPeople = TableCurPeople;
        this.TableVisPriority = TableVisPriority;
        this.TableShopType = TableShopType;
        this.TableState = TableState;
    }

    public int getTableId() {
        return TableId;
    }

    public void setTableId(int tableId) {
        TableId = tableId;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public int getTableCapacity() {
        return TableCapacity;
    }

    public void setTableCapacity(int tableCapacity) {
        TableCapacity = tableCapacity;
    }

    public int getTableCatId() {
        return TableCatId;
    }

    public void setTableCatId(int tableCatId) {
        TableCatId = tableCatId;
    }

    public int getTableVisPriority() {
        return TableVisPriority;
    }

    public void setTableVisPriority(int tableVisPriority) {
        TableVisPriority = tableVisPriority;
    }

    public int getTableShopType() {
        return TableShopType;
    }

    public void setTableShopType(int tableShopType) {
        TableShopType = tableShopType;
    }

    public int getTableState() {
        return TableState;
    }

    public void setTableState(int tableState) {
        TableState = tableState;
    }

    public int getTableCurPeople() {
        return TableCurPeople;
    }

    public void setTableCurPeople(int tableCurPeople) {
        TableCurPeople = tableCurPeople;
    }
}
