package com.androiddevnkds.kopiseong.module.home;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.google.gson.Gson;

public class HomePresenter implements HomeContract.homePresenter{
    private String userName, status;
    private HomeContract.homeView homeView;

    public HomePresenter(HomeContract.homeView homeView){
        this.homeView = homeView;
    }
    @Override
    public void getUserName() {

        if(DataManager.can().getUserInfoFromStorage()!=null){
            Log.e("WELCOME", new Gson().toJson(DataManager.can().getUserInfoFromStorage()));
            userName = DataManager.can().getUserInfoFromStorage().getNameUser();
            status = DataManager.can().getUserInfoFromStorage().getUserRole();
            homeView.showUserName(userName,status);
        }
        else {
            homeView.showUserName("Error","Error");
        }
    }
}
