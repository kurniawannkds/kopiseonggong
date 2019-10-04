package com.androiddevnkds.kopiseong.module.home;

public interface HomeContract {

    interface homeView{
        void showUserName(String userName, String status);

        void successInitBalance(String message);

        void onfailed(String message);

        void showProgressBar();

        void hideProgressBar();
    }

    interface homePresenter{
        void getUserName();

        void initBalance(String balanceID, String balanceIDBefore);

        void insertBalance(String balanceID, String date, long cash, long rekening);
    }
}
