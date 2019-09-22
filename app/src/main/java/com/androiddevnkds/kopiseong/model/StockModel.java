package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockModel {

    @SerializedName("stock_list")
    private List<StockSatuanModel> stockSatuanModelList;

    @SerializedName("error_msg")
    private String errorMessage;

    public StockModel() {
    }

    public StockModel(List<StockSatuanModel> stockSatuanModelList, String errorMessage) {
        this.stockSatuanModelList = stockSatuanModelList;
        this.errorMessage = errorMessage;
    }

    public List<StockSatuanModel> getStockSatuanModelList() {
        return stockSatuanModelList;
    }

    public void setStockSatuanModelList(List<StockSatuanModel> stockSatuanModelList) {
        this.stockSatuanModelList = stockSatuanModelList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class StockSatuanModel{

        @SerializedName("stock_date_sort")
        private String stockDateSort;

        @SerializedName("stock_id")
        private String stockID;

        @SerializedName("stock_name")
        private String stockName;

        @SerializedName("stock_gram")
        private long stockGram;

        @SerializedName("stock_price")
        private long stockPrice;

        @SerializedName("stock_date")
        private String stockDate;

        @SerializedName("stock_price_per_gram")
        private double stockPricePerGram;

        public StockSatuanModel() {
        }

        public StockSatuanModel(String stockDateSort, String stockID, String stockName, long stockGram, long stockPrice, String stockDate, double stockPricePerGram) {
            this.stockDateSort = stockDateSort;
            this.stockID = stockID;
            this.stockName = stockName;
            this.stockGram = stockGram;
            this.stockPrice = stockPrice;
            this.stockDate = stockDate;
            this.stockPricePerGram = stockPricePerGram;
        }

        public String getStockDateSort() {
            return stockDateSort;
        }

        public void setStockDateSort(String stockDateSort) {
            this.stockDateSort = stockDateSort;
        }

        public String getStockID() {
            return stockID;
        }

        public void setStockID(String stockID) {
            this.stockID = stockID;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public long getStockGram() {
            return stockGram;
        }

        public void setStockGram(long stockGram) {
            this.stockGram = stockGram;
        }

        public long getStockPrice() {
            return stockPrice;
        }

        public void setStockPrice(long stockPrice) {
            this.stockPrice = stockPrice;
        }

        public String getStockDate() {
            return stockDate;
        }

        public void setStockDate(String stockDate) {
            this.stockDate = stockDate;
        }

        public double getStockPricePerGram() {
            return stockPricePerGram;
        }

        public void setStockPricePerGram(double stockPricePerGram) {
            this.stockPricePerGram = stockPricePerGram;
        }
    }
}
