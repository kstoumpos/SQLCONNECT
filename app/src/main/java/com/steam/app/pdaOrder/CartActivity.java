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
    public int catId, id;
    private static final String TAG = CartActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //create database
        final SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase", MODE_PRIVATE, null);

        ImageButton updateOrder = findViewById(R.id.updateOrder);
        ImageButton sendOrder = findViewById(R.id.send_order);
        productsListView = findViewById(R.id.products_listView);
        Button toProducts = findViewById(R.id.toProducts);
        final ArrayList<Product> myList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");
        double cost = 0;

        //getting table TableId and catId
        Intent toCart = getIntent();
        Bundle bundle = toCart.getExtras();

        if (bundle != null) {
            catId = bundle.getInt("catId");
            id = bundle.getInt("TableId");
        } else {
            catId = 0;
            id = 0;
        }
        Log.e(TAG + " catId: ", catId + "");
        Log.e(TAG + " id: ", id + "");

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id INT, products VARCHAR,cost DOUBLE );");
        Log.i("on update", "table cart created");

        //myDatabase.execSQL("SELECT id, products, cost FROM Cart;");
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Cart", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("products"));
                double price = cursor.getDouble(cursor.getColumnIndex("cost"));
                Product mProduct = new Product(name, price, 0, 0);
                Log.i("cart products from db", name);
                myList.add(mProduct);
                cursor.moveToNext();
            }
        }

        int listSize = myList.size();
        for (int i = 0; i < listSize; i++) {
            Log.i("Product name: ", myList.get(i).getProductName());
            cost = cost + myList.get(i).getPrice();
            Log.i("Order price", cost + "");
        }

        adapter = new CartAdapter(myList, getApplicationContext());
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
                Log.i("on update", "table cart created");
                adapter.notifyDataSetChanged();
                productsListView.setAdapter(adapter);

                startActivity(getIntent());
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                for (int i = 0; i < adapter.getCount(); i++) {
                    adapter.getItem(i);
                    Log.i("updated cart with", adapter.getItem(i).getProductName());
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
                toTables.putExtra("catId", catId);
                myDatabase.close();
                CartActivity.this.startActivity(toTables);
            }
        });

        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("to products", "clicked");
                onBackPressed();
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                for (int i = 0; i < adapter.getCount(); i++) {
                    adapter.getItem(i);
                    myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + adapter.getItem(i).getProductName() + "', '" + adapter.getItem(i).getPrice() + "');");
                    Log.i("updated db with", adapter.getItem(i).getProductName());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //create database
        final SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase", MODE_PRIVATE, null);
        Log.i("to products", "clicked");
        myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getItem(i);
            myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + adapter.getItem(i).getProductName() + "', '" + adapter.getItem(i).getPrice() + "');");
            Log.i("updated db with", adapter.getItem(i).getProductName());
        }
        super.onBackPressed();
    }
}