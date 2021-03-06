package com.steam.app.pdaOrder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.R;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private List<Product> products;

    public ProductAdapter(@NonNull Context context, ArrayList<Product> list) {
        super(context, 0 , list);
        mContext = context;
        products = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.product_item,parent,false);

        Product product = products.get(position);

        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(product.getProductName());
        TextView price = listItem.findViewById(R.id.textView_price);
        price.setText(product.getPrice()+"");

        if (position % 2 == 1) {
            listItem.setBackgroundColor(Color.parseColor("#B8B8B8"));
        } else {
            listItem.setBackgroundColor(Color.parseColor("#cdcdcd"));
        }


        return listItem;
    }
}