package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResepModel {

    @SerializedName("resep_list")
    private List<ResepModelSatuan> resepModelSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public ResepModel(List<ResepModelSatuan> resepModelSatuanList, String errorMessage) {
        this.resepModelSatuanList = resepModelSatuanList;
        this.errorMessage = errorMessage;
    }

    public ResepModel() {
    }

    public List<ResepModelSatuan> getResepModelSatuanList() {
        return resepModelSatuanList;
    }

    public void setResepModelSatuanList(List<ResepModelSatuan> resepModelSatuanList) {
        this.resepModelSatuanList = resepModelSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class ResepModelSatuan{

        @SerializedName("resep_id")
        private String resepID;

        @SerializedName("resep_item")
        private String resepItem;

        @SerializedName("resep_jumlah_Item_1")
        private String resepJumlahItem;

        @SerializedName("resep_total_price")
        private double resepTotalPrice;

        public ResepModelSatuan() {
        }

        public ResepModelSatuan(String resepID, String resepItem, String resepJumlahItem, double resepTotalPrice) {
            this.resepID = resepID;
            this.resepItem = resepItem;
            this.resepJumlahItem = resepJumlahItem;
            this.resepTotalPrice = resepTotalPrice;
        }

        public String getResepID() {
            return resepID;
        }

        public void setResepID(String resepID) {
            this.resepID = resepID;
        }

        public String getResepItem() {
            return resepItem;
        }

        public void setResepItem(String resepItem) {
            this.resepItem = resepItem;
        }

        public String getResepJumlahItem() {
            return resepJumlahItem;
        }

        public void setResepJumlahItem(String resepJumlahItem) {
            this.resepJumlahItem = resepJumlahItem;
        }

        public double getResepTotalPrice() {
            return resepTotalPrice;
        }

        public void setResepTotalPrice(double resepTotalPrice) {
            this.resepTotalPrice = resepTotalPrice;
        }
    }
}
