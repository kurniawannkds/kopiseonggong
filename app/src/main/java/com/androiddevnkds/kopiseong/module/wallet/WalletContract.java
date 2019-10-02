package com.androiddevnkds.kopiseong.module.wallet;

import com.androiddevnkds.kopiseong.model.TotalBalanceModel;

public interface WalletContract {

    interface walletView{

        void showUserName(String userName, String status);

        void showProgressBar();

        void hideProgressBar();

        void showDiagram(long totalIncome, long totalExpense, long totalIncomeRek, long totalExpenseRek, long hpp);

        void onFailed(String message);

        void showDetailBalance(TotalBalanceModel.TotalBalanceSatuan totalBalanceSatuan, int pos);

        void showBalance(long totalIncome, long totalExpense, long incomeRek, long expenseRek, long hpp, long avalaibleBalance,
                         TotalBalanceModel totalBalanceModel);
    }

    interface walletPresenter{

        void getBalance();

        void onFailed(String message);

        void onSuccess(TotalBalanceModel totalBalanceModel);

        void getUserName();

        void getDetailBalance(TotalBalanceModel totalBalanceModel, int pos);
    }
}
