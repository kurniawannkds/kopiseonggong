package com.androiddevnkds.kopiseong.module.home;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
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

    @Override
    public void initBalance(String balanceID, String balanceIDBefore) {

        AndroidNetworking.post(K.URL_GET_ALL_BALANCE)
                .addBodyParameter("Adjust_new_balance","adjust")
                .addBodyParameter("total_balance_id",balanceID)
                .addBodyParameter("total_balance_id_before",balanceIDBefore)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                    @Override
                    public void onResponse(UpdateResponseModel updateResponseModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(updateResponseModel));
                        if(updateResponseModel.getErrorMessage()!=null){
                            onFailed(updateResponseModel.getErrorMessage());
                        }
                        else {
                            homeView.hideProgressBar();
                            homeView.successInitBalance(updateResponseModel.getSuccessMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed("ERROR");
                    }
                });
    }

    private void onFailed(String message){
        homeView.hideProgressBar();
        homeView.onfailed(message);
    }
}
