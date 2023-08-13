package com.example.furnitureapp;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String image;
    private double price;
    private int id;

    public Product(String name, String image, double price, int id) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
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
}
