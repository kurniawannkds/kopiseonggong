package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    @SerializedName("product_list")
    private List<ProductSatuan> productSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public ProductModel() {
    }

    public ProductModel(List<ProductSatuan> productSatuanList, String errorMessage) {
        this.productSatuanList = productSatuanList;
        this.errorMessage = errorMessage;
    }

    public List<ProductSatuan> getProductSatuanList() {
        return productSatuanList;
    }

    public void setProductSatuanList(List<ProductSatuan> productSatuanList) {
        this.productSatuanList = productSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class ProductSatuan {

        @SerializedName("product_id")
        private String productID;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("product_price")
        private int productPrice;

        @SerializedName("product_resep_id")
        private String productResepID;

        @SerializedName("product_general_category")
        private String productGeneralCategory;

        public ProductSatuan() {
        }

        public ProductSatuan(String productID, String productName, int productPrice, String productResepID, String productGeneralCategory) {
            this.productID = productID;
            this.productName = productName;
            this.productPrice = productPrice;
            this.productResepID = productResepID;
            this.productGeneralCategory = productGeneralCategory;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
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
