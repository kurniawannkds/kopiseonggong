package com.androiddevnkds.kopiseong.module.addTransaction;

public interface AddTransactionContract {

    interface addTransactionView{

        void showProgressBar();

        void hideProgressBar();

        void onFailedGetAllAPI(String message);

    }
    interface addTransactionPresenter{

    }
}
