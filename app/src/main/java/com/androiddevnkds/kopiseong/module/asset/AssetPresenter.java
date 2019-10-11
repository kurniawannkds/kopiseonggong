package com.androiddevnkds.kopiseong.module.asset;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.AssetModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

public class AssetPresenter implements AssetContract.assetPresenter {

    private AssetContract.assetView assetView;
    private AssetModel assetModelGlobal;
    private String assetGlobal = "";

    private PaymentMethodeModel paymentMethodeModelGlobal;
    private String paymentGlobal = "";

    private int sizeArray =0, pos = 0;

    public AssetPresenter(AssetContract.assetView assetView){
        this.assetView = assetView;
    }

    @Override
    public void getAllAsset() {

        assetView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                .addBodyParameter("select", "select")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(AssetModel.class, new ParsedRequestListener<AssetModel>() {
                    @Override
                    public void onResponse(AssetModel assetModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(assetModel));
                        if(assetModel.getErrorMessage()!=null){
                            onFailed(assetModel.getErrorMessage());
                        }
                        else {

                            assetView.hideProgressBar();
                            assetView.showAllAsset(assetModel);
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
    public void getAssetForList(final AssetModel assetModelT, final String assetName,final boolean fromCustomeList) {

        assetView.showProgressBar();
        boolean flag = false;
        if(assetModelT!=null) {
            if(assetModelT.getAssetModelSatuanList()!=null){
                if(assetModelT.getAssetModelSatuanList().size()>0){

                    assetModelGlobal = assetModelT;
                    assetGlobal = assetName;
                    setCustomeList(1);
                }
                else {
                    flag = true;
                }
            }
            else {
                flag = true;
            }
        }
        else {
            flag = true;
        }

        //hit api
        if(flag) {

            AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                    .addBodyParameter("select_custome_list", "select")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(AssetModel.class, new ParsedRequestListener<AssetModel>() {
                        @Override
                        public void onResponse(AssetModel assetModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(assetModel));
                            if(assetModel.getErrorMessage()!=null){
                                onFailed(assetModel.getErrorMessage(), fromCustomeList);
                            }
                            else {

                                assetModelGlobal = assetModel;
                                assetGlobal = assetName;
                                setCustomeList(1);
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

    @Override
    public void getTipeBayarList(PaymentMethodeModel paymentMethodeModelT, final String payment) {
        assetView.showProgressBar();
        boolean flag = false;
        if(paymentMethodeModelT!=null) {
            if(paymentMethodeModelT.getPaymentMethodeSatuanList()!=null){
                if(paymentMethodeModelT.getPaymentMethodeSatuanList().size()>0){

                    paymentMethodeModelGlobal = paymentMethodeModelT;
                    paymentGlobal = payment;
                    setCustomeList(2);
                }
                else {
                    flag = true;
                }
            }
            else {
                flag = true;
            }
        }
        else {
            flag = true;
        }

        //hit api
        if(flag){


            AndroidNetworking.post(K.URL_GET_PAYMENT_METHODE)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(PaymentMethodeModel.class, new ParsedRequestListener<PaymentMethodeModel>() {
                        @Override
                        public void onResponse(PaymentMethodeModel paymentMethodeModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(paymentMethodeModel));
                            if(paymentMethodeModel.getErrorMessage()!=null){
                                onFailed(paymentMethodeModel.getErrorMessage());
                            }
                            else {

                                paymentMethodeModelGlobal = paymentMethodeModel;
                                paymentGlobal = payment;
                                setCustomeList(2);
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

    @Override
    public void setOnCLickAsset(AssetModel asset, int pos) {

        assetView.showProgressBar();
        AssetModel.AssetModelSatuan assetModelSatuan = new AssetModel().new AssetModelSatuan();
        assetModelSatuan = asset.getAssetModelSatuanList().get(pos);

        long price = assetModelSatuan.getAssetPrice();
        int percent = assetModelSatuan.getAssetPercent();
        long curPrice = 0;

        String date = assetModelSatuan.getAssetDate();
        String[] split = date.trim().split("-");
        date = split[2];

        DateAndTime dateAndTime = new DateAndTime();
        String currentDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT).substring(0,4);

        if(currentDate.equalsIgnoreCase(date)){
            curPrice = price;
        }
        else {
            int tempHasil = 0,tempDate = 0, tempCurDate = 0;

            try {
                tempDate = Integer.parseInt(date);
                tempCurDate = Integer.parseInt(currentDate);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            tempHasil = tempCurDate - tempDate;

            for(int i =0;i<tempHasil;i++){

                price = price - ((price*percent)/100);
            }
            curPrice = price;
        }

        assetView.hideProgressBar();
        assetView.showAssetDetail(assetModelSatuan,pos,curPrice);
    }

    @Override
    public void addAsset(final AssetModel.AssetModelSatuan assetModelSatuan, String tipeBayar) {

        assetView.showProgressBar();
        Log.e("add",new Gson().toJson(assetModelSatuan));
        if(!isEmptyAdd(assetModelSatuan,tipeBayar)) {
            DateAndTime dateAndTime = new DateAndTime();
            String assetID = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT) + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
            String time = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
            Log.e("add",time);
            String balanceID = assetModelSatuan.getAssetID().substring(0,6);
            Log.e("add",balanceID);
            String userEmail = "";
            if(DataManager.can().getUserInfoFromStorage()!=null){
                if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                    userEmail = DataManager.can().getUserInfoFromStorage().getUserEmail();
                }
            }
            Log.e("add",userEmail);
            Log.e("add",tipeBayar);
            Log.e("add",assetID);

            AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                    .addBodyParameter("add", "select")
                    .addBodyParameter("asset_id", assetID)
                    .addBodyParameter("asset_name", assetModelSatuan.getAssetName())
                    .addBodyParameter("asset_price", assetModelSatuan.getAssetPrice() + "")
                    .addBodyParameter("asset_date", assetModelSatuan.getAssetDate())
                    .addBodyParameter("asset_depreciation_per_year", assetModelSatuan.getAssetPercent() + "")
                    .addBodyParameter("trans_time", time)
                    .addBodyParameter("trans_tipe_pembayaran", tipeBayar)
                    .addBodyParameter("trans_user_email", userEmail)
                    .addBodyParameter("balance_id",balanceID)
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

                                assetView.hideProgressBar();
                                assetView.showSuccessAdd(updateResponseModel.getSuccessMessage(),assetModelSatuan);
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

    @Override
    public void sellAsset(String assetID, long transPrice, String tipeBayar, final int pos) {

        assetView.showProgressBar();
        if(!isEmptySell(assetID,tipeBayar,transPrice)) {
            DateAndTime dateAndTime = new DateAndTime();
            String time = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
            String date = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
            String balanceID = assetID.substring(0,6);

            String timeSort = dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
            String dateSort = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT);
            String transID = dateSort + timeSort;
            String userEmail = "";
            if(DataManager.can().getUserInfoFromStorage()!=null){
                if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                    userEmail = DataManager.can().getUserInfoFromStorage().getUserEmail();
                }
            }
            Log.e("sell",time);

            AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                    .addBodyParameter("sell", "select")
                    .addBodyParameter("asset_id", assetID)
                    .addBodyParameter("trans_price", transPrice+"")
                    .addBodyParameter("trans_date", date)
                    .addBodyParameter("trans_time", time)
                    .addBodyParameter("trans_tipe_pembayaran", tipeBayar)
                    .addBodyParameter("trans_user_email", userEmail)
                    .addBodyParameter("trans_id", transID)
                    .addBodyParameter("balance_id", balanceID)
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

                                assetView.hideProgressBar();
                                assetView.showSuccessSell(updateResponseModel.getSuccessMessage(), pos);
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

    @Override
    public void editAsset(final AssetModel.AssetModelSatuan assetModelSatuan, final int pos) {

        assetView.showProgressBar();
        if(!isEmptyEdit(assetModelSatuan)) {
            AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                    .addBodyParameter("edit", "select")
                    .addBodyParameter("asset_id", assetModelSatuan.getAssetID())
                    .addBodyParameter("asset_name", assetModelSatuan.getAssetName())
                    .addBodyParameter("asset_price", assetModelSatuan.getAssetPrice() + "")
                    .addBodyParameter("asset_depreciation_per_year", assetModelSatuan.getAssetPercent() + "")
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

                                assetView.hideProgressBar();
                                assetView.showSuccessEdit(updateResponseModel.getSuccessMessage(),assetModelSatuan, pos);
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

    @Override
    public void deleteAsset(String assetID, final int pos) {

        assetView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_ASSET)
                .addBodyParameter("delete", "select")
                .addBodyParameter("asset_id", assetID)
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

                            assetView.hideProgressBar();
                            assetView.showSuccessDelete(updateResponseModel.getSuccessMessage(),pos);
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

        assetView.hideProgressBar();
        assetView.onFailed(message);
    }

    private void onFailed(String message, boolean fromCustomeList){

        assetView.hideProgressBar();
        assetView.onFailed(message,fromCustomeList);
    }

    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (assetModelGlobal.getAssetModelSatuanList() != null) {
                sizeArray = assetModelGlobal.getAssetModelSatuanList().size();
                if (sizeArray > 0) {

                    pos = findPosition(1);
                    assetView.hideProgressBar();
                    assetView.showAssetList(assetModelGlobal, pos);
                }
            }
        }
        else if(tipe==2) {
            if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList() != null) {
                sizeArray = paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size();
                if (sizeArray > 0) {

                    pos = findPosition(2);
                    assetView.hideProgressBar();
                    assetView.showPaymentList(paymentMethodeModelGlobal, pos);
                }
            }
        }

    }

    private int findPosition(int tipe) {

        int tempPosition = -1;

        if(tipe==1) {
            if (!assetGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < assetModelGlobal.getAssetModelSatuanList().size(); i++) {
                    if (assetModelGlobal.getAssetModelSatuanList().get(i).getAssetName().equalsIgnoreCase(assetGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }
        else if(tipe==2) {
            if (!paymentGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size(); i++) {
                    if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList().get(i).getPaymentMethodeID().equalsIgnoreCase(paymentGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }

    private boolean isEmptyEdit(AssetModel.AssetModelSatuan assetModelSatuan){

        if(assetModelSatuan.getAssetID().equalsIgnoreCase("")){
            onFailed("ID cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetName().equalsIgnoreCase("")){
            onFailed("Asset name cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetPercent()==0){
            onFailed("Asset percent cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetPercent()>=100){
            onFailed("Asset percent cannot be greater or equal 100");
            return true;
        }

        else if(assetModelSatuan.getAssetPrice()==0){
            onFailed("Asset price cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEmptyAdd(AssetModel.AssetModelSatuan assetModelSatuan, String tipeBayar){

        if(assetModelSatuan.getAssetID().equalsIgnoreCase("")){
            onFailed("ID cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetName().equalsIgnoreCase("")){
            onFailed("Asset name cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetDate().equalsIgnoreCase("")){
            onFailed("Asset date cannot be empty");
            return true;
        }
        else if(tipeBayar.equalsIgnoreCase("")){
            onFailed("Payment method cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetPercent()==0){
            onFailed("Asset percent cannot be empty");
            return true;
        }
        else if(assetModelSatuan.getAssetPercent()>=100){
            onFailed("Asset percent cannot be greater or equal 100");
            return true;
        }

        else if(assetModelSatuan.getAssetPrice()==0){
            onFailed("Asset price cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEmptySell(String assetID, String tipeBayar,long price){

        if(assetID.equalsIgnoreCase("")){
            onFailed("ID cannot be empty");
            return true;
        }
        else if(tipeBayar.equalsIgnoreCase("")){
            onFailed("Payment method cannot be empty");
            return true;
        }

        else if(price==0){
            onFailed("Asset price cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }
}
