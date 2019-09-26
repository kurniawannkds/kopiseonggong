package com.androiddevnkds.kopiseong.module.stock.stockStore;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.module.stock.stockWareHouse.StockWHContract;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

public class StockStorePresenter implements StockStoreContract.stockPresenter {

    private StockStoreContract.stockView stockView;

    public StockStorePresenter(StockStoreContract.stockView stockView){
        this.stockView = stockView;
    }

    @Override
    public void getAllStock() {

        stockView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_STORE)
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
    public void deleteStock(String stockID, String dateSort, final int pos) {

        stockView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_STORE)
                .setTag("test")
                .addBodyParameter("stock_delete","delete")
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
                            stockView.showSuccessDelete(pos,updateResponseModel.getSuccessMessage());
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
    public void editStock(final StockModel.StockSatuanModel stockSatuanModel, final int position) {

        stockView.showProgressBar();
        if(!validateEdit(stockSatuanModel)){
            stockView.showProgressBar();
            AndroidNetworking.post(K.URL_GET_STOCK_STORE)
                    .setTag("test")
                    .addBodyParameter("stock_update","update")
                    .addBodyParameter("stock_date_sort",stockSatuanModel.getStockDateSort())
                    .addBodyParameter("stock_id",stockSatuanModel.getStockID())
                    .addBodyParameter("stock_price",stockSatuanModel.getStockPrice()+"")
                    .addBodyParameter("stock_date",stockSatuanModel.getStockDate())
                    .addBodyParameter("stock_gram",stockSatuanModel.getStockGram()+"")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            if(updateResponseModel.getErrorMessage()!=null){
                                onFailed(3,updateResponseModel.getErrorMessage());
                            }
                            else {

                                stockView.hideProgressBar();
                                stockView.showSuccessEditStock(updateResponseModel.getSuccessMessage(),stockSatuanModel,position);
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
    }


    private void onFailed(int tipe, String message){

        stockView.hideProgressBar();
        stockView.onFailed(tipe,message);
    }

    private boolean validateEdit(StockModel.StockSatuanModel stockSatuanModel){

        long stockJumlah = stockSatuanModel.getStockGram();
        long stockPrice = stockSatuanModel.getStockPrice();
        String stockDate = stockSatuanModel.getStockDate();

        if(stockJumlah==0){

            onFailed(3,"Stock gram cannot be empty");
            return true;
        }
        else if(stockPrice == 0){

            onFailed(3,"Price cannot be empty");
            return true;
        }
        else if(stockDate.equalsIgnoreCase("")){
            onFailed(3,"Stock date cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }
}
