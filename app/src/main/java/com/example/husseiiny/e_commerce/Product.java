package com.example.husseiiny.e_commerce;

public class Product {

    private int imageId;
    private String name;
    private int price;
    private int quantity;

    public Product(int imageId, String name, int price, int quantity) {
        this.imageId = imageId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
