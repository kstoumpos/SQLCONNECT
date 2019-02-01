package com.steam.app.pdaOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
    private SingleTableActivity.MyAppAdapter myProductCategoryAdapter; //Array DbAdapter
    private GridView PCListView; // ListView
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

//        // Calling Async Task
//        SingleTableActivity.SyncData orderData = new SingleTableActivity.SyncData();
//        orderData.execute("");
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
                    String query = "select des, id name FROM Product_Category";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");
                        while (rs.next())
                        {
                            try {
                                //Log.e("category_des: ", rs.getString("category_des"));
                                Log.e("des: ", rs.getString("des"));
                                PCArrayList.add(new ProductCategory(rs.getString("des")));
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
                        String name  = productCategory.getCategoryName();
                        Log.e(TAG + " Category name: ", name);
                        Log.e(TAG + " item clicked", position+"");
                        int id = position + 1;
                        Log.e(TAG + " Category id: ", id+"");
                        productCategory.setId(id);

                        Intent toProducts = new Intent(SingleTableActivity.this, ProductsActivity.class);
                        toProducts.putExtra("catName", name);
                        toProducts.putExtra("catId", id);
                        toProducts.putExtra("TableName",TableName);
                        startActivity(toProducts);
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
            viewHolder.textName.setText(productCategoryList.get(position).getCategoryName()+"");
            //viewHolder.TableId.setText(TableName);

            if (position % 2 == 1) {
                rowView.setBackgroundColor(Color.BLUE);
            } else {
                rowView.setBackgroundColor(Color.WHITE);
            }

            Log.e(TAG + " Category ListView: ", "OK");
            return rowView;
        }
    }
}