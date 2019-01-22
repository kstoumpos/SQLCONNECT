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
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable
    private ArrayList<TableItem> tableItemArrayList;  //List items Array
    private ListView tablesList;
    private MyTableAdapter myTableAdapter; //Array Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        TextView catNameText = findViewById(R.id.catName);
        TextView catIdText = findViewById(R.id.catId);
        tablesList = findViewById(R.id.tablesList);

        connectionClass = new ConnectionClass(); // Connection Class Initialization
        tableItemArrayList = new ArrayList<>(); // ArrayList Initialization

        Intent i = getIntent();
        int CatId = i.getIntExtra("catId", 0);
        Log.e("CatId: ", CatId+"");
        String catName = i.getStringExtra("catName");
        Log.e("catName: ", catName+"");

        catNameText.setText(catName);
        catIdText.setText(catId);

        // Calling Async Task
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
                    String query = "select id,name,capacity,catid,vispriority,shoptype,state,cur_people FROM tbl_setTable";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArrayList using class created
                    {
                        Log.e("Status: ", "rs not null");
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
                        int id = tableItem.getTableCatId();
                        Log.e("table id: ", id+"");
                        Log.e("item clicked", position+"");

//                        Intent i = new Intent(TablesActivity.this, TablesActivity.class);
//                        i.putExtra("catName", name);
//                        i.putExtra("catId", id);
//                        startActivity(i);
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
            TextView imageView;
        }

        public List<TableItem> tableList;

        public Context context;
        ArrayList<TableItem> arrayTableList;

        private MyTableAdapter(List<TableItem> apps, Context context)
        {
            this.tableList = apps;
            this.context = context;
            arrayTableList = new ArrayList<TableItem>();
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
                viewHolder.imageView = rowView.findViewById(R.id.imageView);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (TablesActivity.MyTableAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(tableList.get(position).getTableName()+"");
            viewHolder.imageView.setText(tableList.get(position).getTableId()+"");

            Log.e("Category ListView: ", "OK");
            return rowView;
        }
    }
}
