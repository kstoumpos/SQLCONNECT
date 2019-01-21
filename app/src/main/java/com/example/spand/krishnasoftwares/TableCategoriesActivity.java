package com.example.spand.krishnasoftwares;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCategoriesActivity extends AppCompatActivity {

    private ArrayList<TableCategoryItem> itemArrayList;  //List items Array
    private MyAppAdapter myCategoryAdapter; //Array Adapter
    private ListView listView; // ListView
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView); //ListView Declaration
        connectionClass = new ConnectionClass(); // Connection Class Initialization
        itemArrayList = new ArrayList<>(); // ArrayList Initialization

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
                    String query = "select id,name FROM tbl_Category";
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
                                itemArrayList.add(new TableCategoryItem(rs.getString("id"),rs.getString("name")));
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
            Toast.makeText(TableCategoriesActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (!success)
            {
            }
            else {
                try {
                    myCategoryAdapter = new MyAppAdapter(itemArrayList, TableCategoriesActivity.this);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myCategoryAdapter);
                } catch (Exception ex)
                {
                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewHolder which holds
    {
        public class ViewHolder
        {
            TextView textName;
        }

        public List<TableCategoryItem> categoryList;

        public Context context;
        ArrayList<TableCategoryItem> arrayList;

        private MyAppAdapter(List<TableCategoryItem> category, Context context)
        {
            this.categoryList = category;
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
            ViewHolder viewHolder = null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = (TextView) rowView.findViewById(R.id.textName);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names
            viewHolder.textName.setText(categoryList.get(position).getName()+"");

            return rowView;
        }
    }

}
