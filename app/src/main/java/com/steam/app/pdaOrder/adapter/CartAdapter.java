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
import com.steam.app.pdaOrder.Model.Order;
import com.steam.app.pdaOrder.Model.Product;
import com.steam.app.pdaOrder.R;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class CartAdapter extends ArrayAdapter<Product> implements View.OnClickListener{

    private ArrayList<Product> dataSet;
    private List<Product> list;
    Context context;
    Order myOrder;
    private TextView currentCost;
    private TextView quantityText;
    private TextView addMeal;
    private TextView subtractMeal;
    private TextView removeMeal;

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
        this.list = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        int position =(Integer) v.getTag();
        Product product = getItem(position);
        myOrder.setOderProducts(dataSet);
    }

    private int lastPosition = -1;

//    @Override
//    public View getView(final int position, View convertView, final ViewGroup parent) {
//
//        // Get the data item for this position
//        Product product = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        final ViewHolder viewHolder; // view lookup cache stored in tag
//
//        final View result;
//
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.list_row, parent, false);
//            viewHolder.txtName = convertView.findViewById(R.id.selected_food_name);
//            viewHolder.txtPrice = convertView.findViewById(R.id.selected_food_amount);
//            viewHolder.remove = convertView.findViewById(R.id.delete_item);
//
//            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Log.i("remove ", "clicked");
//                    Toast.makeText(parent.getContext(), "Πατήστε ανανέωση!", Toast.LENGTH_LONG).show();
//                    dataSet.remove(getItem(position));
//                        int listSize = dataSet.size();
//                    for (int i = 0; i<listSize; i++){
//                        Log.i("Product in cart: ", dataSet.get(i).getProductName());
//                        Log.i("Product position", i+"");
//                    }
//                }
//            });
//
//            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    Log.i("edit ", "clicked");
//                    String name = dataSet.get(position).getProductName();
//                    Log.i("edit ", name);
//
//                    // inflate the layout of the popup window
//                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//                    final View popupView = inflater.inflate(R.layout.popup_window, null);
//                    TextView title = popupView.findViewById(R.id.popup_title);
//                    title.setText(name);
//                    final EditText comment = popupView.findViewById(R.id.comment);
//                    Button submit = popupView.findViewById(R.id.submit_comment);
//
//
//                    // create the popup window
//                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    boolean focusable = true; // lets taps outside the popup also dismiss it
//                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//                    // show the popup window
//                    // which view you pass in doesn't matter, it is only used for the window token
//                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//
//                    submit.setOnClickListener(
//                            new View.OnClickListener()
//                            {
//                                public void onClick(View view)
//                                {
//                                    Log.i("EditText", comment.getText().toString());
//                                    dataSet.get(position).setComment(comment.getText().toString());
//                                    popupWindow.dismiss();
//                                }
//                            });
//
//                    // dismiss the popup window when touched
//                    popupView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            popupWindow.dismiss();
//                            return true;
//                        }
//                    });
//                }
//            });
//
//            result = convertView;
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
//        }
//
//        lastPosition = position;
//
//        try {
//        viewHolder.txtName.setText(product.getProductName());
//        viewHolder.txtPrice.setText(product.getPrice()+"");
//        } catch (Exception e) {
//            Log.e("your app", e.toString());
//        }
//        // Return the completed view to render on screen
//        return convertView;
//    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_row,parent,false
            );
        }

        final Product currentProduct = getItem(position);

        TextView currentFoodName = listItemView.findViewById(R.id.selected_food_name);
        currentCost = listItemView.findViewById(R.id.selected_food_amount);
        subtractMeal = listItemView.findViewById(R.id.minus_meal);
        quantityText = listItemView.findViewById(R.id.quantity);
        addMeal = listItemView.findViewById(R.id.plus_meal);
        removeMeal = listItemView.findViewById(R.id.delete_item);

        //Set the text of the meal, amount and quantity
        currentFoodName.setText(((currentProduct).getProductName()));
        double Price = currentProduct.getPrice();
        currentCost.setText((currentProduct.getPrice() * currentProduct.getmQuantity() + " " +"€"));
        int Qty = currentProduct.getmQuantity();
        if(Qty == 0){
            currentProduct.setmQuantity(1);
        }
        Log.i(currentProduct.getProductName()+": mAmount Quantity",   " " + Price + " " + currentProduct.getmQuantity());
        quantityText.setText("x "+ currentProduct.getmQuantity());

        //OnClick listeners for all the buttons on the ListView Item
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct.addToQuantity();
                quantityText.setText("x "+ currentProduct.getmQuantity());
                currentCost.setText((currentProduct.getPrice() * currentProduct.getmQuantity())+ " " +"€");
                notifyDataSetChanged();
            }
        });

        subtractMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentProduct.removeFromQuantity();
                quantityText.setText("x "+currentProduct.getmQuantity());
                currentCost.setText((currentProduct.getPrice() * currentProduct.getmQuantity())+ " " +"€");
                notifyDataSetChanged();
            }
        });

        removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }
}
