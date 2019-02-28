package com.steam.app.pdaOrder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.adapter.CartAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    private ListView productsListView;
    private CartAdapter adapter;
    public int catId, TableId;
    private static final String TAG = CartActivity.class.getName();
    TextView mealTotalText;
    ArrayList<Product> orders;
    String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        orders = getListItemData();

        //create database
        final SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase", MODE_PRIVATE, null);

        ImageButton updateOrder = findViewById(R.id.updateOrder);
        ImageButton sendOrder = findViewById(R.id.send_order);
        productsListView = findViewById(R.id.products_listView);
        Button toProducts = findViewById(R.id.toProducts);
        mealTotalText = findViewById(R.id.meal_total);
        final ArrayList<Product> myList = (ArrayList<Product>) getIntent().getSerializableExtra("cartList");
        double cost = 0;

        //getting table TableId and catId
        Intent toCart = getIntent();
        Bundle bundle = toCart.getExtras();

        if (bundle != null) {
            catId = bundle.getInt("catId");
            TableId = bundle.getInt("TableId");
        } else {
            catId = 0;
            TableId = 0;
        }
        Log.e(TAG + " catId: ", catId + "");
        Log.e(TAG + " TableId: ", TableId + "");

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id INT, products VARCHAR,cost DOUBLE, comment VARCHAR);");
        Log.i("on update", "table cart created");

        //myDatabase.execSQL("SELECT id, products, cost FROM Cart;");
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Cart", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("products"));
                double price = cursor.getDouble(cursor.getColumnIndex("cost"));
                Product mProduct = new Product(name, price, 0, 0);
                Log.i("cart products from db", name);
                myList.add(mProduct);
                cursor.moveToNext();
            }
        }

        int listSize = myList.size();
        for (int i = 0; i < listSize; i++) {
            Log.i("Product name: ", myList.get(i).getProductName());
            cost = cost + myList.get(i).getPrice();
            Log.i("Order price", cost + "");
        }

        adapter = new CartAdapter(myList, getApplicationContext());
        productsListView.setAdapter(adapter);
        adapter.registerDataSetObserver(observer);

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Log.i("item clicked", i+"");
                String name  = adapter.getItem(i).getProductName();
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle(name);

                // Set up the input
                final EditText input = new EditText(CartActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Προσθήκη σχολίου", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comment = input.getText().toString();
                        adapter.getItem(i).setComment(comment);
                        Log.i("comment", comment);
                    }
                });
                builder.setNegativeButton("Ακυρο", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "update", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id INT, products VARCHAR,cost DOUBLE,comment VARCHAR);");
                Log.i("on update", "table cart created");
                adapter.notifyDataSetChanged();
                //productsListView.setAdapter(adapter);

                startActivity(getIntent());
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                for (int i = 0; i < adapter.getCount(); i++) {
                    adapter.getItem(i);
                    Log.i("updated cart with", adapter.getItem(i).getProductName());
                }
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "Αποστολή παραγγελίας", Toast.LENGTH_SHORT).show();
                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
                int listSize = myList.size();
                for (int i = 0; i<listSize; i++){
                    Log.i("Order name: ", myList.get(i).getProductName());
                    if(myList.get(i).getComment() != null) {
                        Log.i("Order comment", myList.get(i).getComment());
                    }
                }

                String Sql_Header = null;
                String Sql_Values = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String DtSave = sdf.format(new Date());
                String sql_str = "UPDATE tbl_setTable set state={state_Table},cur_people={CountTotProducts},dateOpened='" + DtSave + "' WHERE id=" + TableId + ";";
                int ProductType;
                int PRODUCTYPE_MANUAL_COMENTS = 181;
                int CountTotProducts = 0;

                //foreach product in Cart
                for (int i = 0; i<listSize; i++) {
                    ProductType = myList.get(i).getProductType();
//                For i As Integer = 0 To ProductsAtCart.Rows.Count - 1
//                With ProductsAtCart.Rows(i)
//                ProductType = .Item("type_paragwghs");
//
//                If .Item("des").ToString = "" Then Continue For 'einai manual sxolio kai uparxei epita apo ka8e product
//
                    Sql_Header += ("INSERT INTO PDA_Order_Detail (");
                    Sql_Values += (" Values(");
                    Sql_Header += ("quant,");
                    Log.i(TAG + " getmQuantity()", myList.get(i).getmQuantity()+"");
                    CountTotProducts = CountTotProducts + myList.get(i).getmQuantity();
                    String Sql_Values2 = Sql_Values.replace(myList.get(i).getmQuantity()+"", ".");
                    Sql_Values2 += ",";
                    Sql_Values = Sql_Values2;
                    Sql_Values2 = null;

                    if(ProductType == PRODUCTYPE_MANUAL_COMENTS  && myList.get(i).getProductName() != null ){

                        Sql_Header += ("des,type_paragwghs,");
                        Sql_Values += ("N'" + myList.get(i).getProductName() + "'," + ProductType + ",");
                        Sql_Header += ("order_printer,order_enb,");
                        Sql_Values += ("N'" + myList.get(i).getOrder_printer() + "'," +  myList.get(i).getOrder_enb() + ",");
                        Sql_Header += ("header_guid");
                        Sql_Values += ("'" + myList.get(i).getGuid() + "'");
                        Sql_Header += (")");
                        Sql_Values += (");");
                        Log.i(TAG + "Sql_Header", Sql_Header);
                        Log.i(TAG + "Sql_Values", Sql_Values);
                        sql_str += Sql_Header + Sql_Values;
                        Sql_Header = null;
                        Sql_Values = null;
                    }


                    Sql_Header += ("des,fromm,MayNeedExtra,");
                    if(!myList.get(i).isMayNeedExtra()) {
                        Sql_Values += ("N'" + myList.get(i).getProductName() + "',N'" +  myList.get(i).getProductCategory() + "',0,");
                    } else {
                        Sql_Values += ("N'" + myList.get(i).getProductName() + "',N'" + myList.get(i).getProductCategory() + "'," + myList.get(i).isMayNeedExtra() + ",");
                    }
//                If IsDBNull(.Item("mayNeedExtra")) Then
//                Sql_Values.Append("N'" & .Item("des").ToString & "',N'" & .Item("fromm") & "',0,")
//                Else
//                Sql_Values.Append("N'" & .Item("des").ToString & "',N'" & .Item("fromm") & "'," & .Item("mayNeedExtra") & ",")
//                End If


                    Sql_Header += ("bonus_active,bonus_quantity,bonus_type,");
                    Sql_Header += myList.get(i).getBonusActive()+"" + ",";
                    Sql_Values2 = Sql_Values.replace(myList.get(i).getBonus_quantity()+"", ".");
                    //todo fix replace
                    //& "," & .Item("bonus_type") & ",")
                    Sql_Values2 += "," + myList.get(i).getBonusType()+"" + ",";
                    Sql_Values = Sql_Values2;
                    Sql_Values2 = null;
                    Sql_Header += ("category_id, discount_active, discount_precent, disc_gen_active, disc_gen_precent,");
                    Sql_Values += (myList.get(i).getCatId()+"" + "," + myList.get(i).getDiscountActive() + "," + 0 + "," + myList.get(i).getDiscGenActive() + "," + 0 + ",");

                    Sql_Header += ("isCompl,Guid,");

                    Sql_Values += ( myList.get(i).getIsCompl() + ",'" + myList.get(i).getGuid() + "',");

                    Sql_Header += ("type_paragwghs,shopType,");
                    Sql_Values += (ProductType + "," + myList.get(i).getShopType() + ",");
                    Sql_Header += ("price,");

                    //Sql_Values.Append(Replace(.Item("price"), ",", ".") & ",")

                    Sql_Header += ("order_printer,order_enb,");
                    Sql_Values += ("N'" + myList.get(i).getOrder_printer() + "" + "'," + myList.get(i).getOrder_enb() + "" + ",");

                    Sql_Header += ("header_guid");
                    String  HdGuid = UUID.randomUUID().toString();
                    Sql_Values += ("N'" + HdGuid + "'");
                    //'to executed den to peirazeis apo edw
                    //'to state alazei apo to kentriko
                    Sql_Header += (")");
                    Sql_Values += (");");
//
                    sql_str += Sql_Header + Sql_Values;
//
//                Sql_Header.Clear()
//                Sql_Values.Clear()

                    //'INSERT TO PDA_ORDER HEADER
                    Sql_Header += ("INSERT INTO PDA_Order_Header (");
                    Sql_Values += (" Values(");

                    Sql_Header += ("order_date,total_products,");
                    Sql_Values += ("'" + DtSave + "'," + CountTotProducts + ",");

                    Sql_Header += ("to_shopid,");
                    Sql_Values += (myList.get(i).getShopType() + ",");

                    Sql_Header += ("rsguid,cust_id,");
                    Sql_Values += ("'" + HdGuid + "'," + 0 + ",");

                    Sql_Header += ("user_id,");
                    //getting user_id from shared preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
                    int user_id = preferences.getInt("user_id",0);
                    Sql_Values += (user_id + ",");
                    //user

                    Sql_Header += ("tbl_id,");
                    //Sql_Values += (TransfVariablesFormoForm.tblId + ",");
                    Sql_Values += (TableId + ",");

                    Sql_Header += ("is_closed");
                    Sql_Values += (Constant.enmPdaOrderState.ORDER_justARRIVED);

                    Sql_Header += (")");
                    Sql_Values += (");");
                    sql_str += Sql_Header + Sql_Values;
//
//                Sql_Header.Clear()
//                Sql_Values.Clear()
                }//end foreach product
                myList.clear();
                Log.i("cart table content", "deleted");
                myDatabase.close();
            }
        });

        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("to products", "clicked");
                onBackPressed();
//                myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
//                for (int i = 0; i < adapter.getCount(); i++) {
//                    adapter.getItem(i);
//                    myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + adapter.getItem(i).getProductName() + "', '" + adapter.getItem(i).getPrice() + "');");
//                    Log.i("updated db with", adapter.getItem(i).getProductName());
//                }
            }
        });
    }

    public int calculateMealTotal(){
        int mealTotal = 0;
        for(Product order : orders){
            mealTotal += order.getPrice() * order.getmQuantity();
        }
        return mealTotal;
    }

    DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.i(TAG,"setMealTotal");
            setMealTotal();
        }
    };

    @Override
    public void onBackPressed() {
        //create database
        final SQLiteDatabase myDatabase = openOrCreateDatabase("myDatabase", MODE_PRIVATE, null);
        Log.i("to products", "clicked");
        myDatabase.execSQL("DELETE FROM Cart WHERE id=1;");
        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getItem(i);
            myDatabase.execSQL("INSERT INTO Cart (id,products,cost) VALUES ( 1,'" + adapter.getItem(i).getProductName() + "', '" + adapter.getItem(i).getPrice() + "');");
            Log.i("updated db with", adapter.getItem(i).getProductName());
        }
        super.onBackPressed();
    }

    private ArrayList<Product> getListItemData(){
        ArrayList<Product> listViewItems = new ArrayList<>();
        listViewItems.add(new Product());
        return listViewItems;
    }

    public void setMealTotal(){
        mealTotalText.setText(calculateMealTotal() + " " +"€");
    }
}