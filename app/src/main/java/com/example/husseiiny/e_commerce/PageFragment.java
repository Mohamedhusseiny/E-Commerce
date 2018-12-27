package com.example.husseiiny.e_commerce;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.husseiiny.e_commerce.Database.CommerceDBHelper;

import java.util.ArrayList;
import java.util.List;

public class PageFragment extends Fragment {
    public static final int VOICE_CODE = 1;
    public static final String ARG_PAGE = "ARG_PAGE";

    public static String scanCode;
    public static boolean isScanned = false;

    private int mPage;

    private CommerceDBHelper mDbHelper;
    private List<Product> productList;
    private CustomProductAdapterView adapter;

    private String productName;
    private int quantity, totalPrice;

    private ListView listView;
    private EditText editTextSearch;
    private ImageView imageViewCancel, imageViewVoiceSearch, imageViewCameraSearch;

    private TextView textViewQuantity, textViewTotal;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        imageViewCancel  = getActivity().findViewById(R.id.image_cancel);
        editTextSearch  = getActivity().findViewById(R.id.edit_search);
        imageViewVoiceSearch  = getActivity().findViewById(R.id.image_voice_search);
        imageViewCameraSearch = getActivity().findViewById(R.id.image_camera_search);

        mDbHelper = new CommerceDBHelper(getContext());
//        mDbHelper.clearProducts();
//
//        int laptopCategoryId = mDbHelper.getCategoryID("Laptop");
//        int mobileCategoryId = mDbHelper.getCategoryID("Mobile");
//        int accessoryCategoryId = mDbHelper.getCategoryID("Accessory");
//
//         //Inserting Products of Laptops
//        mDbHelper.addProduct(R.drawable.laptop_apple, "Mac Book", 2299, 3, laptopCategoryId, "012000809941");
//        mDbHelper.addProduct(R.drawable.laptop_dell, "Dell 5300", 1735, 1, laptopCategoryId, null);
//        mDbHelper.addProduct(R.drawable.laptop_lenovo, "Lenovo V330", 887, 5, laptopCategoryId, null);
//
//        // Inserting Products of Mobiles
//        mDbHelper.addProduct(R.drawable.mobile_infinix, "Infinix x609", 490, 2, mobileCategoryId, "9501101530003");
//        mDbHelper.addProduct(R.drawable.mobile_iphone, "iphone X", 1250, 6, mobileCategoryId, null);
//        mDbHelper.addProduct(R.drawable.mobile_samsung, "Samsung Galaxy Note +9", 899, 8, mobileCategoryId, null);
//        mDbHelper.addProduct(R.drawable.mobile_sony, "Sony Z3", 649, 4, mobileCategoryId, null);
//
//        // Inserting Products of Headphones
//        mDbHelper.addProduct(R.drawable.accessories_headphone, "Mobile Headphone", 99, 15, accessoryCategoryId, null);
//        mDbHelper.addProduct(R.drawable.accessories_phonebearer, "Mobile Bearer in Car", 50, 9, accessoryCategoryId, null);
//        mDbHelper.addProduct(R.drawable.accessories_laptopcover, "Purple Laptop Cover", 199, 18, accessoryCategoryId, null);
//        mDbHelper.addProduct(R.drawable.accessories_headphone2, "Wireless Headphone", 299, 7, accessoryCategoryId, null);
//        mDbHelper.addProduct(R.drawable.laptop_bag, "Laptop Backpack", 15, 3, accessoryCategoryId, "1234567890128");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.list_products);
        FloatingActionButton floatingActionButtonShoppingCart =
                view.findViewById(R.id.button_shoping_cart);

        // Set content of the fragment
        productList = mDbHelper.fetchProducts(mPage);
        adapter = new CustomProductAdapterView(getContext(), productList);
        listView.setAdapter(adapter);

        //====================================SEARCH OPERATION======================================
        // The search operation by text
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageViewCancel.setVisibility(View.VISIBLE);
                adapter = new CustomProductAdapterView(getContext(),
                        mDbHelper.getProductsBySearch(s.toString()));
                listView.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        // The search operation by voice
        imageViewVoiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(voiceIntent, VOICE_CODE);
                imageViewCancel.setVisibility(View.VISIBLE);
            }
        });
        imageViewCameraSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(getContext(), ScanActivity.class);
                startActivity(cameraIntent);
            }
        });

        if(isScanned){
            if(scanCode == null){
                Toast.makeText(getContext(), "Cannot find this product by scanned barcode!", Toast.LENGTH_SHORT).show();
            }else {
                Product product = mDbHelper.getProductsByBarcode(scanCode);
                if(product != null) {
                    adapter.clear();
                    adapter.add(product);
                    listView.setAdapter(adapter);
                    imageViewCancel.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Cannot find this product by scanned barcode!", Toast.LENGTH_SHORT).show();
                }
            }
            isScanned = false;
        }

        //=============================RETRIEVE PRODUCTS AFTER SEARCHING============================
        // Set back products content after searching
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                imageViewCancel.setVisibility(View.INVISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), 0);
                productList = mDbHelper.fetchProducts(mPage);
                adapter = new CustomProductAdapterView(getContext(), productList);
                listView.setAdapter(adapter);
            }
        });

        //================================PROCEED TO SUMMARY========================================
        // When shopping cart have some products, user could go to summary activity through the floating action button
        floatingActionButtonShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShoppingCart.shoppingCartList != null && ShoppingCart.shoppingCartList.size() > 0){
                    Intent intent = new Intent(getContext(), SummaryActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),
                            "The shopping cart is empty!\nAdd products to review your summary.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //=============================CRUD OPERATIONS BY LISTVIEW==================================
        // Set click listener to the listview to add/ edit/ remove products
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                productName = productList.get(position).getName();

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                View dialogView = LayoutInflater.from(getContext()).inflate
                        (R.layout.dialog_shopping_cart, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setView(dialogView);
                alertDialog.show();

                TextView textViewDialogTitle = dialogView.findViewById(R.id.text_title);
                textViewQuantity = alertDialog.findViewById(R.id.text_quantity);
                textViewTotal = alertDialog.findViewById(R.id.textview_total_summary);
                Button buttonDecrease = alertDialog.findViewById(R.id.button_decrease);
                Button buttonIncrease = alertDialog.findViewById(R.id.button_increase);
                Button buttonAdd = alertDialog.findViewById(R.id.button_add_shopping_cart);
                Button buttonRemove = alertDialog.findViewById(R.id.button_remove_shopping_cart);

                // Check if the item is not in shopping cart
                if(!ShoppingCart.containItem(productName)) {
                    textViewDialogTitle.setText("Add Product " + productName + " to Shopping Cart");
                    // Decreasing quantity of item through the dialog
                    buttonDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decreaseQuantity();
                        }
                    });

                    // Increasing quantity of item through the dialog
                    buttonIncrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            increaseQuantity();
                        }
                    });
                    // Adding product to shopping cart
                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Integer.parseInt(textViewQuantity.getText().toString()) > 0) {
                                ShoppingCart.shoppingCartList
                                        .add(new ShoppingCart(productName, quantity, totalPrice));
                            } else{
                                Toast.makeText(getContext(),
                                        "Specify the quantity of the product",
                                        Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();
                        }
                    });
                } else if(ShoppingCart.containItem(productName)) {
                    buttonRemove.setVisibility(View.VISIBLE);
                    textViewDialogTitle.setText("Update Product " + productName + " from Shopping Cart");
                    buttonAdd.setText("Update");
                    textViewQuantity.setText(String.valueOf(ShoppingCart.getAddedQuantity(productName)));
                    textViewTotal.setText(String.valueOf(ShoppingCart.getAddedPrice(productName)));

                    buttonDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decreaseQuantity();
                        }
                    });
                    buttonIncrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            increaseQuantity();
                        }
                    });
                    // Update product from shopping cart
                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Integer.parseInt(textViewQuantity.getText().toString()) > 0) {
                                ShoppingCart.updateItem(new ShoppingCart(productName, quantity, totalPrice));
                            }else{
                                Toast.makeText(getContext(),
                                        "Specify the quantity of the product",
                                        Toast.LENGTH_SHORT).show();
                            }
                            alertDialog.dismiss();
                        }
                    });
                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShoppingCart.removeItem(productName);
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });

        return view;
    }

    //=======================================VOICE RESULT===========================================
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VOICE_CODE && resultCode == getActivity().RESULT_OK){
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(getContext(), "Output: " + text.get(0), Toast.LENGTH_SHORT).show();
            adapter = new CustomProductAdapterView(getContext(),
                    mDbHelper.getProductsBySearch(text.get(0)));
            listView.setAdapter(adapter);
        }
    }


    private void increaseQuantity() {
        quantity = Integer.parseInt(textViewQuantity.getText().toString());
        if (quantity < mDbHelper.getProductQuantity(productName)) {
            textViewQuantity.setText(String.valueOf(++quantity));
            totalPrice = Integer.parseInt(textViewTotal.getText().toString()) +
                    mDbHelper.getProductPrice(productName);
            textViewTotal.setText(String.valueOf(totalPrice));
    }
    }

    private void decreaseQuantity() {
        quantity = Integer.parseInt(textViewQuantity.getText().toString());
        if (quantity > 0) {
            textViewQuantity.setText(String.valueOf(--quantity));
            totalPrice = Integer.parseInt(textViewTotal.getText().toString()) -
                    mDbHelper.getProductPrice(productName);
            textViewTotal.setText(String.valueOf(totalPrice));
        }
    }
}
