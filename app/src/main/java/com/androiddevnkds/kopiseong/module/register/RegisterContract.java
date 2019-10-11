package com.androiddevnkds.kopiseong.module.register;

import java.util.ArrayList;
import java.util.List;

public interface RegisterContract {

    interface registerView{

        void showProgressBar();

        void hideProgressBar();

        void onSuccess(String message);

        void onFailed(int tipe,String message);

        void showUserRole(List userRole);
    }

    interface registerPresenter{

        void onFailed(int tipe,String message);

        void getAllUserRole();
    }
}
