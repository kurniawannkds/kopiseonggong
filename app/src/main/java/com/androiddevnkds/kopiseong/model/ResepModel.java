package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class ResepModel {

    @SerializedName("resep_model")
    private ResepModelSatuan resepModelSatuan;

    @SerializedName("error_msg")
    private String errorMessage;

    public ResepModel(ResepModelSatuan resepModelSatuan, String errorMessage) {
        this.resepModelSatuan = resepModelSatuan;
        this.errorMessage = errorMessage;
    }

    public ResepModel() {
    }

    public ResepModelSatuan getResepModelSatuan() {
        return resepModelSatuan;
    }

    public void setResepModelSatuan(ResepModelSatuan resepModelSatuan) {
        this.resepModelSatuan = resepModelSatuan;
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
        private long resepTotalPrice;

        public ResepModelSatuan() {
        }

        public ResepModelSatuan(String resepID, String resepItem, String resepJumlahItem, long resepTotalPrice) {
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

        public long getResepTotalPrice() {
            return resepTotalPrice;
        }

        public void setResepTotalPrice(long resepTotalPrice) {
            this.resepTotalPrice = resepTotalPrice;
        }
    }
}
