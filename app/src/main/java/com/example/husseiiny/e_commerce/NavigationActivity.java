package com.example.husseiiny.e_commerce;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.husseiiny.e_commerce.Database.CommerceDBHelper;

public class NavigationActivity extends AppCompatActivity {
    public static final String LAPTOP_CATEGORY = "Laptop";
    public static final String MOBILE_CATEGORY = "Mobile";
    public static final String ACCESSORY_CATEGORY = "Accessory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        CommerceDBHelper mDbHelper = new CommerceDBHelper(this);
        mDbHelper.addCategories(new String[]{LAPTOP_CATEGORY, MOBILE_CATEGORY, ACCESSORY_CATEGORY});

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                this, mDbHelper.fetchCategories()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
