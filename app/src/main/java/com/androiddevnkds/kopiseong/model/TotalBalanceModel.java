package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalBalanceModel {

    @SerializedName("total_balance_list")
    private List<TotalBalanceSatuan> totalBalanceSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    @SerializedName("total_all_sum")
    private TotalAllSum totalAllSum;

    public TotalBalanceModel() {
    }

    public TotalBalanceModel(List<TotalBalanceSatuan> totalBalanceSatuanList, String errorMessage, TotalAllSum totalAllSum) {
        this.totalBalanceSatuanList = totalBalanceSatuanList;
        this.errorMessage = errorMessage;
        this.totalAllSum = totalAllSum;
    }

    public List<TotalBalanceSatuan> getTotalBalanceSatuanList() {
        return totalBalanceSatuanList;
    }

    public void setTotalBalanceSatuanList(List<TotalBalanceSatuan> totalBalanceSatuanList) {
        this.totalBalanceSatuanList = totalBalanceSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TotalAllSum getTotalAllSum() {
        return totalAllSum;
    }

    public void setTotalAllSum(TotalAllSum totalAllSum) {
        this.totalAllSum = totalAllSum;
    }

    public class TotalBalanceSatuan{

        @SerializedName("total_balance_id")
        private int totalBalanceID;

        @SerializedName("total_pemasukan")
        private long totalBalancePemasukan;

        @SerializedName("total_pengeluaran")
        private long totalBalancePengeluaran;

        @SerializedName("total_pajak")
        private long totalBalancePajak;

        public TotalBalanceSatuan() {
        }

        public TotalBalanceSatuan(int totalBalanceID, long totalBalancePemasukan, long totalBalancePengeluaran, long totalBalancePajak) {
            this.totalBalanceID = totalBalanceID;
            this.totalBalancePemasukan = totalBalancePemasukan;
            this.totalBalancePengeluaran = totalBalancePengeluaran;
            this.totalBalancePajak = totalBalancePajak;
        }

        public int getTotalBalanceID() {
            return totalBalanceID;
        }

        public void setTotalBalanceID(int totalBalanceID) {
            this.totalBalanceID = totalBalanceID;
        }

        public long getTotalBalancePemasukan() {
            return totalBalancePemasukan;
        }

        public void setTotalBalancePemasukan(long totalBalancePemasukan) {
            this.totalBalancePemasukan = totalBalancePemasukan;
        }

        public long getTotalBalancePengeluaran() {
            return totalBalancePengeluaran;
        }

        public void setTotalBalancePengeluaran(long totalBalancePengeluaran) {
            this.totalBalancePengeluaran = totalBalancePengeluaran;
        }

        public long getTotalBalancePajak() {
            return totalBalancePajak;
        }

        public void setTotalBalancePajak(long totalBalancePajak) {
            this.totalBalancePajak = totalBalancePajak;
        }
    }

    public class TotalAllSum{

        @SerializedName("total_all_pemasukan")
        private long totalAllPemasukan;

        @SerializedName("total_all_pengeluaran")
        private long totalAllPengeluaran;

        @SerializedName("total_all_pajak")
        private long totalAllPajak;

        public TotalAllSum() {
        }

        public TotalAllSum(long totalAllPemasukan, long totalAllPengeluaran, long totalAllPajak) {
            this.totalAllPemasukan = totalAllPemasukan;
            this.totalAllPengeluaran = totalAllPengeluaran;
            this.totalAllPajak = totalAllPajak;
        }

        public long getTotalAllPemasukan() {
            return totalAllPemasukan;
        }

        public void setTotalAllPemasukan(long totalAllPemasukan) {
            this.totalAllPemasukan = totalAllPemasukan;
        }

        public long getTotalAllPengeluaran() {
            return totalAllPengeluaran;
        }

        public void setTotalAllPengeluaran(long totalAllPengeluaran) {
            this.totalAllPengeluaran = totalAllPengeluaran;
        }

        public long getTotalAllPajak() {
            return totalAllPajak;
        }

        public void setTotalAllPajak(long totalAllPajak) {
            this.totalAllPajak = totalAllPajak;
        }
    }
}
