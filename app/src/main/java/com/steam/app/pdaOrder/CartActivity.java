package com.steam.app.pdaOrder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        ArrayList<Product> myList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");

        int listSize = myList.size();
        for (int i = 0; i<listSize; i++){
            Log.i("Product name: ", myList.get(i).getProductName());
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
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "send order", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
