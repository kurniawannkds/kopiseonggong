package com.androiddevnkds.kopiseong.module.register;

public interface RegisterContract {

    interface registerView{

        void showProgressBar();

        void hideProgressBar();

        void onSuccess();

        void onFailed(int tipe,String message);
    }

    interface registerPresenter{

        void onSuccess();

        void onFailed(int tipe,String message);
    }
}
