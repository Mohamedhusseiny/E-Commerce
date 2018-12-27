package com.example.husseiiny.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomProductAdapterView extends ArrayAdapter<Product> {
    public CustomProductAdapterView(Context context, List<Product> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Product product = getItem(position);

        ImageView imageViewProduct = convertView.findViewById(R.id.image_product);
        TextView textViewName = convertView.findViewById(R.id.text_product_name);
        TextView textViewPrice = convertView.findViewById(R.id.text_product_price);
        TextView textViewQuantity = convertView.findViewById(R.id.text_product_quantity);

        imageViewProduct.setImageResource(product.getImageId());
        textViewName.setText("Name " + product.getName());
        textViewPrice.setText("Price $" + product.getPrice());
        textViewQuantity.setText("Available Quantity " + product.getQuantity());

        return convertView;
    }
}
