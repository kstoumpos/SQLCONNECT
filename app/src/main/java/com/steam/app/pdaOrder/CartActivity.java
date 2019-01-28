package com.steam.app.pdaOrder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

        productsListView = findViewById(R.id.products_listView);
        ArrayList<Product> myList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");

        int listSize = myList.size();
        for (int i = 0; i<listSize; i++){
            Log.i("Product name: ", myList.get(i).getCategoryName());
        }

        adapter = new CartAdapter(myList,getApplicationContext());
        productsListView.setAdapter(adapter);
    }
}
