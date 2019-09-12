package com.androiddevnkds.kopiseong.module.home;

public interface HomeContract {

    interface homeView{
        void showUserName(String userName, String status);
    }

    interface homePresenter{
        void getUserName();
    }
}
