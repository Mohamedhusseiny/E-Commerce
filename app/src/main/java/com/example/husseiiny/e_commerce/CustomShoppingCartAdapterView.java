package com.example.husseiiny.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class CustomShoppingCartAdapterView extends ArrayAdapter<ShoppingCart> {

    public CustomShoppingCartAdapterView(Context context) {
        super(context, 0, ShoppingCart.shoppingCartList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_shopping_cart_item, parent, false);
        }
        ShoppingCart item = getItem(position);


        TextView textViewName = convertView.findViewById(R.id.text_name_summary);
        TextView textViewPrice = convertView.findViewById(R.id.text_price_summary);
        TextView textViewQuantity = convertView.findViewById(R.id.text_quantity_summary);

        textViewName.setText("Name " + item.getProductName());
        textViewPrice.setText("Price $" + item.getTotalPrice());
        textViewQuantity.setText("Ordered Quantity " + item.getQuantity());

        return convertView;
    }
}
