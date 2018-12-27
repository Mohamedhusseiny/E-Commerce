package com.example.husseiiny.e_commerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.husseiiny.e_commerce.Database.CommerceContract.*;
import com.example.husseiiny.e_commerce.Product;
import com.example.husseiiny.e_commerce.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class CommerceDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "e-commerce";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;

    public CommerceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + CustomerEntry.TABLE_NAME + "(" +
                CustomerEntry.CUST_ID + " INTEGER primary key autoincrement, " +
                CustomerEntry.CUST_NAME + " TEXT NOT NULL, " +
                CustomerEntry.CUST_USERNAME + " TEXT NOT NULL, " +
                CustomerEntry.CUST_PASSWORD + " TEXT NOT NULL, " +
                CustomerEntry.CUST_JOB + " TEXT, " +
                CustomerEntry.CUST_GENDER + " TEXT, " +
                CustomerEntry.CUST_BIRTHDAY + " DATE);"
        );

        db.execSQL("CREATE TABLE " + CategoriesEntry.TABLE_NAME + "(" +
                CategoriesEntry.CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesEntry.CAT_NAME + " TEXT NOT NULL);"
        );

        db.execSQL("CREATE TABLE " + ProductEntry.TABLE_NAME + "(" +
                ProductEntry.PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductEntry.PRODUCT_IMAGE + " INTEGER, " +
                ProductEntry.PRODUCT_NAME + " TEXT NOT NULL, " +
                ProductEntry.PRODUCT_PRICE + " INTEGER NOT NULL, " +
                ProductEntry.PRODUCT_QUANTITY + " INTEGER, " +
                ProductEntry.PRODUCT_BARCODE + " TEXT, " +
                ProductEntry.CAT_ID + " INTEGER, " +
                "FOREIGN KEY(" + ProductEntry.CAT_ID + ") " +
                "REFERENCES " + CategoriesEntry.TABLE_NAME + "(" + CategoriesEntry.CAT_ID + "));"
        );

        db.execSQL("CREATE TABLE " + OrderEntry.TABLE_NAME + "(" +
                OrderEntry.ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrderEntry.ORDER_ADDRESS + " TEXT NOT NULL, " +
                OrderEntry.ORDER_DATE + " DATE, " +
                OrderEntry.CUST_ID + " INTEGER, " +
                "FOREIGN KEY(" + OrderEntry.CUST_ID + ") " +
                "REFERENCES " + CustomerEntry.TABLE_NAME + "(" + CustomerEntry.CUST_ID + "));"
        );

        // TODO: TEST
        db.execSQL("CREATE TABLE " + OrderDetailsEntry.TABLE_NAME + "(" +
                OrderDetailsEntry.ORD_ID + " INTEGER, " +
                OrderDetailsEntry.PRO_ID + " INTEGER, " +
                OrderDetailsEntry.ORDER_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + OrderDetailsEntry.ORD_ID + ") " +
                "REFERENCES " + OrderEntry.TABLE_NAME + "(" + OrderEntry.ORDER_ID + "), " +
                "FOREIGN KEY(" + OrderDetailsEntry.PRO_ID + ") " +
                "REFERENCES " + ProductEntry.TABLE_NAME + "(" + ProductEntry.PRODUCT_ID + "));"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CustomerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderDetailsEntry.TABLE_NAME);
        onCreate(db);
    }

    //=====================================CUSTOMER METHODS=========================================
    public boolean checkCredentials(String email, String password) {
        database = getReadableDatabase();
        Cursor cursor = database.query(CustomerEntry.TABLE_NAME, new String[]{CustomerEntry.CUST_PASSWORD},
                CustomerEntry.CUST_USERNAME + "=?", new String[]{email},
                null, null, null);
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex(CustomerEntry.CUST_PASSWORD)).equals(password)) {
                return true;
            }
        }
        return false;
    }


    public boolean checkCustomerUsername(String username) {
        database = getReadableDatabase();
        Cursor cursor = database.query(CustomerEntry.TABLE_NAME, new String[]{CustomerEntry.CUST_ID},
                CustomerEntry.CUST_USERNAME + "=?", new String[]{username}, null, null, null);
        return cursor.moveToFirst();
    }

    public void createNewUser(String name, String email, String password, String gender, String birthday, String job) {

        ContentValues values = new ContentValues();
        values.put(CustomerEntry.CUST_NAME, name);
        values.put(CustomerEntry.CUST_USERNAME, email);
        values.put(CustomerEntry.CUST_PASSWORD, password);
        values.put(CustomerEntry.CUST_GENDER, gender);
        values.put(CustomerEntry.CUST_BIRTHDAY, birthday);
        values.put(CustomerEntry.CUST_JOB, job);

        database = getWritableDatabase();
        database.insert(CustomerEntry.TABLE_NAME, null, values);
    }

    public void updatePassword(String username, String newPassword) {
        ContentValues values = new ContentValues();
        values.put(CustomerEntry.CUST_PASSWORD, newPassword);
        database = getWritableDatabase();
        database.update(CustomerEntry.TABLE_NAME, values, CustomerEntry.CUST_USERNAME + "=?", new String[]{username});
    }

    public int getCustomerId(String username){
        database = getReadableDatabase();
        Cursor cursor = database.query(CustomerEntry.TABLE_NAME, new String[]{CustomerEntry.CUST_ID},
                CustomerEntry.CUST_USERNAME + "=?", new String[]{username},
                null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(CustomerEntry.CUST_ID));
        }
        return -1;
    }

    //======================================CATEGORY METHODS========================================
    public void addCategories(String[] categories){
        database = getWritableDatabase();
        database.delete(CategoriesEntry.TABLE_NAME, null, null);
        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + CategoriesEntry.TABLE_NAME + "'");

            for (int i = 0; i<categories.length; i++) {
                ContentValues values = new ContentValues();
                values.put(CategoriesEntry.CAT_NAME, categories[i]);
                database.insert(CategoriesEntry.TABLE_NAME, null, values);
            }

    }

    public String[] fetchCategories(){

        database = getReadableDatabase();
        Cursor cursor = database.query(CategoriesEntry.TABLE_NAME, new String[]{CategoriesEntry.CAT_NAME},
                null, null, null, null, null);
        cursor.moveToFirst();
        String[] categories = new String[cursor.getCount()];
        for(int i = 0; i< cursor.getCount(); i++){
            categories[i] = cursor.getString(cursor.getColumnIndex(CategoriesEntry.CAT_NAME));
            cursor.moveToPosition(i+1);
        }
        return categories;
    }

    public int getCategoryID(String productName) {
        database = getReadableDatabase();
        Cursor cursor = database.query(CategoriesEntry.TABLE_NAME, new String[]{CategoriesEntry.CAT_ID},
                CategoriesEntry.CAT_NAME + "=?", new String[]{productName}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(CategoriesEntry.CAT_ID));
        }
        return -1;
    }


    //=======================================PRODUCTS METHODS=======================================
    public void clearProducts(){
        database = getWritableDatabase();
        database.delete(ProductEntry.TABLE_NAME, null, null);
        database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + ProductEntry.TABLE_NAME + "'");
    }

    public void addProduct(int imageId, String name, int price, int quantity, int categoryId, String barcode) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.PRODUCT_IMAGE, imageId);
        values.put(ProductEntry.PRODUCT_NAME, name);
        values.put(ProductEntry.PRODUCT_PRICE, price);
        values.put(ProductEntry.PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.PRODUCT_BARCODE, barcode);
        values.put(ProductEntry.CAT_ID, categoryId);

        database = getWritableDatabase();
        database.insert(ProductEntry.TABLE_NAME, null, values);
    }


    // Used when navigating products through fragment
    public List<Product> fetchProducts(int id){
        List<Product> list = new ArrayList<>();
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]
                {ProductEntry.PRODUCT_IMAGE, ProductEntry.PRODUCT_NAME, ProductEntry.PRODUCT_PRICE,
                ProductEntry.PRODUCT_QUANTITY}, ProductEntry.CAT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Product(cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(ProductEntry.PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_QUANTITY))
                ));
                cursor.moveToNext();
            }
        }
        return list;
    }

//    // Used when user searching for specific products
    public List<Product> getProductsBySearch(String productName) {
        List<Product> list = new ArrayList<>();
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]
                        {ProductEntry.PRODUCT_IMAGE, ProductEntry.PRODUCT_NAME, ProductEntry.PRODUCT_PRICE,
                                ProductEntry.PRODUCT_QUANTITY}, ProductEntry.PRODUCT_NAME + " LIKE '%" + productName + "%'",
                null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(new Product(cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(ProductEntry.PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_QUANTITY))
                ));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void updateProducts() {
        database = getWritableDatabase();
        for(ShoppingCart item: ShoppingCart.shoppingCartList){
            ContentValues values = new ContentValues();
            values.put(ProductEntry.PRODUCT_QUANTITY, getProductQuantity(item.getProductName()) - item.getQuantity());
            database.update(ProductEntry.TABLE_NAME, values, ProductEntry.PRODUCT_NAME + "=?",
                    new String[]{item.getProductName()});
        }
    }

    private int getProductId(String productName) {
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]{ProductEntry.PRODUCT_ID},
                ProductEntry.PRODUCT_NAME + "=?", new String[]{productName}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_ID));
        }
        return -1;
    }

    public int getProductPrice(String productName) {
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]{ProductEntry.PRODUCT_PRICE},
                ProductEntry.PRODUCT_NAME + "=?", new String[]{productName}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_PRICE));
        }
        return -1;
    }

    public int getProductQuantity(String productName) {
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]{ProductEntry.PRODUCT_QUANTITY},
                ProductEntry.PRODUCT_NAME + "=?", new String[]{productName}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_QUANTITY));
        }
        return -1;
    }

    //=====================================ORDER METHODS============================================

    public long makeOrder(String date, String clientLocation) {
        ContentValues values = new ContentValues();
        values.put(OrderEntry.ORDER_DATE, date);
        values.put(OrderEntry.ORDER_ADDRESS, clientLocation);
        values.put(OrderEntry.CUST_ID, getCustomerId(ShoppingCart.getCustomer()));

        database = getWritableDatabase();
        long id = database.insert(OrderEntry.TABLE_NAME, null, values);
        return id;
    }


    public void setOrderDetail(long orderId) {
        for(ShoppingCart item: ShoppingCart.shoppingCartList){
            ContentValues values = new ContentValues();
            values.put(OrderDetailsEntry.ORD_ID, orderId);
            values.put(OrderDetailsEntry.PRO_ID, getProductId(item.getProductName()));
            values.put(OrderDetailsEntry.ORDER_QUANTITY, item.getQuantity());

            database = getWritableDatabase();
            database.insert(OrderDetailsEntry.TABLE_NAME, null, values);
        }
    }


    public Product getProductsByBarcode(String scanCode) {
        database = getReadableDatabase();
        Cursor cursor = database.query(ProductEntry.TABLE_NAME, new String[]
                        {ProductEntry.PRODUCT_IMAGE, ProductEntry.PRODUCT_NAME, ProductEntry.PRODUCT_PRICE,
                                ProductEntry.PRODUCT_QUANTITY}, ProductEntry.PRODUCT_BARCODE + " LIKE '%" + scanCode + "%'",
                null, null, null, null);
        if(!cursor.moveToFirst()) return null;
        cursor.moveToFirst();
        return new Product(cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_IMAGE)),
                cursor.getString(cursor.getColumnIndex(ProductEntry.PRODUCT_NAME)),
                cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_PRICE)),
                cursor.getInt(cursor.getColumnIndex(ProductEntry.PRODUCT_QUANTITY)));
    }
}