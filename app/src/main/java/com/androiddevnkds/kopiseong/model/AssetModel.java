package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetModel {

    @SerializedName("asset_list")
    private List<AssetModelSatuan> assetModelSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public AssetModel(List<AssetModelSatuan> assetModelSatuanList, String errorMessage) {
        this.assetModelSatuanList = assetModelSatuanList;
        this.errorMessage = errorMessage;
    }

    public AssetModel() {
    }

    public List<AssetModelSatuan> getAssetModelSatuanList() {
        return assetModelSatuanList;
    }

    public void setAssetModelSatuanList(List<AssetModelSatuan> assetModelSatuanList) {
        this.assetModelSatuanList = assetModelSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class AssetModelSatuan{

        @SerializedName("asset_id")
        private String assetID;

        @SerializedName("asset_name")
        private String assetName;

        @SerializedName("asset_price")
        private long assetPrice;

        @SerializedName("asset_date")
        private String assetDate;

        @SerializedName("asset_depreciation_per_year")
        private int assetPercent;

        public AssetModelSatuan() {
        }

        public AssetModelSatuan(String assetID, String assetName, long assetPrice, String assetDate, int assetPercent) {
            this.assetID = assetID;
            this.assetName = assetName;
            this.assetPrice = assetPrice;
            this.assetDate = assetDate;
            this.assetPercent = assetPercent;
        }

        public String getAssetID() {
            return assetID;
        }

        public void setAssetID(String assetID) {
            this.assetID = assetID;
        }

        public String getAssetName() {
            return assetName;
        }

        public void setAssetName(String assetName) {
            this.assetName = assetName;
        }

        public long getAssetPrice() {
            return assetPrice;
        }

        public void setAssetPrice(long assetPrice) {
            this.assetPrice = assetPrice;
        }

        public String getAssetDate() {
            return assetDate;
        }

        public void setAssetDate(String assetDate) {
            this.assetDate = assetDate;
        }

        public int getAssetPercent() {
            return assetPercent;
        }

        public void setAssetPercent(int assetPercent) {
            this.assetPercent = assetPercent;
        }
    }

}
