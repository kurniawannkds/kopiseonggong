package com.androiddevnkds.kopiseong.model;

public class ListModel {

    private int image;
    private String title;
    private String price;

    public ListModel(int image, String title, String price) {
        this.image = image;
        this.title = title;
        this.price = price;
    }

    public ListModel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
