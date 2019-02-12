package com.steam.app.pdaOrder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.steam.app.pdaOrder.DB.DBAdapter;
import com.steam.app.pdaOrder.Model.Order;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.adapter.ProductAdapter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Product> ProductArrayList;  //List items Array
    private ArrayList<Product> ProductExtraArrayList;
    private GridView ProductListView; // ListView
    private GridView ProductExtraListView;
    private static final String TAG = ProductsActivity.class.getName();
    public String ProductName;
    public int id;
    TextView ProductNameTextView;
    public int catId;
    ImageButton toCart;
    Order myOrder;
    private ArrayList<Product> productsToCart;
    public ArrayList<TableCategoryItem> itemArrayList;
    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    ProductAdapter mAdapter;
    EditText search;
    ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ProductArrayList = new ArrayList<>(); // ArrayList Initialization
        ProductExtraArrayList = new ArrayList<>();
        productsToCart = new ArrayList<>();
        ProductListView = findViewById(R.id.productListView);
        ProductExtraListView = findViewById(R.id.confProductListView);
        toCart = findViewById(R.id.toCart);
        search = findViewById(R.id.search_src_text);
        homeButton = findViewById(R.id.homeButton);

        //getting table TableName and id
        Intent toTable = getIntent();
        Bundle bundle = toTable.getExtras();

        if (bundle != null) {
            id = toTable.getIntExtra("TableId",0);
            ProductName = toTable.getStringExtra("TableName");
            catId = toTable.getIntExtra("catId", 0);
        } else {
            id = 0;
            ProductName = null;
            catId = 0;
        }
        Log.e(TAG+" TableId: ", id + "");
        Log.e(TAG+" TableName: ", ProductName + "");
        Log.e(TAG+" catId: ", catId + "");

        DBAdapter mDbHelper = new DBAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor productData = mDbHelper.getTestData(catId);
        if (productData.moveToFirst()){
            do{
                Product mProduct = new Product();
                String name = productData.getString(productData.getColumnIndex("name"));
                double price = productData.getDouble(productData.getColumnIndex("price"));
                int id = productData.getInt(productData.getColumnIndex("id"));
                mProduct.setProductName(name);
                mProduct.setPrice(price);
                mProduct.setId(id);
                // do what ever you want here
                Log.i("productData", name+"");
                ProductArrayList.add(mProduct);
            }while(productData.moveToNext());
        }
        productData.close();
        mDbHelper.close();

        itemArrayList = (ArrayList<TableCategoryItem>) toTable.getSerializableExtra("TableCategoryArrayList");
        PCArrayList = (ArrayList<ProductCategory>) toTable.getSerializableExtra("PCArrayList");

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

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProductsActivity.this, TableCategoriesActivity.class);
                ProductsActivity.this.startActivity(myIntent);
            }
        });

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
