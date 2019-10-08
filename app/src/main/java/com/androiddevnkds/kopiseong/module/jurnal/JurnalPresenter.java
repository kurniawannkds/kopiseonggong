package com.androiddevnkds.kopiseong.module.jurnal;

import android.util.Log;

import com.androiddevnkds.kopiseong.model.AssetModel;
import com.androiddevnkds.kopiseong.model.JurnalModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

public class JurnalPresenter implements  JurnalContract.jurnalPresenter {

    private JurnalContract.jurnalView jurnalView;

    public JurnalPresenter(JurnalContract.jurnalView jurnalView){
        this.jurnalView = jurnalView;
    }
    @Override
    public void getAllJurnalPerMonth(String month) {

        jurnalView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_JURNAL)
                .addBodyParameter("select", "select")
                .addBodyParameter("month", month)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(JurnalModel.class, new ParsedRequestListener<JurnalModel>() {
                    @Override
                    public void onResponse(JurnalModel jurnalModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(jurnalModel));
                        if(jurnalModel.getErrorMessage()!=null){
                            onFailed(jurnalModel.getErrorMessage());
                        }
                        else {

                            jurnalView.hideProgressBar();
                            jurnalView.showAllJurnalPerMonth(jurnalModel);
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
        jurnalView.hideProgressBar();
        jurnalView.onFailed(message);
    }
}
