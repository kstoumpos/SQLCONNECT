package com.steam.app.pdaOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.steam.app.pdaOrder.Model.Product;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity {

    private ArrayList<Product> ProductArrayList;  //List items Array
    private ProductsActivity.MyAppAdapter myProductAdapter; //Array Adapter
    private ListView ProductListView; // ListView
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable
    private static final String TAG = ProductsActivity.class.getName();
    public String ProductName;
    public int id;
    TextView ProductNameTextView;
    public int catId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ArrayList<String> items = new ArrayList<>();
        ProductArrayList = new ArrayList<>(); // ArrayList Initialization
        ProductListView = findViewById(R.id.productListView);
        connectionClass = new ConnectionClass(); // Connection Class Initialization

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

        ProductNameTextView = findViewById(R.id.TableHead);
        ProductNameTextView.setText(ProductName);

        // Calling Async Task
        ProductsActivity.SyncData orderData = new ProductsActivity.SyncData();
        orderData.execute("");
    }

    // Async Task has three override methods,
    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dialog
        {
            progress = ProgressDialog.show(ProductsActivity.this, "Synchronising",
                    "ListView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try
            {
                Connection conn = connectionClass.CONN(); //Connection Object
                if (conn == null)
                {
                    success = false;
                }
                else {
                    Log.e("CONNECTED", "Ready for query");
                    // Change below query according to your own database
                    String Sql_str;
                    String WHCODE = "1000";
                    String extra = "{PRODUCTYPE_EXTRA_EPILOGES}";
                    Sql_str = "Select Products.id,des,bonus_active,bonus_quantity,bonus_type,";
                    Sql_str += "category_id,discount_active,discount_precent,disc_gen_active,disc_gen_precent,";
                    Sql_str += "isCompl,Products.guid,";
                    Sql_str += "isnull(price_format,'N2') as price_format,type_paragwghs,shopType,";
                    Sql_str += "category_des,";
                    Sql_str += "Products_WH.price1 as price,";
                    Sql_str += "isnull(Products_WH.order_enb,0) as order_enb,isnull(Products_WH.order_printer,'DISABLE') as order_printer,";
                    Sql_str += "isnull(price2,0) as price2,product_type";
                    Sql_str += " from Products";
                    Sql_str += " LEFT JOIN Products_WH on Products.guid=Products_WH.prguid";
                    //Sql_str += " where product_type!=1 And product_type!=3 And product_type!=4 And type_paragwghs!=" + extra; //na mhn einai kai xtra choise proion
                    //todo aporia gia Sergio!
                    Sql_str += " where Products_WH.WHCODE=" + WHCODE;
                    Sql_str += "and  Products.category_id="+ catId;
                    Sql_str += " order by Products.priority,Products.des";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(Sql_str);
                    if (rs != null) // if resultset not null, I add items to itemArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");
                        while (rs.next())
                        {
                            try {
                                //Log.e("category_des: ", rs.getString("category_des"));
                                Log.e("des: ", rs.getString("des"));
                                Log.e("price:", String.valueOf(rs.getDouble("price")));
                                ProductArrayList.add(new Product(rs.getString("des"),rs.getDouble("price")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("Error: ", ex.toString());
                                Log.e("Status: ", "exception after query");
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // dismissing progress dialog, showing error and setting up my listView
        {
            progress.dismiss();
            Toast.makeText(ProductsActivity.this, msg + "", Toast.LENGTH_LONG).show();
            Log.e("success", "is true");
            if (!success)
            {
                Log.e("success", "is false");
            }
            else {
                try {
                    myProductAdapter = new MyAppAdapter(ProductArrayList, ProductsActivity.this);
                    ProductListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    ProductListView.setAdapter(myProductAdapter);
                    Log.e(TAG + " adapter: ", "OK");
                } catch (Exception ex)
                {
                    Log.e(TAG + " adapter: ", "ERROR");
                    Log.e(TAG + " error: ", ex.toString());
                }

                ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        Product product = ProductArrayList.get(position);
                        String name  = product.getCategoryName();
                        Log.e(TAG + " Category name: ", name);
                        Log.e(TAG + " item clicked", position+"");

//                        Intent toProducts = new Intent(SingleTableActivity.this, ProductsActivity.class);
//                        toProducts.putExtra("catName", name);
//                        toProducts.putExtra("catId", id);
//                        startActivity(toProducts);
                    }
                });
            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewHolder which holds
    {
        public class ViewHolder
        {
            TextView textName;
            TextView TableId;
        }

        public List<Product> productCategoryList;

        public Context context;
        ArrayList<Product> arrayList;

        private MyAppAdapter(List<Product> apps, Context context)
        {
            this.productCategoryList = apps;
            this.context = context;
            arrayList = new ArrayList<>();
            arrayList.addAll(productCategoryList);
        }

        @Override
        public int getCount() {
            return productCategoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
        {

            View rowView = convertView;
            ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.product_category_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = rowView.findViewById(R.id.ProductCategoryTitle);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(productCategoryList.get(position).getCategoryName()+"");
            //viewHolder.TableId.setText(TableName);

            Log.e(TAG + " Category ListView: ", "OK");
            return rowView;
        }
    }
}
