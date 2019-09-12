package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionModel {

    @SerializedName("error_msg")
    private String errorMessage;

    @SerializedName("transaction_list")
    private List<TransactionSatuanModel> transactionModelLists;

    @SerializedName("total_row")
    private int totalRow;

    public TransactionModel() {
    }

    public TransactionModel(String errorMessage, List<TransactionSatuanModel> transactionModelLists, int totalRow) {
        this.errorMessage = errorMessage;
        this.transactionModelLists = transactionModelLists;
        this.totalRow = totalRow;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<TransactionSatuanModel> getTransactionModelLists() {
        return transactionModelLists;
    }

    public void setTransactionModelLists(List<TransactionSatuanModel> transactionModelLists) {
        this.transactionModelLists = transactionModelLists;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

//    public class TransactionModelList{
//
//        @SerializedName("trans_id")
//        private String transactionID;
//
//        @SerializedName("trans_category")
//        private String transactionCategory;
//
//        @SerializedName("trans_date_sort")
//        private int transactionDateSort;
//
//        @SerializedName("trans_date")
//        private String transactionDate;
//
//        @SerializedName("trans_time")
//        private String transactionTime;
//
//        @SerializedName("trans_price")
//        private int transactionBalance;
//
//        @SerializedName("trans_user_email")
//        private String userEmail;
//
//        public String getTransactionID() {
//            return transactionID;
//        }
//
//        public void setTransactionID(String transactionID) {
//            this.transactionID = transactionID;
//        }
//
//        public String getTransactionCategory() {
//            return transactionCategory;
//        }
//
//        public void setTransactionCategory(String transactionCategory) {
//            this.transactionCategory = transactionCategory;
//        }
//
//        public String getTransactionDate() {
//            return transactionDate;
//        }
//
//        public void setTransactionDate(String transactionDate) {
//            this.transactionDate = transactionDate;
//        }
//
//        public int getTransactionDateSort() {
//            return transactionDateSort;
//        }
//
//        public void setTransactionDateSort(int transactionDateSort) {
//            this.transactionDateSort = transactionDateSort;
//        }
//
//        public String getTransactionTime() {
//            return transactionTime;
//        }
//
//        public void setTransactionTime(String transactionTime) {
//            this.transactionTime = transactionTime;
//        }
//
//        public int getTransactionBalance() {
//            return transactionBalance;
//        }
//
//        public void setTransactionBalance(int transactionBalance) {
//            this.transactionBalance = transactionBalance;
//        }
//
//        public String getUserEmail() {
//            return userEmail;
//        }
//
//        public void setUserEmail(String userEmail) {
//            this.userEmail = userEmail;
//        }
//
//        public TransactionModelList() {
//        }
//
//        public TransactionModelList(String transactionID, String transactionCategory, int transactionDateSort, String transactionDate, String transactionTime, int transactionBalance, String userEmail) {
//            this.transactionID = transactionID;
//            this.transactionCategory = transactionCategory;
//            this.transactionDateSort = transactionDateSort;
//            this.transactionDate = transactionDate;
//            this.transactionTime = transactionTime;
//            this.transactionBalance = transactionBalance;
//            this.userEmail = userEmail;
//        }
//    }


}
