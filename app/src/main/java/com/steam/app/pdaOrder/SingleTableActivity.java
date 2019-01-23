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
import com.steam.app.pdaOrder.Model.ProductCategory;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SingleTableActivity extends AppCompatActivity {

    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    private SingleTableActivity.MyAppAdapter myProductCategoryAdapter; //Array Adapter
    private ListView PCListView; // ListView
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable
    private static final String TAG = SingleTableActivity.class.getName();
    public String TableName;
    public int id;
    TextView tableNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_table);

        ArrayList<String> items = new ArrayList<>();
        PCArrayList = new ArrayList<>(); // ArrayList Initialization
        PCListView = findViewById(R.id.ProductListView);
        connectionClass = new ConnectionClass(); // Connection Class Initialization

        //getting table TableName and id
        Intent toTable = getIntent();
        Bundle bundle = toTable.getExtras();

        if (bundle != null) {
            id = bundle.getInt("TableId");
            TableName = bundle.getString("TableName");
        } else {
            id = 0;
            TableName = null;
        }
        Log.e(TAG+" TableId: ", id + "");
        Log.e(TAG+" TableName: ", TableName + "");

        tableNameTextView = findViewById(R.id.TableHeader);
        tableNameTextView.setText(TableName);

        // Calling Async Task
        SingleTableActivity.SyncData orderData = new SingleTableActivity.SyncData();
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
            progress = ProgressDialog.show(SingleTableActivity.this, "Synchronising",
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
                    //String query = "select des, category_des, category_id name FROM Products";
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
                    Sql_str += " and Products_WH.WHCODE=" + WHCODE;
                    Sql_str += " order by Products.priority,Products.des";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(Sql_str);
                    if (rs != null) // if resultset not null, I add items to itemArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");
                        while (rs.next())
                        {
                            try {
                                Log.e("category_des: ", rs.getString("category_des"));
                                Log.e("des: ", rs.getString("des"));
                                PCArrayList.add(new ProductCategory(rs.getString("category_des"),rs.getString("des")));
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
            Toast.makeText(SingleTableActivity.this, msg + "", Toast.LENGTH_LONG).show();
            Log.e("success", "is true");
            if (!success)
            {
                Log.e("success", "is false");
            }
            else {
                try {
                    myProductCategoryAdapter = new MyAppAdapter(PCArrayList, SingleTableActivity.this);
                    PCListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    PCListView.setAdapter(myProductCategoryAdapter);
                    Log.e(TAG + " adapter: ", "OK");
                } catch (Exception ex)
                {
                    Log.e(TAG + " adapter: ", "ERROR");
                    Log.e(TAG + " error: ", ex.toString());
                }

                PCListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        ProductCategory productCategory = PCArrayList.get(position);
                        String name  = productCategory.getProductName();
                        Log.e(TAG + " Product name: ", name);
                        int id = productCategory.getId();
                        Log.e(TAG + " Category id: ", id+"");
                        Log.e(TAG + " item clicked", position+"");

//                        Intent i = new Intent(TableCategoriesActivity.this, TablesActivity.class);
//                        i.putExtra("catName", name);
//                        i.putExtra("catId", id);
//                        startActivity(i);
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

        public List<ProductCategory> productCategoryList;

        public Context context;
        ArrayList<ProductCategory> arrayList;

        private MyAppAdapter(List<ProductCategory> apps, Context context)
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
            viewHolder.textName.setText(productCategoryList.get(position).getProductName()+"");
            //viewHolder.TableId.setText(TableName);

            Log.e(TAG + " Category ListView: ", "OK");
            return rowView;
        }
    }
}