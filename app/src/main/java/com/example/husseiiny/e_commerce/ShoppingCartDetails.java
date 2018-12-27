package com.example.husseiiny.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ShoppingCartDetails extends AppCompatActivity {

    private CustomShoppingCartAdapterView customShoppingCartAdapterView;
    private ListView listViewSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_details);

        listViewSummary = findViewById(R.id.listview_summary);
        customShoppingCartAdapterView = new CustomShoppingCartAdapterView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        listViewSummary.setAdapter(customShoppingCartAdapterView);
    }
}
