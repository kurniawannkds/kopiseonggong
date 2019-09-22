package com.androiddevnkds.kopiseong.module.resep;

import android.util.Log;
import android.view.View;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.HPPModel;
import com.androiddevnkds.kopiseong.model.ResepItemModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ResepPresenter implements ResepContract.resepPresenter {

    private ResepContract.resepView resepView;
    private int sizeArray = 0, selectedPos = -1;

    public ResepPresenter(ResepContract.resepView  resepView){
        this.resepView = resepView;
    }

    @Override
    public void getAllResep() {

        resepView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_RESEP)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResepModel.class, new ParsedRequestListener<ResepModel>() {
                    @Override
                    public void onResponse(ResepModel resepModel) {
                        // do anything with response
                        if(resepModel.getErrorMessage()!=null){
                            onFailed(1,resepModel.getErrorMessage());
                        }
                        else {

                            resepView.hideProgressBar();
                            resepView.showAllResep(resepModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed(1,"ERROR");
                        Log.e("base",anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void updateResep(ResepModel.ResepModelSatuan resepModel) {

        Log.e("adapter", new Gson().toJson(resepModel));
        resepView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_RESEP)
                .addBodyParameter("change_resep_id", resepModel.getResepID())
                .addBodyParameter("new_item", resepModel.getResepItem())
                .addBodyParameter("new_jumlah_item", resepModel.getResepJumlahItem())
                .addBodyParameter("new_price", resepModel.getResepTotalPrice()+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                    @Override
                    public void onResponse(UpdateResponseModel updateResponseModel) {
                        // do anything with response
                        if(updateResponseModel.getErrorMessage()!=null){
                            onFailed(2,updateResponseModel.getErrorMessage());
                            Log.e("adapter", "FAILED");
                        }
                        else {

                            resepView.hideProgressBar();
                            resepView.showSuccessUpdate(updateResponseModel.getSuccessMessage());
                            Log.e("adapter","SUKSES");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed(2,"ERROR");
                        Log.e("base",anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void setOnClickResep(ResepModel resepModel, int position) {

        resepView.showProgressBar();
        ResepModel.ResepModelSatuan resepModelSatuan = new ResepModel().new ResepModelSatuan();
        resepModelSatuan = resepModel.getResepModelSatuanList().get(position);

        List<ResepItemModel> resepItemModelList = new ArrayList<>();
        ResepItemModel resepItemModel = new ResepItemModel();

        String[] splitItem = resepModelSatuan.getResepItem().trim().split(",");
        String[] splitJumlah = resepModelSatuan.getResepJumlahItem().trim().split(",");

        for(int i=0;i<splitItem.length;i++){
            resepItemModel.setItemName(splitItem[i]);
            resepItemModel.setItemJumlah(splitJumlah[i]);
            resepItemModelList.add(resepItemModel);
            resepItemModel = new ResepItemModel();
        }

        resepView.hideProgressBar();
        resepView.showResepSatuan(resepModelSatuan,resepItemModelList);
    }

    @Override
    public void setOnClickEditResep(List<ResepItemModel> resepItemModel, int position) {
        Log.e("adapter", "masuk presenter ga");
        ResepItemModel resepItemModelSatuan = resepItemModel.get(position);
        resepView.showEditDialog(resepItemModelSatuan);
    }

    @Override
    public void onFailed(int tipe, String message) {

        resepView.hideProgressBar();
        resepView.onFailed(tipe,message);
    }

    @Override
    public void getDataAfterEdit(ResepItemModel resepItemModel, int position) {

        resepView.updateResepItem(resepItemModel,position);
    }

    @Override
    public void deleteResep(String resepID, final int position) {

        resepView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_RESEP)
                .addBodyParameter("resep_id", resepID)
                .addBodyParameter("delete_resep", "delete")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                    @Override
                    public void onResponse(UpdateResponseModel updateResponseModel) {
                        // do anything with response
                        if(updateResponseModel.getErrorMessage()!=null){
                            onFailed(3,updateResponseModel.getErrorMessage());
                            Log.e("adapter", "FAILED");
                        }
                        else {

                            resepView.hideProgressBar();
                            resepView.succesDeleteResep(updateResponseModel.getSuccessMessage(), position);
                            Log.e("adapter","SUKSES");
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed(3,"ERROR");
                        Log.e("base",anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void addResep(final ResepModel.ResepModelSatuan resepModelSatuan) {

        if(hasError(resepModelSatuan)){

        }
        else {
            Log.e("base",new Gson().toJson(resepModelSatuan));
            AndroidNetworking.post(K.URL_GET_ALL_RESEP)
                    .addBodyParameter("resep_id", resepModelSatuan.getResepID())
                    .addBodyParameter("resep_item", resepModelSatuan.getResepItem())
                    .addBodyParameter("resep_jumlah_item_1", resepModelSatuan.getResepJumlahItem())
                    .addBodyParameter("resep_total_price", resepModelSatuan.getResepTotalPrice()+"")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            if(updateResponseModel.getErrorMessage()!=null){
                                onFailed(2,updateResponseModel.getErrorMessage());
                                Log.e("adapter", "FAILED");
                            }
                            else {

                                resepView.hideProgressBar();
                                resepView.successAddResep(updateResponseModel.getSuccessMessage(),resepModelSatuan);
                                Log.e("adapter","SUKSES");
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(2,"ERROR");
                            Log.e("base",anError.getErrorDetail());
                        }
                    });
        }

    }

    @Override
    public void getCountHPP(final String item, final String jumlahItem, final int tipe) {

        String[] splitItem = item.trim().split(",");
        String[] splitJumlah = jumlahItem.trim().split(",");

        if(splitItem.length ==  splitJumlah.length) {
            AndroidNetworking.post(K.URL_GET_COUNT_HPP)
                    .addBodyParameter("resep_item", item)
                    .addBodyParameter("resep_jumlah", jumlahItem)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(HPPModel.class, new ParsedRequestListener<HPPModel>() {
                        @Override
                        public void onResponse(HPPModel hppModel) {
                            // do anything with response
                            if (hppModel.getErrorMessage() != null) {
                                onFailed(3, hppModel.getErrorMessage());
                                Log.e("adapter", "FAILED");
                            } else {

                                resepView.hideProgressBar();
                                resepView.setHPP(hppModel.getHpp(), tipe, item, jumlahItem);
                                Log.e("adapter", "SUKSES");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(3, "ERROR");
                            Log.e("base", anError.getErrorDetail());
                        }
                    });
        }
        else {

            onFailed(3,"Item and jumlah item not same");
        }

    }

    @Override
    public void getAllStock(final String stock) {

        resepView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_STORE)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(StockModel.class, new ParsedRequestListener<StockModel>() {
                    @Override
                    public void onResponse(StockModel stockModel) {
                        // do anything with response
                        if(stockModel.getErrorMessage()!=null){
                            onFailed(4,stockModel.getErrorMessage());
                        }
                        else {

                            setCustomeList(stockModel, stock);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed(4,"ERROR");
                        Log.e("base",anError.getErrorDetail());
                    }
                });
    }

    private boolean hasError(ResepModel.ResepModelSatuan resepModelSatuan){

        String id = resepModelSatuan.getResepID();
        String item = resepModelSatuan.getResepItem();
        String jumlah = resepModelSatuan.getResepJumlahItem();
        String[] splitItem = item.trim().split(",");
        String[] splitJumlah = jumlah.trim().split(",");
        double hpp = resepModelSatuan.getResepTotalPrice();
        if(id.equalsIgnoreCase("")){
            onFailed(2,"ID cannot be empty");
            return true;
        }
        else if(item.equalsIgnoreCase("")){
            onFailed(2,"Item cannot be empty");
            return true;
        }
        else if(jumlah.equalsIgnoreCase("")){
            onFailed(2,"Jumlah cannot be empty");
            return true;
        }
        else  if(splitItem.length != splitJumlah.length){

            onFailed(2,"Item and jumlah item not same");
            return  true;
        }
        else if(hpp == 0){
            onFailed(2,"Hpp cannot be zero");
            return true;
        }
        else {
            return false;
        }
    }

    private void setCustomeList(StockModel stockModel, String stock){

        if (stockModel.getStockSatuanModelList() != null) {
            sizeArray = stockModel.getStockSatuanModelList().size();
            if (sizeArray > 0) {

                selectedPos = findPosition(stock, stockModel);
                resepView.hideProgressBar();
                resepView.showAllStock(stockModel,selectedPos);
            }
        }
    }

    private int findPosition(String kata, StockModel stockModel) {

        int tempPosition = -1;

        if (!kata.equalsIgnoreCase("")) {
            //category

                for (int i = 0; i < stockModel.getStockSatuanModelList().size(); i++) {
                    if (stockModel.getStockSatuanModelList().get(i).getStockID().equalsIgnoreCase(kata)) {
                        tempPosition = i;
                        break;
                    }
                }
            }

        return tempPosition;
    }
}
