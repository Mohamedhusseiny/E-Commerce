package com.example.husseiiny.e_commerce;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private String productName;
    private int quantity;
    private int totalPrice;
    private static String customer;

    public static List<ShoppingCart> shoppingCartList = new ArrayList<>();

    public ShoppingCart(String productName, int quantity, int totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static void setCustomer(String username) {
        customer = username;
    }

    public static String getCustomer(){ return customer; }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public int getTotalPrice() {
        return totalPrice;
    }

    public static int getAddedQuantity(String productName) {
        for(ShoppingCart item: shoppingCartList){
            if(item.productName.equals(productName)) return item.getQuantity();
        }
        return -1;
    }

    public static int getAddedPrice(String productName) {
        for(ShoppingCart item: shoppingCartList){
            if(item.productName.equals(productName)) return item.getTotalPrice();
        }
        return -1;
    }

    public static boolean containItem(String productName) {
        for(ShoppingCart item: shoppingCartList){
            if(item.productName.equals(productName)) return true;
        }
        return false;
    }

    public static void updateItem(ShoppingCart shoppingCart) {
        for(int i = 0; i < shoppingCartList.size(); i ++){
            if(shoppingCartList.get(i).productName.equals(shoppingCart.productName)){
                shoppingCartList.get(i).quantity = shoppingCart.quantity;
                shoppingCartList.get(i).totalPrice = shoppingCart.totalPrice;
            }
        }
    }

    public static void removeItem(String productName){
        for(int i = 0; i < shoppingCartList.size(); i++){
            if(shoppingCartList.get(i).productName.equals(productName)){
                shoppingCartList.remove(i);
            }
        }
    }

    public static String getTotalSummaryPrice() {
        int total = 0;
        for(ShoppingCart item : shoppingCartList){
            total += item.getTotalPrice();
        }
        return String.valueOf(total);
    }
}
