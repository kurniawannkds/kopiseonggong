package com.androiddevnkds.kopiseong.module.wallet;

import com.androiddevnkds.kopiseong.model.TotalBalanceModel;

public interface WalletContract {

    interface walletView{

        void showUserName(String userName, String status);

        void showProgressBar();

        void hideProgressBar();

        void showDiagram(long totalIncome, long totalExpense, long hpp);

        void onFailed(String message);

        void showDetailBalance(TotalBalanceModel.TotalBalanceSatuan totalBalanceSatuan, int pos);

        void showBalance(long totalCash, long totalAcc,long totalIncome, long totalExpense, long incomeRek, long expenseRek, long hpp, long avalaibleBalance,
                         TotalBalanceModel totalBalanceModel);

        void showSuccessInit(String message);
    }

    interface walletPresenter{

        void getBalance(String balanceID);

        void onFailed(String message);

        void onSuccess(TotalBalanceModel totalBalanceModel);

        void getUserName();

        void getDetailBalance(TotalBalanceModel totalBalanceModel, int pos);

        void insertBalance(String balanceID, String date, long cash, long rekening);
    }
}
