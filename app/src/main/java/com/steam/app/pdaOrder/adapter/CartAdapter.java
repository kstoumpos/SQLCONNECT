package com.steam.app.pdaOrder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.R;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<Product> implements View.OnClickListener{

    private ArrayList<Product> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        ImageButton remove;
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

    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        ImageButton remove;



        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.from_name);
            viewHolder.txtPrice = convertView.findViewById(R.id.plist_price_text);
            viewHolder.remove = convertView.findViewById(R.id.chk_selectitem);

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("remove ", "clicked");

                        dataSet.remove(getItem(position));
                        //dataSet.notifyAll();
                        int listSize = dataSet.size();
                    for (int i = 0; i<listSize; i++){
                        Log.i("Product in cart: ", dataSet.get(i).getProductName());
                    }
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

//        viewHolder.txtType.setText(dataModel.getType());
//        viewHolder.txtVersion.setText(dataModel.getVersion_number());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
