package com.steam.app.pdaOrder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.steam.app.pdaOrder.Model.TableItem;
import com.steam.app.pdaOrder.R;
import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends ArrayAdapter<TableItem> {

    private Context mContext;
    private List<TableItem> tableItems;

    public TableAdapter(@NonNull Context context, ArrayList<TableItem> list) {
        super(context, 0 , list);
        mContext = context;
        tableItems = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        TableItem product = tableItems.get(position);

        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(product.getTableName());


        return listItem;
    }
}
