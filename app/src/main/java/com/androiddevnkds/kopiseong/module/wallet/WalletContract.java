package com.androiddevnkds.kopiseong.module.wallet;

import com.androiddevnkds.kopiseong.model.TotalBalanceModel;

public interface WalletContract {

    interface walletView{

        void showUserName(String userName, String status);

        void showProgressBar();

        void hideProgressBar();

        void showDiagram(long totalIncome, long totalExpense, long totalTax);

        void onFailed(String message);

        void showBalance(long totalIncome, long totalExpense, long totalTax, long avalaibleBalance,
                         TotalBalanceModel totalBalanceModel);
    }

    interface walletPresenter{

        void getBalance();

        void onFailed(String message);

        void onSuccess(TotalBalanceModel totalBalanceModel);

        void getUserName();
    }
}
