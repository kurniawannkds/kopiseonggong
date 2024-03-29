package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailTransactionModel {

    @SerializedName("detail_transaction_list")
    private List<DetailTransaction> detailTransactionList;

    @SerializedName("error_msg")
    private String errorMessage;

    public DetailTransactionModel() {
    }

    public DetailTransactionModel(List<DetailTransaction> detailTransactionList, String errorMessage) {
        this.detailTransactionList = detailTransactionList;
        this.errorMessage = errorMessage;
    }

    public List<DetailTransaction> getDetailTransactionList() {
        return detailTransactionList;
    }

    public void setDetailTransactionList(List<DetailTransaction> detailTransactionList) {
        this.detailTransactionList = detailTransactionList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class CustomerName{

        @SerializedName("name_user")
        private String nameUser;

        public CustomerName() {
        }

        public CustomerName(String nameUser) {
            this.nameUser = nameUser;
        }

        public String getNameUser() {
            return nameUser;
        }

        public void setNameUser(String nameUser) {
            this.nameUser = nameUser;
        }
    }

    public class DetailTransaction{

        @SerializedName("detail_trans_ID")
        private String detailID;

        @SerializedName("detail_trans_jumlah")
        private int detailJumlah;

        @SerializedName("detail_trans_product_id")
        private String detailProductID;

        @SerializedName("detail_trans_transaction_id")
        private String detailTransactionID;

        public DetailTransaction(String detailID, int detailJumlah, String detailProductID, String detailTransactionID) {
            this.detailID = detailID;
            this.detailJumlah = detailJumlah;
            this.detailProductID = detailProductID;
            this.detailTransactionID = detailTransactionID;
        }

        public DetailTransaction() {
        }

        public String getDetailID() {
            return detailID;
        }

        public void setDetailID(String detailID) {
            this.detailID = detailID;
        }

        public int getDetailJumlah() {
            return detailJumlah;
        }

        public void setDetailJumlah(int detailJumlah) {
            this.detailJumlah = detailJumlah;
        }

        public String getDetailProductID() {
            return detailProductID;
        }

        public void setDetailProductID(String detailProductID) {
            this.detailProductID = detailProductID;
        }

        public String getDetailTransactionID() {
            return detailTransactionID;
        }

        public void setDetailTransactionID(String detailTransactionID) {
            this.detailTransactionID = detailTransactionID;
        }
    }
}
