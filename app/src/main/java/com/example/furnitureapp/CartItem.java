package com.example.furnitureapp;

public class CartItem {
    private String name;
    private String image;
    private double price;
    private int id;
    private int quantity;

    public CartItem(String name, String image, double price, int id, int quantity) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.id = id;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
