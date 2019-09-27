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

    public TransactionSatuanModel(String transactionID, String transactionCategory, String transactionDate, String transactionTime, long transactionBalance, String userEmail) {
        this.transactionID = transactionID;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transactionBalance = transactionBalance;
        this.userEmail = userEmail;
    }

    public TransactionSatuanModel() {
    }
}
