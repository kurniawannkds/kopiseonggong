package com.androiddevnkds.kopiseong.module.login;

import android.content.Context;

public interface LoginContract {

    interface loginView{
        void showProgressBar();

        void hideProgressBar();

        void onSuccess();

        void onFailed(int tipe,String message);
    }

    interface loginPresenter{

        void onSuccess();

        void onFailed(int tipe,String message);

    }
}
