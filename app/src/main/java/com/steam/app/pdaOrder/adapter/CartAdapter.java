package com.steam.app.pdaOrder.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.steam.app.pdaOrder.CartActivity;
import com.steam.app.pdaOrder.Model.Order;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.R;
import java.util.ArrayList;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class CartAdapter extends ArrayAdapter<Product> implements View.OnClickListener{

    private ArrayList<Product> dataSet;
    Context mContext;
    Order myOrder;
    PopupWindow popupWindow;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        ImageButton remove;
        ImageButton edit;
    }

    public CartAdapter(ArrayList<Product> data, Context context) {
        super(context, R.layout.list_row, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position =(Integer) v.getTag();
        Product product = getItem(position);
        myOrder.setOderProducts(dataSet);
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.from_name);
            viewHolder.txtPrice = convertView.findViewById(R.id.plist_price_text);
            viewHolder.remove = convertView.findViewById(R.id.delete_item);
            viewHolder.edit = convertView.findViewById(R.id.edit_item);

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("remove ", "clicked");
                    Toast.makeText(parent.getContext(), "Πατήστε ανανέωση!", Toast.LENGTH_LONG).show();
                    dataSet.remove(getItem(position));
                        int listSize = dataSet.size();
                    for (int i = 0; i<listSize; i++){
                        Log.i("Product in cart: ", dataSet.get(i).getProductName());
                        Log.i("Product position", i+"");
                    }
                }
            });

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("edit ", "clicked");
                    String name = dataSet.get(position).getProductName();
                    Log.i("edit ", name);

                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View popupView = inflater.inflate(R.layout.popup_window, null);
                    TextView title = popupView.findViewById(R.id.popup_title);
                    title.setText(name);
                    final EditText comment = popupView.findViewById(R.id.comment);
                    EditText quantity = popupView.findViewById(R.id.qty_num);
                    Button submit = popupView.findViewById(R.id.submit_comment);


                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    submit.setOnClickListener(
                            new View.OnClickListener()
                            {
                                public void onClick(View view)
                                {
                                    Log.i("EditText", comment.getText().toString());
                                    dataSet.get(position).setComment(comment.getText().toString());
                                    popupWindow.dismiss();
                                }
                            });

                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
            });

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        try {
        viewHolder.txtName.setText(product.getProductName());
        viewHolder.txtPrice.setText(product.getPrice()+"");
        } catch (Exception e) {
            Log.e("your app", e.toString());
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
