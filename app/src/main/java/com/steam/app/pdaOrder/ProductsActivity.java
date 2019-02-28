package com.steam.app.pdaOrder;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.steam.app.pdaOrder.DB.DBAdapter;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.adapter.ProductAdapter;
import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    private ArrayList<Product> ProductArrayList;  //List items Array
    private ArrayList<Product> ProductExtraArrayList;
    private GridView ProductListView; // ListView
    private GridView ProductExtraListView;
    private static final String TAG = ProductsActivity.class.getName();
    public String ProductName;
    public int id;
    TextView ProductNameTextView, total;
    public int catId;
    ImageButton toCart;
    Button back;
    private ArrayList<Product> productsToCart;
    public ArrayList<TableCategoryItem> itemArrayList;
    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    ProductAdapter mAdapter;
    TextView search;
    ImageButton homeButton;
    public double cost;

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

        ProductNameTextView = findViewById(R.id.TableHead);
        ProductNameTextView.setText(ProductName);

        mAdapter = new ProductAdapter(this,ProductArrayList);
        ProductListView.setAdapter(mAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                ProductsActivity.this.mAdapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProductsActivity.this, mAdapter.getItem(i).getProductName(), Toast.LENGTH_LONG).show();
                mAdapter.getItem(i).setMayNeedExtra(false);
                productsToCart.add(mAdapter.getItem(i));
                int listSize = productsToCart.size();
                for (int j = 0; j < listSize; j++) {
                    Log.i("Product to cart: ", productsToCart.get(j).getProductName());
                    Log.i("Cost to cart: ", productsToCart.get(j).getPrice()+"");
                    cost = cost + productsToCart.get(j).getPrice();
                }
            }
        });

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
                Log.e(TAG+" Table Name: ", ProductName + "");
                Log.e(TAG+" catId: ", catId + "");

                Intent toCart = new Intent(ProductsActivity.this, CartActivity.class);
                toCart.putExtra("cartList", productsToCart); //Optional parameters
                toCart.putExtra("TableId", id);
                toCart.putExtra("catId", catId);
                ProductsActivity.this.startActivity(toCart);
            }
        });
    }

    public void ReturnBack(View view){
        super.onBackPressed();
    }
}
