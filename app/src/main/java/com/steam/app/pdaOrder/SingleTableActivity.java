package com.steam.app.pdaOrder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.adapter.CategoryAdapter;
import java.util.ArrayList;

public class SingleTableActivity extends AppCompatActivity {

    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    private GridView PCListView; // ListView
    private static final String TAG = SingleTableActivity.class.getName();
    public String TableName;
    public int TableId, catId;
    TextView tableNameTextView;
    public ArrayList<TableCategoryItem> itemArrayList;
    public ArrayList<Product> ProductArrayList;
    CategoryAdapter mAdapter;
    int CategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_table);

        PCArrayList = new ArrayList<>(); // ArrayList Initialization
        PCListView = findViewById(R.id.ProductListView);

        //getting table TableName and id
        Intent toTable = getIntent();
        Bundle bundle = toTable.getExtras();

        if (bundle != null) {
            TableId = bundle.getInt("TableId");
            TableName = bundle.getString("TableName");
            catId = bundle.getInt("catId");
        } else {
            TableId = 0;
            TableName = null;
            catId = 0;
        }

        CategoryId = catId;
        Log.e(TAG+" TableId: ", TableId + "");
        Log.e(TAG+" TableName: ", TableName + "");
        Log.e("CategoryId", CategoryId + "");

        itemArrayList = (ArrayList<TableCategoryItem>) toTable.getSerializableExtra("TableCategoryArrayList");
        PCArrayList = (ArrayList<ProductCategory>) toTable.getSerializableExtra("PCArrayList");
//        if(PCArrayList.isEmpty()) {
//            String sql = "SELECT name,price,id,category_id FROM Products;";
//            myDatabase.execSQL("SELECT name,price,id,category_id FROM Products;");
//            Cursor categories = myDatabase.rawQuery(sql, null);
//            TableItem tableItem = new TableItem();
//            int tableName = categories.getColumnIndex("name");
//            Log.e("tableName", tableName + "");
////            TableCategoryArrayList.add(new TableCategoryItem(rs.getString("name"),rs.getInt("id")));
//        }
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
//            Log.i(TAG+" ProductArrayList name ", ProductArrayList.get(j).getProductName());
//        }

        tableNameTextView = findViewById(R.id.TableHeader);
        tableNameTextView.setText(TableName);

        mAdapter = new CategoryAdapter(this,PCArrayList);
        PCListView.setAdapter(mAdapter);

        PCListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.i("item clicked", position + "");
                Log.i("catId clicked", position + 1 + "");
                Log.i("CategoryId", CategoryId + "");
                int catId  = position + 1;

                Intent toProducts = new Intent(SingleTableActivity.this, ProductsActivity.class);
                toProducts.putExtra("TableName", TableName);
                toProducts.putExtra("TableId", TableId);
                //itemArrayList, PCArrayList, ProductArrayList
                toProducts.putExtra("TableCategoryArrayList", itemArrayList);
                toProducts.putExtra("PCArrayList", PCArrayList);
                toProducts.putExtra("ProductArrayList", ProductArrayList);
                toProducts.putExtra("catId", catId);
                toProducts.putExtra("CategoryId", CategoryId);
                SingleTableActivity.this.startActivity(toProducts);
            }
        });
    }

    public void ReturnBack(View view){
        super.onBackPressed();
    }
}