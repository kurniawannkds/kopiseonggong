package com.androiddevnkds.kopiseong.module.home;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
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

        homeView.showProgressBar();
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

    @Override
    public void insertBalance(String balanceID,String date, long cash, long rekening) {

        homeView.showProgressBar();
        DateAndTime dateAndTime = new DateAndTime();
        String dateTemp = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT);
        String timeSort = dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
        String time = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
        String transID = dateTemp + timeSort;
        String user = "";
        if(DataManager.can().getUserInfoFromStorage()!=null){
            if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                user = DataManager.can().getUserInfoFromStorage().getUserEmail();
            }
        }
        if(!isEmpty(balanceID,date,cash,rekening)) {
            AndroidNetworking.post(K.URL_GET_ALL_BALANCE)
                    .addBodyParameter("add", "add")
                    .addBodyParameter("total_balance_id", balanceID)
                    .addBodyParameter("trans_id",transID)
                    .addBodyParameter("trans_date",date)
                    .addBodyParameter("trans_time",time)
                    .addBodyParameter("trans_user_email",user)
                    .addBodyParameter("total_cash_balance", cash + "")
                    .addBodyParameter("total_account_balance", rekening + "")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            Log.e("BASE", new Gson().toJson(updateResponseModel));
                            if (updateResponseModel.getErrorMessage() != null) {
                                onFailed(updateResponseModel.getErrorMessage());
                            } else {
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
    }

    private void onFailed(String message){
        homeView.hideProgressBar();
        homeView.onfailed(message);
    }

    private boolean isEmpty(String balanceID,String date, long cash, long acc){

        if(balanceID.equalsIgnoreCase("")){
            onFailed("ID cannot be empty");
            return true;
        }
        else if(date.equalsIgnoreCase("")){
            onFailed("Date cannot be empty");
            return true;
        }
        else if(cash==0){
            onFailed("Cash cannot be empty");
            return true;
        }

        else if(acc==0){
            onFailed("Acc cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }
}
