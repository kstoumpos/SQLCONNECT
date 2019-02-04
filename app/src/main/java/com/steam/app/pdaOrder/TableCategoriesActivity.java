package com.steam.app.pdaOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;
import android.widget.Toast;

import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCategoriesActivity extends AppCompatActivity {

    private ArrayList<TableCategoryItem> TableCategoryArrayList;  //List items Array
    private ArrayList<Product> ProductArrayList;  //List items Array
    private ArrayList<ProductCategory> PCArrayList;  //List items Array
    private TablesAdapter myCategoryAdapter; //Array DbAdapter
    private GridView listView; // ListView
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable
    public int catId;
    private static final String TAG = TableCategoriesActivity.class.getName();
    int ShpType = 1;
    public int CatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_categories);

        Log.e(TAG, "started");

        listView = findViewById(R.id.listView); //ListView Declaration
        connectionClass = new ConnectionClass(); // Connection Class Initialization
        TableCategoryArrayList = new ArrayList<>(); // ArrayList Initialization
        PCArrayList = new ArrayList<>();
        ProductArrayList = new ArrayList<>();
        catId = 0;

        // Calling Async Task
        SyncData orderData = new SyncData();
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
            progress = ProgressDialog.show(TableCategoriesActivity.this, "Synchronising",
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
                    // Change below query according to your own database.
                    String query = "select id, name FROM tbl_Category";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    //create database
                    SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase",MODE_PRIVATE,null);

                    if (rs != null) // if resultset not null, I add items to TableCategoryArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");

                        //create table
                        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS tbl_Category(id VARCHAR,name VARCHAR);");
                        while (rs.next())
                        {
                            try {
                                Log.e("id: ", rs.getString("id"));
                                Log.e("name: ", rs.getString("name"));

                                TableCategoryArrayList.add(new TableCategoryItem(rs.getString("name"),rs.getInt("id")));
                                //insert data into table
                                myDatabase.execSQL("INSERT INTO tbl_Category VALUES('rs.getString(\"name\")','rs.getInt(\"id\")');");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("Status: ", "exception after query");
                            }
                        }
                        msg = "Found table categories";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }

                    Log.e("query 2", "just started");
                    String query2 = "select des, id name FROM Product_Category";
                    ResultSet rs2 = stmt.executeQuery(query2);

                    if (rs2 != null) // if resultset not null, I add items to TableCategoryArrayList using class created
                    {
                        Log.e("Status: ", "rs2 not null");

                        //create table
                        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Product_Category(name VARCHAR,id INT);");
                        while (rs2.next())
                        {
                            try {
                                //Log.e("id: ", rs2.getString("id"));
                                Log.e("name: ", rs2.getString("des"));
                                catId++;
                                PCArrayList.add(new ProductCategory(rs2.getString("des")));
                                //insert data into table
                                myDatabase.execSQL("INSERT INTO Product_Category VALUES('rs2.getString(\"name\")','rs2.getInt(\"id\")');");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("Status: ", "exception after query");
                            }
                        }
                        msg = "Found product categories";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }

                    String Sql_str;
                    String WHCODE = "1000";
                    String extra = "180";
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
                    Sql_str += " Where Products.product_type<>1 And Products.product_type<>3 And Products.product_type<>4 And type_paragwghs<>" + extra; //na mhn einai kai xtra choise proion
                    Sql_str += " and Products_WH.WHCODE=" + WHCODE;
                    Sql_str += " and Products.category_id="+ catId;
                    Sql_str += " order by Products.priority,Products.des;";
                    Log.e("swl string", Sql_str);
                    ResultSet rs3 = stmt.executeQuery(Sql_str);

                    if (rs3 != null) // if resultset not null, I add items to TableCategoryArrayList using class created
                    {
                        Log.e("Status: ", "rs2 not null");

                        //create table
                        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Products(name VARCHAR,price DOUBLE,id INT);");
                        while (rs3.next())
                        {
                            try {
                                //Log.e("id: ", rs3.getString("id"));
                                Log.e("name: ", rs3.getString("des"));

                                ProductArrayList.add(new Product(rs3.getString("des"),rs3.getDouble("price"),rs3.getInt("id")));
                                //insert data into table
                                myDatabase.execSQL("INSERT INTO Products VALUES('rs3.getString(\"des\")','rs3.getDouble(\"price\")','rs3.getInt(\"id\")');");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e("Status: ", "exception after query");
                            }
                        }
                        msg = "Found product categories";
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
            Toast.makeText(TableCategoriesActivity.this, msg + "", Toast.LENGTH_LONG).show();
            Log.e("success", "is true");
            if (!success)
            {
                Log.e("success", "is false");
            }
            else {
                try {
                    myCategoryAdapter = new TablesAdapter(TableCategoryArrayList, TableCategoriesActivity.this);
                    listView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

                    listView.setAdapter(myCategoryAdapter);

                    Log.e("adapter: ", "OK");
                } catch (Exception ex)
                {
                    Log.e("adapter: ", "ERROR");
                    Log.e("error: ", ex.toString());
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        TableCategoryItem TableCategoryItem = TableCategoryArrayList.get(position);
                        String name  = TableCategoryItem.getName();
                        Log.e("Category name: ", name);
                        int id = TableCategoryItem.getId();
                        Log.e("Category id: ", id+"");
                        Log.e("item clicked", position+"");

                        Intent i = new Intent(TableCategoriesActivity.this, TablesActivity.class);
                        i.putExtra("catName", name);
                        i.putExtra("catId", id);
                        //TableCategoryArrayList, PCArrayList, ProductArrayList
                        i.putExtra("TableCategoryArrayList", TableCategoryArrayList);
                        i.putExtra("PCArrayList", PCArrayList);
                        i.putExtra("ProductArrayList", ProductArrayList);

                        int listSize = TableCategoryArrayList.size();
                        for (int j = 0; j<listSize; j++){
                            Log.i(TAG+" itemArrayList name: ", TableCategoryArrayList.get(j).getName());
                        }

                        int listSize2 = PCArrayList.size();
                        for (int j = 0; j<listSize2; j++){
                            Log.i(TAG+" PCArrayList name: ", PCArrayList.get(j).getCategoryName());
                        }

                        int listSize3 = ProductArrayList.size();
                        for (int j = 0; j<listSize3; j++){
                            Log.i(TAG+" ProductArrayList name ", ProductArrayList.get(j).getCategoryName());
                        }

                        startActivity(i);
                    }
                });
            }
        }
    }

    public class TablesAdapter extends BaseAdapter         //has a class viewHolder which holds
    {
        public class ViewHolder
        {
            TextView textName;
            TextView textId;
        }

        public List<TableCategoryItem> categoryList;

        public Context context;
        ArrayList<TableCategoryItem> arrayList;

        private TablesAdapter(List<TableCategoryItem> apps, Context context)
        {
            this.categoryList = apps;
            this.context = context;
            arrayList = new ArrayList<>();
            arrayList.addAll(categoryList);
        }

        @Override
        public int getCount() {
            return categoryList.size();
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
                rowView = inflater.inflate(R.layout.category_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = rowView.findViewById(R.id.textName);
                viewHolder.textId = rowView.findViewById(R.id.textCatId);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(categoryList.get(position).getName()+"");
            viewHolder.textId.setText(categoryList.get(position).getId()+"");

            Log.e("Category ListView: ", "OK");
            return rowView;
        }
    }

}
