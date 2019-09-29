package com.androiddevnkds.kopiseong.module.wallet;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

public class WalletPresenter implements WalletContract.walletPresenter {

    private WalletContract.walletView walletView;
    private long income=0, expense=0,incomeRek=0, expenseRek=0, hpp=0, avalaible=0;
    private String userName, status;

    public WalletPresenter(WalletContract.walletView walletView){
        this.walletView = walletView;
    }


    @Override
    public void getBalance() {

        AndroidNetworking.post(K.URL_GET_ALL_BALANCE)
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

        expense = totalBalanceModel.getTotalAllSum().getTotalAllPengeluaran();
        income = totalBalanceModel.getTotalAllSum().getTotalAllPemasukan();
        incomeRek = totalBalanceModel.getTotalAllSum().getTotalAllPemasukanRek();
        expenseRek = totalBalanceModel.getTotalAllSum().getTotalAllPengeluaranRek();
        hpp = totalBalanceModel.getTotalAllSum().getTotalAllHpp();

        avalaible = (income + incomeRek) - expense - hpp - expenseRek;

        walletView.hideProgressBar();
        walletView.showDiagram(income,expense,incomeRek,expenseRek,hpp);
        walletView.showBalance(income,expense,incomeRek,expenseRek,hpp,avalaible,totalBalanceModel);
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
}
