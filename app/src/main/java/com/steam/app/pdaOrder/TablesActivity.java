package com.steam.app.pdaOrder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.Model.TableItem;
import com.steam.app.pdaOrder.adapter.TableAdapter;
import java.util.ArrayList;

public class TablesActivity extends AppCompatActivity {

    public String catId;
    public int CatId;
    private ArrayList<TableItem> tableItemArrayList;  //List items Array
    private GridView tablesList;
    private static final String TAG = TablesActivity.class.getName();
    public ArrayList<TableCategoryItem> itemArrayList;
    public ArrayList<ProductCategory> PCArrayList;
    public ArrayList<Product> ProductArrayList;
    TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        Log.e(TAG, "started");

        TextView catNameText = findViewById(R.id.catName);
        TextView catIdText = findViewById(R.id.catId);
        tablesList = findViewById(R.id.tablesList);
        tableItemArrayList = new ArrayList<>(); // ArrayList Initialization

        Intent i = getIntent();
        CatId = i.getIntExtra("catId", 0);
        Log.e("CatId: ", CatId+"");
        String catName = i.getStringExtra("catName");
        Log.e("catName: ", catName+"");
        itemArrayList = (ArrayList<TableCategoryItem>) i.getSerializableExtra("TableCategoryArrayList");
        PCArrayList = (ArrayList<ProductCategory>) i.getSerializableExtra("PCArrayList");
        ProductArrayList = (ArrayList<Product>) i.getSerializableExtra("ProductArrayList");

        //create database
        SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase",MODE_PRIVATE,null);

        String sql = "SELECT name, id, catid FROM Tables WHERE catid = '" + CatId + "';";
        Cursor  cursor = myDatabase.rawQuery(sql,null);
        String TableId = "id";
        String TableName = "name";

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(TableName));
                Log.i(TAG + " name", name);
                int id = cursor.getInt(cursor.getColumnIndex(TableId));
                Log.i(TAG + " id", id + "");
                TableItem mTable = new TableItem();
                mTable.setTableName(name);
                mTable.setCatId(CatId);
                mTable.setTableId(id);
                tableItemArrayList.add(mTable);
                cursor.moveToNext();
            }
        }

        tableAdapter = new TableAdapter(this,tableItemArrayList);
        tablesList.setAdapter(tableAdapter);

        tablesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                TableItem tableItem = tableItemArrayList.get(position);
                String name  = tableItem.getTableName();
                Log.i("table name: ", name);
                int id = tableItem.getTableId();
                Log.i("table id: ", id+"");
                Log.i("item clicked", position+"");

                Toast.makeText(TablesActivity.this, ""+name+" "+id+"",
                        Toast.LENGTH_LONG).show();

                Intent toTable = new Intent(TablesActivity.this, SingleTableActivity.class);

                toTable.putExtra("TableName", name);
                toTable.putExtra("TableId", id);
                toTable.putExtra("TableCategoryArrayList", itemArrayList);
                toTable.putExtra("PCArrayList", PCArrayList);
                toTable.putExtra("ProductArrayList", ProductArrayList);
                toTable.putExtra("catId", CatId);

                startActivity(toTable);
            }
        });

//        int listSize = tableItemArrayList.size();
//        for (int j = 0; j < listSize; j++){
//            Log.i("Table name: ", tableItemArrayList.get(j).getTableName());
//        }
//        int listSize = itemArrayList.size();
//        for (int j = 0; j<listSize; j++){
//            Log.i(TAG+" itemArrayList name: ", itemArrayList.get(j).getName());
//        }
//
//        int listSize2 = PCArrayList.size();
//        for (int j = 0; j<listSize2; j++){
//            Log.i(TAG+" PCArrayList name: ", PCArrayList.get(j).getCategoryName());
//        }
//
//        int listSize3 = ProductArrayList.size();
//        for (int j = 0; j<listSize3; j++){
//            Log.i(TAG+" ProductArrayList name ", ProductArrayList.get(j).getProductName());
//        }

        catNameText.setText(catName);
        catIdText.setText(catId);
    }
}
