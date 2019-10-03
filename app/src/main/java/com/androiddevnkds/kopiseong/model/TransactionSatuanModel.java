package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class TransactionSatuanModel {

    @SerializedName("trans_id")
    private String transactionID;

    @SerializedName("trans_category")
    private String transactionCategory;

    @SerializedName("trans_date")
    private String transactionDate;

    @SerializedName("trans_time")
    private String transactionTime;

    @SerializedName("trans_price")
    private long transactionBalance;

    @SerializedName("trans_user_email")
    private String userEmail;

    @SerializedName("trans_tipe_pembayaran")
    private String tipePembayaran;

    @SerializedName("trans_keterangan")
    private String keterangan;

    public String getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(String tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public long getTransactionBalance() {
        return transactionBalance;
    }

    public void setTransactionBalance(long transactionBalance) {
        this.transactionBalance = transactionBalance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public TransactionSatuanModel(String transactionID, String transactionCategory, String transactionDate, String transactionTime, long transactionBalance, String userEmail, String tipePembayaran) {
        this.transactionID = transactionID;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionBalance = transactionBalance;
        this.userEmail = userEmail;
        this.tipePembayaran = tipePembayaran;
    }

    public TransactionSatuanModel(String transactionID, String transactionCategory, String transactionDate, String transactionTime, long transactionBalance, String userEmail, String tipePembayaran, String keterangan) {
        this.transactionID = transactionID;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionBalance = transactionBalance;
        this.userEmail = userEmail;
        this.tipePembayaran = tipePembayaran;
        this.keterangan = keterangan;
    }

    public TransactionSatuanModel() {
    }
}
