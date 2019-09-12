package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddDetailTransactionRequestModel {

    @SerializedName("detail_jumlah")
    private String[] detailJumlah;

    @SerializedName("detail_product")
    private String[] detailProduct;

    @SerializedName("transaction_id")
    private String transactionID;

    @SerializedName("size_model")
    private int sizeModel;

    public AddDetailTransactionRequestModel() {
    }

    public AddDetailTransactionRequestModel(String[] detailJumlah, String[] detailProduct, String transactionID, int sizeModel) {
        this.detailJumlah = detailJumlah;
        this.detailProduct = detailProduct;
        this.transactionID = transactionID;
        this.sizeModel = sizeModel;
    }

    public String[] getDetailJumlah() {
        return detailJumlah;
    }

    public void setDetailJumlah(String[] detailJumlah) {
        this.detailJumlah = detailJumlah;
    }

    public String[] getDetailProduct() {
        return detailProduct;
    }

    public void setDetailProduct(String[] detailProduct) {
        this.detailProduct = detailProduct;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public int getSizeModel() {
        return sizeModel;
    }

    public void setSizeModel(int sizeModel) {
        this.sizeModel = sizeModel;
    }
}
