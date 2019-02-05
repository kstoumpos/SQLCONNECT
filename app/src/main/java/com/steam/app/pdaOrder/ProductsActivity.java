package com.steam.app.pdaOrder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.steam.app.pdaOrder.Model.Order;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.adapter.CategoryAdapter;
import com.steam.app.pdaOrder.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private ArrayList<Product> ProductArrayList;  //List items Array
    private ArrayList<Product> ProductExtraArrayList;
    private GridView ProductListView; // ListView
    private GridView ProductExtraListView;
    private static final String TAG = ProductsActivity.class.getName();
    public String ProductName;
    public long id;
    TextView ProductNameTextView;
    public int catId;
    ImageButton toCart;
    Order myOrder;
    private ArrayList<Product> productsToCart;
    public ArrayList<TableCategoryItem> itemArrayList;
    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    ProductAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ProductArrayList = new ArrayList<>(); // ArrayList Initialization
        ProductExtraArrayList = new ArrayList<>();
        productsToCart = new ArrayList<Product>();
        ProductListView = findViewById(R.id.productListView);
        ProductExtraListView = findViewById(R.id.confProductListView);
        toCart = findViewById(R.id.toCart);

        //getting table TableName and id
        Intent toTable = getIntent();
        Bundle bundle = toTable.getExtras();

        if (bundle != null) {
            id = bundle.getInt("TableId");
            ProductName = bundle.getString("TableName");
            catId = bundle.getInt("catId");
        } else {
            id = 0;
            ProductName = null;
            catId = 0;
        }
        Log.e(TAG+" TableId: ", id + "");
        Log.e(TAG+" TableName: ", ProductName + "");
        Log.e(TAG+" catId: ", catId + "");

        itemArrayList = (ArrayList<TableCategoryItem>) toTable.getSerializableExtra("TableCategoryArrayList");
        PCArrayList = (ArrayList<ProductCategory>) toTable.getSerializableExtra("PCArrayList");
        ProductArrayList = (ArrayList<Product>) toTable.getSerializableExtra("ProductArrayList");

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
//            Log.i(TAG+" ProductArrayList name ", ProductArrayList.get(j).getCategoryName());
//        }

        ProductNameTextView = findViewById(R.id.TableHead);
        ProductNameTextView.setText(ProductName);

        mAdapter = new ProductAdapter(this,ProductArrayList);
        ProductListView.setAdapter(mAdapter);


        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductsActivity.this, "test", Toast.LENGTH_SHORT).show();
                Log.e(TAG+" TableId: ", id + "");
                Log.e(TAG+" TableName: ", ProductName + "");
                Log.e(TAG+" catId: ", catId + "");

                Intent toCart = new Intent(ProductsActivity.this, CartActivity.class);
                toCart.putExtra("cartList", productsToCart); //Optional parameters
                ProductsActivity.this.startActivity(toCart);
            }
        });

    }
}
