package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class GetDetailTransResponseModel {

    @SerializedName("detail_transaction_list")
    private DetailSatuan detailJumlah;

    @SerializedName("error_msg")
    private String errorMessage;

    public GetDetailTransResponseModel() {
    }

    public GetDetailTransResponseModel(DetailSatuan detailJumlah, String errorMessage) {
        this.detailJumlah = detailJumlah;
        this.errorMessage = errorMessage;
    }

    public DetailSatuan getDetailJumlah() {
        return detailJumlah;
    }

    public void setDetailJumlah(DetailSatuan detailJumlah) {
        this.detailJumlah = detailJumlah;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class DetailSatuan{

        @SerializedName("detail_trans_jumlah")
        private int detailJumlah;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("product_price")
        private int productPrice;

        @SerializedName("product_resep_id")
        private String productResepID;

        @SerializedName("product_general_category")
        private String productGeneralCategory;

        public DetailSatuan() {
        }

        public DetailSatuan(int detailJumlah, String productName, int productPrice, String productResepID, String productGeneralCategory) {
            this.detailJumlah = detailJumlah;
            this.productName = productName;
            this.productPrice = productPrice;
            this.productResepID = productResepID;
            this.productGeneralCategory = productGeneralCategory;
        }

        public int getDetailJumlah() {
            return detailJumlah;
        }

        public void setDetailJumlah(int detailJumlah) {
            this.detailJumlah = detailJumlah;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(int productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductResepID() {
            return productResepID;
        }

        public void setProductResepID(String productResepID) {
            this.productResepID = productResepID;
        }

        public String getProductGeneralCategory() {
            return productGeneralCategory;
        }

        public void setProductGeneralCategory(String productGeneralCategory) {
            this.productGeneralCategory = productGeneralCategory;
        }
    }

}
