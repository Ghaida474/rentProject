package com.example.rentmyspot;

import android.graphics.BitmapFactory;

import java.util.Arrays;
//
public class Seating {
    private String username;
    private String sName;
    private String sCategory;
    private int sPrice;
    private String sDescription;
    private byte[] imageData;
    private boolean rented;

    public Seating(String username, String sName, String sCategory, int sPrice, String sDescription, byte[] imageData, boolean rented) {
        this.username = username;
        this.sName = sName;
        this.sCategory = sCategory;
        this.sPrice = sPrice;
        this.sDescription = sDescription;
        this.imageData = imageData;
        this.rented = rented;
    }

    public Seating() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSCategory() {
        return sCategory;
    }

    public void setSCategory(String sCategory) {
        this.sCategory = sCategory;
    }

    public int getSPrice() {
        return sPrice;
    }

    public void setSPrice(int sPrice) {
        this.sPrice = sPrice;
    }

    public String getSDescription() {
        return sDescription;
    }

    public void setSDescription(String sDescription) {
        this.sDescription = sDescription;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    @Override
    public String toString() {
        return "Seating Information\n"
                + "User Name: " + username
                + "\nSeating Name: " + sName
                + "\nSeating Category: " + sCategory
                + "\nSeating Price: " + sPrice
                + "\nSeating Description: " + sDescription;
    }
}