package com.example.husseiiny.e_commerce.Database;

import android.provider.BaseColumns;

public class CommerceContract {

    public class CustomerEntry implements BaseColumns {

        public static final String TABLE_NAME = "customer";

        public static final String CUST_ID = BaseColumns._ID;
        public static final String CUST_NAME = "fname";
        public static final String CUST_USERNAME = "username";
        public static final String CUST_PASSWORD = "password";
        public static final String CUST_GENDER = "gender";
        public static final String CUST_BIRTHDAY = "birthdate";
        public static final String CUST_JOB = "job";


    }

    public class CategoriesEntry implements BaseColumns {

        public static final String TABLE_NAME = "category";

        public static final String CAT_ID = BaseColumns._ID;
        public static final String CAT_NAME = "name";
    }

    public class OrderEntry implements BaseColumns {

        public static final String TABLE_NAME = "orders";

        public static final String ORDER_ID = BaseColumns._ID;
        public static final String ORDER_DATE = "date";
        public static final String ORDER_ADDRESS = "address";
        public static final String CUST_ID = "cust_id";
    }

    public class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "product";

        public static final String PRODUCT_ID = BaseColumns._ID;
        public static final String PRODUCT_IMAGE = "image";
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_PRICE = "price";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String CAT_ID = "cat_id";
        public static final String PRODUCT_BARCODE = "barcode";
    }

    public class OrderDetailsEntry implements BaseColumns {

        public static final String TABLE_NAME = "details";

        public static final String ORD_ID = "ord_id";
        public static final String PRO_ID = "pro_id";
        public static final String ORDER_QUANTITY = "quantity";

    }
}
