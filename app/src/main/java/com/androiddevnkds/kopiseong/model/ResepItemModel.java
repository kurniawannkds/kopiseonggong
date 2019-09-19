package com.androiddevnkds.kopiseong.model;

public class ResepItemModel {

    private String itemName;
    private String itemJumlah;

    public ResepItemModel() {
    }

    public ResepItemModel(String itemName, String itemJumlah) {
        this.itemName = itemName;
        this.itemJumlah = itemJumlah;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemJumlah() {
        return itemJumlah;
    }

    public void setItemJumlah(String itemJumlah) {
        this.itemJumlah = itemJumlah;
    }
}
