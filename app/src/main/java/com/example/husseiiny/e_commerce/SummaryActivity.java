package com.example.husseiiny.e_commerce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.husseiiny.e_commerce.Database.CommerceDBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private CommerceDBHelper mDbHelper;
    private FusedLocationProviderClient mFusedLocationClient;

    private TextView textViewShoppingCart;
    private TextView textViewOrderDate;
    private TextView textViewAddress;
    private TextView textViewTotalSummary;
    private TextView textViewDetermineLocation;
    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        textViewShoppingCart = findViewById(R.id.textview_shopping_cart);
        textViewAddress = findViewById(R.id.text_address);
        textViewOrderDate = findViewById(R.id.text_date);
        textViewTotalSummary = findViewById(R.id.textview_total_summary);
        textViewDetermineLocation = findViewById(R.id.textview_determine_location);
        buttonConfirm = findViewById(R.id.button_confirm);


        mDbHelper = new CommerceDBHelper(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textViewOrderDate.setText(getDate());
        textViewTotalSummary.setText(ShoppingCart.getTotalSummaryPrice());

        //========================================GET LOCATION======================================
        getLocation();
        textViewDetermineLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDetermineLocation.setVisibility(View.GONE);
                getLocation();
            }
        });

        // View shopping cart items
        textViewShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shoppingCartIntent = new Intent(SummaryActivity.this, ShoppingCartDetails.class);
                startActivity(shoppingCartIntent);
            }
        });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make Order
                long orderId = mDbHelper.makeOrder(getDate(), textViewAddress.getText().toString());
                // Inserting order detail
                mDbHelper.setOrderDetail(orderId);
                // Updating products
                mDbHelper.updateProducts();
                // Clear the shopping cart list and back to navigation activity
                ShoppingCart.shoppingCartList.clear();
                Toast.makeText(SummaryActivity.this, "Operation is confirmed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SummaryActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder geocoder = new Geocoder(SummaryActivity.this);
                        List<Address> addresses;
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                textViewAddress.setText(addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                textViewDetermineLocation.setVisibility(View.VISIBLE);
                                Toast.makeText(SummaryActivity.this, "Error occur while fetching your location!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            textViewDetermineLocation.setVisibility(View.VISIBLE);
                            Toast.makeText(SummaryActivity.this, "Enable GPS or check internet connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getDate() {
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        return curFormater.format(Calendar.getInstance().getTime());
    }


}
