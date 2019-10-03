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

        @SerializedName("total_cash_balance")
        private long totalCashBalance;

        @SerializedName("total_account_balance")
        private long totalAccBalance;

        @SerializedName("total_pemasukan")
        private long totalBalancePemasukan;

        @SerializedName("total_pengeluaran")
        private long totalBalancePengeluaran;

        @SerializedName("total_rekening_income")
        private long totalBalancePemasukanRek;

        @SerializedName("total_rekening_expense")
        private long totalBalancePengeluaranRek;

        @SerializedName("total_balance_hpp")
        private long totalBalanceHpp;


        public TotalBalanceSatuan() {
        }

        public TotalBalanceSatuan(int totalBalanceID, long totalCashBalance, long totalAccBalance, long totalBalancePemasukan, long totalBalancePengeluaran, long totalBalancePemasukanRek, long totalBalancePengeluaranRek, long totalBalanceHpp) {
            this.totalBalanceID = totalBalanceID;
            this.totalCashBalance = totalCashBalance;
            this.totalAccBalance = totalAccBalance;
            this.totalBalancePemasukan = totalBalancePemasukan;
            this.totalBalancePengeluaran = totalBalancePengeluaran;
            this.totalBalancePemasukanRek = totalBalancePemasukanRek;
            this.totalBalancePengeluaranRek = totalBalancePengeluaranRek;
            this.totalBalanceHpp = totalBalanceHpp;
        }

        public long getTotalCashBalance() {
            return totalCashBalance;
        }

        public void setTotalCashBalance(long totalCashBalance) {
            this.totalCashBalance = totalCashBalance;
        }

        public long getTotalAccBalance() {
            return totalAccBalance;
        }

        public void setTotalAccBalance(long totalAccBalance) {
            this.totalAccBalance = totalAccBalance;
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

        public long getTotalBalancePemasukanRek() {
            return totalBalancePemasukanRek;
        }

        public void setTotalBalancePemasukanRek(long totalBalancePemasukanRek) {
            this.totalBalancePemasukanRek = totalBalancePemasukanRek;
        }

        public long getTotalBalancePengeluaranRek() {
            return totalBalancePengeluaranRek;
        }

        public void setTotalBalancePengeluaranRek(long totalBalancePengeluaranRek) {
            this.totalBalancePengeluaranRek = totalBalancePengeluaranRek;
        }

        public long getTotalBalanceHpp() {
            return totalBalanceHpp;
        }

        public void setTotalBalanceHpp(long totalBalanceHpp) {
            this.totalBalanceHpp = totalBalanceHpp;
        }
    }

    public class TotalAllSum{

        @SerializedName("total_all_cash")
        private long totalAllCash;

        @SerializedName("total_all_account")
        private long totalAllAccount;

        @SerializedName("total_all_pemasukan")
        private long totalAllPemasukan;

        @SerializedName("total_all_pengeluaran")
        private long totalAllPengeluaran;

        @SerializedName("total_all_rekening_income")
        private long totalAllPemasukanRek;

        @SerializedName("total_all_rekening_expense")
        private long totalAllPengeluaranRek;

        @SerializedName("total_all_hpp")
        private long totalAllHpp;

        public TotalAllSum() {
        }

        public TotalAllSum(long totalAllCash, long totalAllAccount, long totalAllPemasukan, long totalAllPengeluaran, long totalAllPemasukanRek, long totalAllPengeluaranRek, long totalAllHpp) {
            this.totalAllCash = totalAllCash;
            this.totalAllAccount = totalAllAccount;
            this.totalAllPemasukan = totalAllPemasukan;
            this.totalAllPengeluaran = totalAllPengeluaran;
            this.totalAllPemasukanRek = totalAllPemasukanRek;
            this.totalAllPengeluaranRek = totalAllPengeluaranRek;
            this.totalAllHpp = totalAllHpp;
        }

        public long getTotalAllCash() {
            return totalAllCash;
        }

        public void setTotalAllCash(long totalAllCash) {
            this.totalAllCash = totalAllCash;
        }

        public long getTotalAllAccount() {
            return totalAllAccount;
        }

        public void setTotalAllAccount(long totalAllAccount) {
            this.totalAllAccount = totalAllAccount;
        }

        public long getTotalAllPemasukanRek() {
            return totalAllPemasukanRek;
        }

        public void setTotalAllPemasukanRek(long totalAllPemasukanRek) {
            this.totalAllPemasukanRek = totalAllPemasukanRek;
        }

        public long getTotalAllPengeluaranRek() {
            return totalAllPengeluaranRek;
        }

        public void setTotalAllPengeluaranRek(long totalAllPengeluaranRek) {
            this.totalAllPengeluaranRek = totalAllPengeluaranRek;
        }

        public long getTotalAllHpp() {
            return totalAllHpp;
        }

        public void setTotalAllHpp(long totalAllHpp) {
            this.totalAllHpp = totalAllHpp;
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
    }
}
