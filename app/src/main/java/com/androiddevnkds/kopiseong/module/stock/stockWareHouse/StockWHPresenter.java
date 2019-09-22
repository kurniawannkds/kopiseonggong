package com.androiddevnkds.kopiseong.module.stock.stockWareHouse;

import android.util.Log;

import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

public class StockWHPresenter implements StockWHContract.stockPresenter {

    private StockWHContract.stockView stockView;

    public StockWHPresenter(StockWHContract.stockView stockView){
        this.stockView = stockView;
    }

    @Override
    public void getAllStock() {

        stockView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(StockModel.class, new ParsedRequestListener<StockModel>() {
                    @Override
                    public void onResponse(StockModel stockModel) {
                        // do anything with response
                        if(stockModel.getErrorMessage()!=null){
                            onFailed(1,stockModel.getErrorMessage());
                        }
                        else {

                            stockView.hideProgressBar();
                            stockView.showAllStock(stockModel);
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
    public void setOnClickStock(StockModel stockModel, int position) {

        StockModel.StockSatuanModel stockSatuanModel = stockModel.getStockSatuanModelList().get(position);
        stockView.showStockDetail(stockSatuanModel,position);
    }

    @Override
    public void deleteStock(String stockID, String dateSort) {

        stockView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                .setTag("test")
                .addBodyParameter("stock_action","delete")
                .addBodyParameter("stock_date_sort",dateSort)
                .addBodyParameter("stock_id",stockID)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                    @Override
                    public void onResponse(UpdateResponseModel updateResponseModel) {
                        // do anything with response
                        if(updateResponseModel.getErrorMessage()!=null){
                            onFailed(2,updateResponseModel.getErrorMessage());
                        }
                        else {

                            stockView.hideProgressBar();
                            stockView.showSuccessDelete(1,updateResponseModel.getSuccessMessage());
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

    private void onFailed(int tipe, String message){

        stockView.hideProgressBar();
        stockView.onFailed(tipe,message);
    }
}
