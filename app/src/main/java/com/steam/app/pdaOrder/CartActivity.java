package com.steam.app.pdaOrder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.adapter.CartAdapter;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView productsListView;
    private static CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageButton updateOrder = findViewById(R.id.updateOrder);
        ImageButton sendOrder = findViewById(R.id.send_order);
        productsListView = findViewById(R.id.products_listView);
        final ArrayList<Product> myList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");
        double cost = 0;
        //create database
        final SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase",MODE_PRIVATE,null);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id INT, products VARCHAR,cost DOUBLE );");
        Log.i("on update","table cart created");

        //myDatabase.execSQL("SELECT id, products, cost FROM Cart;");
        Cursor cursor = myDatabase.rawQuery("select * from Cart",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("products"));
                double price = cursor.getDouble(cursor.getColumnIndex("cost"));
                Product mProduct = new Product(name, price, 0, 0);
                myList.add(mProduct);
                cursor.moveToNext();
            }
        }

        final int listSize = myList.size();
        for (int i = 0; i<listSize; i++){
            Log.i("Product name: ", myList.get(i).getProductName());
            cost = cost + myList.get(i).getPrice();
            Log.i("Order price", cost+"");
        }

        Gson gson = new Gson();
        final String inputString = gson.toJson(myList);

        adapter = new CartAdapter(myList,getApplicationContext());
        productsListView.setAdapter(adapter);
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "update", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id INT, products VARCHAR,cost DOUBLE );");
                Log.i("on update","table cart created");
                finish();
                startActivity(getIntent());
                if (listSize != 0) {
                    for (int i = 0; i < listSize; i++) {
//                        Log.i("Product name: ", myList.get(i).getProductName());
                        myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + myList.get(i).getProductName() + "', '" + myList.get(i).getPrice() + "');");
                        Log.i(myList.get(i).getProductName(), "to db");
                    }
                }
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "send order", Toast.LENGTH_SHORT).show();
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                myList.clear();
                Log.i("cart table content", "deleted");
                Intent toTables = new Intent(CartActivity.this, TableCategoriesActivity.class);
                CartActivity.this.startActivity(toTables);
            }
        });
    }
}
