package com.androiddevnkds.kopiseong.module.wallet;

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

public class WalletPresenter implements WalletContract.walletPresenter {

    private WalletContract.walletView walletView;
    private long income=0, expense=0,incomeRek=0, expenseRek=0, hpp=0, laba=0;
    private String userName, status;

    public WalletPresenter(WalletContract.walletView walletView){
        this.walletView = walletView;
    }


    @Override
    public void getBalance(String balanceID) {

        walletView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_BALANCE)
                .addBodyParameter("select","select")
                .addBodyParameter("total_balance_id",balanceID)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(TotalBalanceModel.class, new ParsedRequestListener<TotalBalanceModel>() {
                    @Override
                    public void onResponse(TotalBalanceModel totalBalanceModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(totalBalanceModel));
                        if(totalBalanceModel.getErrorMessage()!=null){
                            onFailed(totalBalanceModel.getErrorMessage());
                        }
                        else {
                            walletView.hideProgressBar();
                            onSuccess(totalBalanceModel);
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
    public void onFailed(String message) {

        walletView.hideProgressBar();
        walletView.onFailed(message);
    }

    @Override
    public void onSuccess(TotalBalanceModel totalBalanceModel) {

        long cash =totalBalanceModel.getTotalAllSum().getTotalAllCash();
        long acc = totalBalanceModel.getTotalAllSum().getTotalAllAccount();
        expense = totalBalanceModel.getTotalAllSum().getTotalAllPengeluaran();
        income = totalBalanceModel.getTotalAllSum().getTotalAllPemasukan();
        incomeRek = totalBalanceModel.getTotalAllSum().getTotalAllPemasukanRek();
        expenseRek = totalBalanceModel.getTotalAllSum().getTotalAllPengeluaranRek();
        hpp = totalBalanceModel.getTotalAllSum().getTotalAllHpp();

        laba = (income + incomeRek) - expense - hpp - expenseRek;

        walletView.hideProgressBar();
        walletView.showDiagram(income,expense,incomeRek,expenseRek,hpp);
        walletView.showBalance(cash,acc,income,expense,incomeRek,expenseRek,hpp,laba,totalBalanceModel);
    }

    @Override
    public void getUserName() {
        walletView.showProgressBar();
        if(DataManager.can().getUserInfoFromStorage()!=null){
            Log.e("WELCOME", new Gson().toJson(DataManager.can().getUserInfoFromStorage()));
            userName = DataManager.can().getUserInfoFromStorage().getNameUser();
            status = DataManager.can().getUserInfoFromStorage().getUserRole();
            walletView.showUserName(userName,status);
        }
        else {
            walletView.showUserName("Error","Error");
        }
    }

    @Override
    public void getDetailBalance(TotalBalanceModel totalBalanceModel, int pos) {

        TotalBalanceModel.TotalBalanceSatuan totalBalanceSatuan = totalBalanceModel.getTotalBalanceSatuanList().get(pos);
        walletView.showDetailBalance(totalBalanceSatuan,pos);

        walletView.showDetailBalance(totalBalanceSatuan,pos);
    }

    @Override
    public void insertBalance(String balanceID, String date, long cash, long rekening) {

        walletView.showProgressBar();
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
                                walletView.hideProgressBar();
                                walletView.showSuccessInit(updateResponseModel.getSuccessMessage());
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
