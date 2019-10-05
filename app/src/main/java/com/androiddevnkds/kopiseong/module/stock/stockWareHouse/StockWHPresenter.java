package com.androiddevnkds.kopiseong.module.stock.stockWareHouse;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

public class StockWHPresenter implements StockWHContract.stockPresenter {

    private StockWHContract.stockView stockView;
    private int selectedPos = -1, sizeArray = 0;
    private StockModel stockModel;
    private String stock = "";
    private PaymentMethodeModel paymentMethodeModelGlobal;
    private String payment = "";

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
    public void deleteStock(String stockID, String dateSort, final int pos) {

        stockView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
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
            AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
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

    @Override
    public void setStockList(StockModel stockList,String stock) {

        stockModel = stockList;
        this.stock = stock;
        setCustomeList(1);
    }

    @Override
    public void setStockListForAdd(final StockModel stockListT, final String stockT) {
        boolean flag = false;
        if(stockListT!=null) {
            if(stockListT.getStockSatuanModelList()!=null){
                if(stockListT.getStockSatuanModelList().size()>0){

                    stockModel = stockListT;
                    this.stock = stockT;
                    setCustomeList(3);
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
            AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                    .addBodyParameter("select_custome_list","select")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(StockModel.class, new ParsedRequestListener<StockModel>() {
                        @Override
                        public void onResponse(StockModel stockModelS) {
                            // do anything with response
                            if(stockModelS.getErrorMessage()!=null){
                                onFailed(1,stockModelS.getErrorMessage());
                            }
                            else {

                                stockModel = stockModelS;
                                stock = stockT;
                                setCustomeList(3);
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
    }

    @Override
    public void addNewStock(final StockModel.StockSatuanModel stockSatuanModel, String payment, String category, String time) {

        stockView.showProgressBar();

        if(!validateAdd(stockSatuanModel, payment)){
            String userEmail = "";
            if(DataManager.can().getUserInfoFromStorage()!=null){
                if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                    userEmail = DataManager.can().getUserInfoFromStorage().getUserEmail();
                }
            }
            String balanceID = stockSatuanModel.getStockDateSort().substring(0,6);
            AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                    .setTag("test")
                    .addBodyParameter("stock_add","update")
                    .addBodyParameter("stock_date_sort",stockSatuanModel.getStockDateSort())
                    .addBodyParameter("stock_id",stockSatuanModel.getStockID())
                    .addBodyParameter("stock_name",stockSatuanModel.getStockName())
                    .addBodyParameter("stock_price",stockSatuanModel.getStockPrice()+"")
                    .addBodyParameter("stock_date",stockSatuanModel.getStockDate())
                    .addBodyParameter("stock_gram",stockSatuanModel.getStockGram()+"")
                    .addBodyParameter("trans_category",category)
                    .addBodyParameter("trans_time",time)
                    .addBodyParameter("trans_tipe_pembayaran",payment)
                    .addBodyParameter("trans_user_email",userEmail)
                    .addBodyParameter("balance_id",balanceID)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            Log.e("stcok",new Gson().toJson(updateResponseModel));
                            if(updateResponseModel.getErrorMessage()!=null){
                                onFailed(4,updateResponseModel.getErrorMessage());
                            }
                            else {

                                stockView.hideProgressBar();
                                stockView.showSuccessAddStock(updateResponseModel.getSuccessMessage(),stockSatuanModel);
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
    }

    @Override
    public void sellStock(StockModel.StockSatuanModel stockSatuanModel, String transID, String payment, String category, String time, long gram, final int pos) {

        stockView.showProgressBar();
        if(!validateSell(stockSatuanModel,transID,payment,gram)){
            String userEmail = "";
            if(DataManager.can().getUserInfoFromStorage()!=null){
                if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                    userEmail = DataManager.can().getUserInfoFromStorage().getUserEmail();
                }
            }
            String balanceID = stockSatuanModel.getStockDateSort().substring(0,6);
            AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                    .setTag("test")
                    .addBodyParameter("stock_sell","update")
                    .addBodyParameter("stock_date_sort",stockSatuanModel.getStockDateSort())
                    .addBodyParameter("stock_id",stockSatuanModel.getStockID())
                    .addBodyParameter("stock_price",stockSatuanModel.getStockPrice()+"")
                    .addBodyParameter("stock_date",stockSatuanModel.getStockDate())
                    .addBodyParameter("stock_gram",gram+"")
                    .addBodyParameter("stock_price_per_gram",stockSatuanModel.getStockPricePerGram()+"")
                    .addBodyParameter("trans_category",category)
                    .addBodyParameter("trans_time",time)
                    .addBodyParameter("trans_tipe_pembayaran",payment)
                    .addBodyParameter("trans_user_email",userEmail)
                    .addBodyParameter("trans_id",transID)
                    .addBodyParameter("balance_id",balanceID)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            if(updateResponseModel.getErrorMessage()!=null){
                                onFailed(6,updateResponseModel.getErrorMessage());
                            }
                            else {

                                stockView.hideProgressBar();
                                stockView.showSuccessSellStock(updateResponseModel.getSuccessMessage(),pos);
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(6,"ERROR");
                            Log.e("base",anError.getErrorDetail());
                        }
                    });
        }
    }

    @Override
    public void sentStock(StockModel.StockSatuanModel stockSatuanModel, long gram, final int pos) {

        stockView.showProgressBar();
        if(!validateSent(stockSatuanModel,gram)) {

            AndroidNetworking.post(K.URL_GET_STOCK_WAREHOUSE)
                    .setTag("test")
                    .addBodyParameter("stock_add_to_store", "update")
                    .addBodyParameter("stock_date_sort", stockSatuanModel.getStockDateSort())
                    .addBodyParameter("stock_id", stockSatuanModel.getStockID())
                    .addBodyParameter("stock_price", stockSatuanModel.getStockPrice() + "")
                    .addBodyParameter("stock_date", stockSatuanModel.getStockDate())
                    .addBodyParameter("stock_name", stockSatuanModel.getStockName())
                    .addBodyParameter("stock_gram", gram + "")
                    .addBodyParameter("stock_price_per_gram", stockSatuanModel.getStockPricePerGram() + "")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                        @Override
                        public void onResponse(UpdateResponseModel updateResponseModel) {
                            // do anything with response
                            if (updateResponseModel.getErrorMessage() != null) {
                                onFailed(7, updateResponseModel.getErrorMessage());
                            } else {

                                stockView.hideProgressBar();
                                stockView.showSuccessSentStock(updateResponseModel.getSuccessMessage(), pos);
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(7, "ERROR");
                            Log.e("base", anError.getErrorDetail());
                        }
                    });
        }
    }

    @Override
    public void getAllPayment(final PaymentMethodeModel paymentMethodeModelT, final String paymentT) {

        boolean flag = false;
        if(paymentMethodeModelT!=null) {
            if(paymentMethodeModelT.getPaymentMethodeSatuanList()!=null){
                if(paymentMethodeModelT.getPaymentMethodeSatuanList().size()>0){

                    paymentMethodeModelGlobal = paymentMethodeModelT;
                    payment = paymentT;
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
                                onFailed(5,paymentMethodeModel.getErrorMessage());
                            }
                            else {

                                paymentMethodeModelGlobal = paymentMethodeModel;
                                payment = paymentT;
                                setCustomeList(2);
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(5,"ERROR");
                        }
                    });
        }
//        else {
//
//            //g usah hit
//
//            paymentMethodeModelGlobal = paymentMethodeModelT;
//            payment = paymentT;
//            setCustomeList(2);
//        }
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

    private boolean validateAdd(StockModel.StockSatuanModel stockSatuanModel, String payment){

        long stockJumlah = stockSatuanModel.getStockGram();
        long stockPrice = stockSatuanModel.getStockPrice();
        String stockDate = stockSatuanModel.getStockDate();
        String dateSort = stockSatuanModel.getStockDateSort();
        String stockId = stockSatuanModel.getStockID();
        String stockName = stockSatuanModel.getStockName();

        if(stockJumlah==0){

            onFailed(4,"Stock gram cannot be empty");
            return true;
        }
        else if(stockPrice == 0){

            onFailed(4,"Price cannot be empty");
            return true;
        }
        else if(stockDate.equalsIgnoreCase("")){
            onFailed(4,"Stock date cannot be empty");
            return true;
        }
        else if(dateSort.equalsIgnoreCase("")){
            onFailed(4,"Stock date cannot be empty");
            return true;
        }
        else if(stockId.equalsIgnoreCase("")){
            onFailed(4,"Stock ID cannot be empty");
            return true;
        }
        else if(stockName.equalsIgnoreCase("")){
            onFailed(4,"Stock name cannot be empty");
            return true;
        }
        else if(payment.equalsIgnoreCase("")){
            onFailed(4,"Stock payment cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }


    private boolean validateSell(StockModel.StockSatuanModel stockSatuanModel,String transID,String payment, long gramSell){

        long stockJumlah = stockSatuanModel.getStockGram();
        long stockPrice = stockSatuanModel.getStockPrice();
        String stockDate = stockSatuanModel.getStockDate();
        String dateSort = stockSatuanModel.getStockDateSort();
        String stockId = stockSatuanModel.getStockID();

        if(gramSell==0){

            onFailed(6,"Stock gram cannot be empty");
            return true;
        }
        else if(stockPrice == 0){

            onFailed(6,"Price cannot be empty");
            return true;
        }
        else if(stockDate.equalsIgnoreCase("")){
            onFailed(6,"Stock date cannot be empty");
            return true;
        }
        else if(dateSort.equalsIgnoreCase("")){
            onFailed(6,"Stock date cannot be empty");
            return true;
        }
        else if(stockId.equalsIgnoreCase("")){
            onFailed(6,"Stock ID cannot be empty");
            return true;
        }
        else if(payment.equalsIgnoreCase("")){
            onFailed(6,"Stock payment cannot be empty");
            return true;
        }
        else if(gramSell > stockJumlah){
            onFailed(6,"Gram sell bigger than current gram stock");
            return true;
        }
        else if(transID.equalsIgnoreCase("")){
            onFailed(6,"TransID cannot be empty");
            return true;
        }
        else if(stockSatuanModel.getStockPricePerGram() ==0){
            onFailed(6,"Price per gram cannot be zero");
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validateSent(StockModel.StockSatuanModel stockSatuanModel,long gramSell){

        long stockJumlah = stockSatuanModel.getStockGram();
        long stockPrice = stockSatuanModel.getStockPrice();
        String stockDate = stockSatuanModel.getStockDate();
        String dateSort = stockSatuanModel.getStockDateSort();
        String stockId = stockSatuanModel.getStockID();

        if(gramSell==0){

            onFailed(7,"Stock gram cannot be empty");
            return true;
        }
        else if(stockPrice == 0){

            onFailed(7,"Price cannot be empty");
            return true;
        }
        else if(stockDate.equalsIgnoreCase("")){
            onFailed(7,"Stock date cannot be empty");
            return true;
        }
        else if(dateSort.equalsIgnoreCase("")){
            onFailed(7,"Stock date cannot be empty");
            return true;
        }
        else if(stockId.equalsIgnoreCase("")){
            onFailed(7,"Stock ID cannot be empty");
            return true;
        }
        else if(gramSell > stockJumlah){
            onFailed(7,"Gram sent bigger than current gram stock");
            return true;
        }
        else if(stockSatuanModel.getStockPricePerGram() ==0){
            onFailed(7,"Price per gram cannot be zero");
            return true;
        }
        else {
            return false;
        }
    }

    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (stockModel.getStockSatuanModelList() != null) {
                sizeArray = stockModel.getStockSatuanModelList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(1);
                    stockView.hideProgressBar();
                    stockView.showStockCustomeList(stockModel, selectedPos);
                }
            }
        }
        else if(tipe==2){
            if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList() != null) {
                sizeArray = paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(2);
                    stockView.hideProgressBar();
                    stockView.showAllPayment(paymentMethodeModelGlobal, selectedPos);
                }
            }
        }
        else if(tipe==3) {
            if (stockModel.getStockSatuanModelList() != null) {
                sizeArray = stockModel.getStockSatuanModelList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(1);
                    stockView.hideProgressBar();
                    stockView.showStockCustomeListAdd(stockModel, selectedPos);
                }
            }
        }

    }

    private int findPosition(int tipe) {

        int tempPosition = -1;

        if(tipe==1) {
            if (!stock.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < stockModel.getStockSatuanModelList().size(); i++) {
                    if (stockModel.getStockSatuanModelList().get(i).getStockID().equalsIgnoreCase(stock)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }
        else {
            if (!payment.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size(); i++) {
                    if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList().get(i).getPaymentMethodeID().equalsIgnoreCase(payment)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }
}
