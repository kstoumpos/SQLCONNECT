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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.Model.ProductCategory;
import com.steam.app.pdaOrder.Model.TableCategoryItem;
import com.steam.app.pdaOrder.Model.TableItem;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TablesActivity extends AppCompatActivity {

    public String catName;
    public String catId;
    public int CatId;
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable
    private ArrayList<TableItem> tableItemArrayList;  //List items Array
    private GridView tablesList;
    private MyTableAdapter myTableAdapter; //Array DbAdapter
    int ShpType = 1;
    private static final String TAG = TablesActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        Log.e(TAG, "started");

        TextView catNameText = findViewById(R.id.catName);
        TextView catIdText = findViewById(R.id.catId);
        tablesList = findViewById(R.id.tablesList);

        connectionClass = new ConnectionClass(); // Connection Class Initialization
        tableItemArrayList = new ArrayList<>(); // ArrayList Initialization

        Intent i = getIntent();
        CatId = i.getIntExtra("catId", 0);
        Log.e("CatId: ", CatId+"");
        String catName = i.getStringExtra("catName");
        Log.e("catName: ", catName+"");
        ArrayList<TableCategoryItem> itemArrayList = (ArrayList<TableCategoryItem>) i.getSerializableExtra("TableCategoryArrayList");
        ArrayList<ProductCategory> PCArrayList = (ArrayList<ProductCategory>) i.getSerializableExtra("PCArrayList");
        ArrayList<Product> ProductArrayList = (ArrayList<Product>) i.getSerializableExtra("ProductArrayList");

        int listSize = itemArrayList.size();
        for (int j = 0; j<listSize; j++){
            Log.i(TAG+" itemArrayList name: ", itemArrayList.get(j).getName());
        }

        int listSize2 = PCArrayList.size();
        for (int j = 0; j<listSize2; j++){
            Log.i(TAG+" PCArrayList name: ", PCArrayList.get(j).getCategoryName());
        }

        int listSize3 = ProductArrayList.size();
        for (int j = 0; j<listSize3; j++){
            Log.i(TAG+" ProductArrayList name ", ProductArrayList.get(j).getCategoryName());
        }

        catNameText.setText(catName);
        catIdText.setText(catId);

//        // Calling Async Task
        TablesActivity.SyncData orderData = new TablesActivity.SyncData();
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
            progress = ProgressDialog.show(TablesActivity.this, "Synchronising",
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
                    //String query = "select id,name,capacity,catid,vispriority,shoptype,state,cur_people FROM tbl_setTable where catid=" + catId +"";
                    //String query = "select id,name,capacity,catid,vispriority,shoptype,state,cur_people FROM tbl_setTable";
                    String Sql_str;
                    Sql_str = "Select id,name,capacity,isnull(tbl_setTable.state,0) as state,isnull(cur_people,0) as cur_people,dateOpened";
                    Sql_str += " From tbl_setTable ";
                    Sql_str += "Where shoptype=" + ShpType + " and catid =" + CatId; //to posto pou epilex8ike CatId
                    Sql_str += " Order by tbl_setTable.vispriority;";

//
//                    Dim Sql_str As String = ""
//                    Sql_str = "Select Products.id,Products.des,bonus_active,bonus_quantity,bonus_type,"
//                    Sql_str += "Products.category_id,discount_active,discount_precent,disc_gen_active,disc_gen_precent,"
//                    Sql_str += "isCompl,Products.guid,"
//                    Sql_str += "isnull(price_format,'N2') as price_format,type_paragwghs,Products.shopType,"
//                    Sql_str += "Products.category_des,"
//                    Sql_str += "Products_WH.price1 as price,"
//                    Sql_str += "isnull(Products_WH.order_enb,0) as order_enb,isnull(Products_WH.order_printer,'DISABLE') as order_printer,"
//                    Sql_str += "isnull(Products_WH.price2,0) as price2,product_type"
//
//                    Sql_str += " from Products"
//                    Sql_str += " LEFT JOIN Products_WH on Products.guid=Products_WH.prguid"
//                    Sql_str += $" where product_type<>1 And product_type <> 3 And product_type <> 4 And type_paragwghs <> {PRODUCTYPE_EXTRA_EPILOGES}" 'na mhn einai kai xtra choise proion
//                    Sql_str += " and Products_WH.WHCODE=" & WHCODE
//                    Sql_str += " order by Products.priority,Products.des;"
//
//
//                    '=====//////// Gia xtra choise product //////////=========
//                    Sql_str += "Select Products.id,Products.des,bonus_active,bonus_quantity,bonus_type,"
//                    Sql_str += "Products.category_id,discount_active,discount_precent,disc_gen_active,disc_gen_precent,"
//                    Sql_str += "isCompl,Products.guid,"
//                    Sql_str += "isnull(price_format,'N2') as price_format,type_paragwghs,Products.shopType,"
//                    Sql_str += "Products.category_des,"
//                    Sql_str += "Products_WH.price1 as price,"
//                    Sql_str += "isnull(Products_WH.order_enb,0) as order_enb,isnull(Products_WH.order_printer,'DISABLE') as order_printer,"
//                    Sql_str += "isnull(Products_WH.price2,0) as price2,product_type"
//
//                    Sql_str += " from Products"
//                    Sql_str += " LEFT JOIN Products_WH on Products.guid=Products_WH.prguid"
//                    Sql_str += $" where product_type<>1 And product_type <> 3 And product_type <> 4 And type_paragwghs = {PRODUCTYPE_EXTRA_EPILOGES}" 'na mhn einai kai xtra choise proion
//                    Sql_str += " and Products_WH.WHCODE=" & WHCODE
//                    Sql_str += " order by Products.priority,Products.des;"
//
//
//
//                    Sql_str += "Select des,id,shopType from Product_Category where (shopType=100 or shopType=" & ShpType & ") and catid=1 order by vis_priority,des;"
//                    Sql_str += "Select id,optName,category_id,opguid,isnull(its_xtrabtn,0) as its_xtrabtn from Cat_Options;"

                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(Sql_str);
                    if (rs != null) // if resultset not null, I add items to itemArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");
                        Log.e("Result", rs.toString());
                        while (rs.next())
                        {
                            try {
                                Log.e("id: ", rs.getString("id"));
                                Log.e("name: ", rs.getString("name"));
                                tableItemArrayList.add(new TableItem(rs.getInt("id"),rs.getString("name")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
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
            Toast.makeText(TablesActivity.this, msg + "", Toast.LENGTH_LONG).show();
            Log.e("success", "is true");
            if (!success)
            {
                Log.e("success", "is false");
            }
            else {
                try {
                    myTableAdapter = new TablesActivity.MyTableAdapter(tableItemArrayList, TablesActivity.this);
                    tablesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    tablesList.setAdapter(myTableAdapter);

                    Log.e("adapter: ", "OK");
                } catch (Exception ex)
                {
                    Log.e("adapter: ", "ERROR");
                    Log.e("error: ", ex.toString());
                }

                tablesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        TableItem tableItem = tableItemArrayList.get(position);
                        String name  = tableItem.getTableName();
                        Log.e("table name: ", name);
                        int id = tableItem.getTableId();
                        Log.e("table id: ", id+"");
                        Log.e("item clicked", position+"");

                        Toast.makeText(TablesActivity.this, ""+name+" "+id+"",
                                Toast.LENGTH_LONG).show();

                        Intent toTable = new Intent(TablesActivity.this, SingleTableActivity.class);
                        toTable.putExtra("TableName", name);
                        toTable.putExtra("TableId", id);
                        //itemArrayList, PCArrayList, ProductArrayList
                        startActivity(toTable);
                    }
                });
            }
        }
    }

    public class MyTableAdapter extends BaseAdapter         //has a class viewHolder which holds
    {
        public class ViewHolder
        {
            TextView textName;
            TextView textId;
        }

        public List<TableItem> tableList;

        public Context context;
        ArrayList<TableItem> arrayTableList;

        private MyTableAdapter(List<TableItem> apps, Context context)
        {
            this.tableList = apps;
            this.context = context;
            arrayTableList = new ArrayList<>();
            arrayTableList.addAll(tableList);
        }

        @Override
        public int getCount() {
            return tableList.size();
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
            TablesActivity.MyTableAdapter.ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.category_content, parent, false);
                viewHolder = new TablesActivity.MyTableAdapter.ViewHolder();
                viewHolder.textName = rowView.findViewById(R.id.textName);
                viewHolder.textId = rowView.findViewById(R.id.textCatId);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (TablesActivity.MyTableAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up name and id
            viewHolder.textName.setText(tableList.get(position).getTableName()+"");
            viewHolder.textId.setText(CatId+"");

            Log.e("Category ListView: ", "OK");
            return rowView;
        }
    }
}
