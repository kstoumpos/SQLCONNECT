package com.steam.app.pdaOrder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
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
        Button toProducts = findViewById(R.id.toProducts);
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

        int listSize = myList.size();
        for (int i = 0; i<listSize; i++){
            Log.i("Product name: ", myList.get(i).getProductName());
            cost = cost + myList.get(i).getPrice();
            Log.i("Order price", cost+"");
        }

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
                adapter.notifyDataSetChanged();
                productsListView.setAdapter(adapter);

                startActivity(getIntent());
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                for (int i=0;i<adapter.getCount();i++){
                    adapter.getItem(i);
                    Log.i("updated cart with", adapter.getItem(i).getProductName());
                    //myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + adapter.getItem(i).getProductName() + "', '" + adapter.getItem(i).getPrice() + "');");
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

        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("to products", "clicked");
                onBackPressed();
//                Intent toProducts = new Intent(CartActivity.this, SingleTableActivity.class);
//                toProducts.putExtra("alreadyInCartList", myList); //Optional parameters
//                CartActivity.this.startActivity(toProducts);
            }
        });
    }
}
