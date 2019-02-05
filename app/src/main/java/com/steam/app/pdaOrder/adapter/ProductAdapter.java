package com.steam.app.pdaOrder.adapter;

import android.content.Context;
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
    private List<Product> products = new ArrayList<>();

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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Product product = products.get(position);

        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(product.getProductName());

        return listItem;
    }
}