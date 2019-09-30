package com.androiddevnkds.kopiseong.module.addTransaction;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.HPPModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.text.DateFormat;

public class AddTransactionPresenter implements AddTransactionContract.addTransactionPresenter{

    private AddTransactionContract.addTransactionView addTransactionView;
    private int sizeArray = 0, selectedPos = 0;
    private CategoryModel categoryModelGlobal;
    private String categoryGlobal ="";

    private ProductModel productModelGlobal;
    private String productGlobal ="";

    private PaymentMethodeModel paymentMethodeModelGlobal;
    private String paymentGlobal = "";

    public AddTransactionPresenter(AddTransactionContract.addTransactionView addTransactionView){
        this.addTransactionView = addTransactionView;
    }

    @Override
    public void getCategoryList(CategoryModel categoryModelT, final String category) {

        boolean flag = false;

        if(categoryModelT!=null) {
            if(categoryModelT.getCategorySatuanList()!=null){
                if(categoryModelT.getCategorySatuanList().size()>0){

                    categoryModelGlobal = categoryModelT;
                    categoryGlobal = category;
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
        if(flag){
            String userRole = "";
            if(DataManager.can().getUserInfoFromStorage()!=null){
                if(DataManager.can().getUserInfoFromStorage().getUserRole()!=null){
                    userRole = DataManager.can().getUserInfoFromStorage().getUserRole();
                }
            }
            //dummy
            userRole = "Master";
            AndroidNetworking.post(K.URL_GET_CATEGORY_TRANSACTION)
                    .addBodyParameter("user_role",userRole)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(CategoryModel.class, new ParsedRequestListener<CategoryModel>() {
                        @Override
                        public void onResponse(CategoryModel categoryModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(categoryModel));
                            if(categoryModel.getErrorMessage()!=null){
                                onFailed(categoryModel.getErrorMessage());
                            }
                            else {

                                categoryModelGlobal = categoryModel;
                                categoryGlobal = category;
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
    public void getDateTimeToday(String dateFormatString, String dateFormatSort,String timeFormatString, String timeFormatSort) {

        DateAndTime dateAndTime = new DateAndTime();
        String dateString = dateAndTime.getCurrentDate(dateFormatString);
        String dateSort = dateAndTime.getCurrentDate(dateFormatSort);
        String timeString = dateAndTime.getCurrentTime(timeFormatString);
        String timeSort = dateAndTime.getCurrentTime(timeFormatSort);

        String transID = dateSort+timeSort;

        addTransactionView.showDateTimeToday(dateString,timeString,transID);
    }

    @Override
    public void getProductList(final ProductModel productModelT, final String product) {

        boolean flag = false;

        if(productModelT!=null) {
            if(productModelT.getProductSatuanList()!=null){
                if(productModelT.getProductSatuanList().size()>0){

                    productModelGlobal = productModelT;
                    productGlobal = product;
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

            AndroidNetworking.post(K.URL_GET_PRODUCT)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(ProductModel.class, new ParsedRequestListener<ProductModel>() {
                        @Override
                        public void onResponse(ProductModel productModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(productModel));
                            if(productModel.getErrorMessage()!=null){
                                onFailed(productModel.getErrorMessage());
                            }
                            else {

                                productModelGlobal = productModel;
                                productGlobal = product;
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
    public void getPaymentList(PaymentMethodeModel paymentMethodeModelT, final String payment) {

        boolean flag = false;
        if(paymentMethodeModelT!=null) {
            if(paymentMethodeModelT.getPaymentMethodeSatuanList()!=null){
                if(paymentMethodeModelT.getPaymentMethodeSatuanList().size()>0){

                    paymentMethodeModelGlobal = paymentMethodeModelT;
                    paymentGlobal = payment;
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
                                setCustomeList(3);
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
    public void addTransaction(final TransactionSatuanModel transactionSatuanModel, final String generalCategory, final String detailProduct, final String detailJumlah, final String detailResep) {

        addTransactionView.showProgressBar();

        String[] splitItem = detailProduct.trim().split(",");
        String[] splitJumlah = detailJumlah.trim().split(",");
        String[] splitResep = detailResep.trim().split(",");
        if(!detailResep.equalsIgnoreCase("") && !detailJumlah.equalsIgnoreCase("")&&
                !detailProduct.equalsIgnoreCase("") && splitItem.length ==  splitJumlah.length &&
                splitItem.length == splitResep.length) {
            AndroidNetworking.post(K.URL_GET_COUNT_HPP)
                    .addBodyParameter("resep_id", detailResep)
                    .addBodyParameter("resep_jumlah", detailJumlah)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(HPPModel.class, new ParsedRequestListener<HPPModel>() {
                        @Override
                        public void onResponse(HPPModel hppModel) {
                            // do anything with response
                            if (hppModel.getErrorMessage() != null) {
                                onFailed(hppModel.getErrorMessage());
                                Log.e("adapter", "FAILED");
                            } else {

                                if(!isEmpty(transactionSatuanModel, generalCategory,  detailProduct,  detailJumlah,  detailResep,hppModel.getHpp())){

                                    hitApiAddTransaction( transactionSatuanModel, generalCategory,  detailProduct,  detailJumlah,  detailResep,hppModel.getHpp());
                                }

                                Log.e("adapter", "SUKSES");
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed("ERROR");
                            Log.e("base", anError.getErrorDetail());
                        }
                    });
        }
        else {
            onFailed("Resep and Total not match or empty");
        }
    }

    private void onFailed(String message){

        addTransactionView.hideProgressBar();
        addTransactionView.onFailedGetAllAPI(message);
    }

    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (categoryModelGlobal.getCategorySatuanList() != null) {
                sizeArray = categoryModelGlobal.getCategorySatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(1);
                    addTransactionView.hideProgressBar();
                    addTransactionView.showCategoryList(categoryModelGlobal, selectedPos);
                }
            }
        }
        else if(tipe==2){

            if (productModelGlobal.getProductSatuanList() != null) {
                sizeArray = productModelGlobal.getProductSatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(2);
                    addTransactionView.hideProgressBar();
                    addTransactionView.showProductList(productModelGlobal, selectedPos);
                }
            }
        }

        else if(tipe==3){

            if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList() != null) {
                sizeArray = paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(2);
                    addTransactionView.hideProgressBar();
                    addTransactionView.showPaymentList(paymentMethodeModelGlobal, selectedPos);
                }
            }
        }
    }

    private int findPosition(int tipe) {

        int tempPosition = -1;

        if(tipe==1) {
            if (!categoryGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < categoryModelGlobal.getCategorySatuanList().size(); i++) {
                    if (categoryModelGlobal.getCategorySatuanList().get(i).getCategoryID().equalsIgnoreCase(categoryGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }
        else if(tipe==2){

            if (!productGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < productModelGlobal.getProductSatuanList().size(); i++) {
                    if (productModelGlobal.getProductSatuanList().get(i).getProductID().equalsIgnoreCase(productGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        else if(tipe==3){

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

    private void hitApiAddTransaction(TransactionSatuanModel transactionSatuanModel,String generalCat, String detailProduct, String detailJumlah, String detailResep, long hpp){

        String balanceID = transactionSatuanModel.getTransactionID().substring(0,6);
        AndroidNetworking.post(K.URL_ADD_TRANSACTION)
                .addBodyParameter("income","income")
                .addBodyParameter("trans_id",transactionSatuanModel.getTransactionID())
                .addBodyParameter("trans_category",transactionSatuanModel.getTransactionCategory())
                .addBodyParameter("trans_date",transactionSatuanModel.getTransactionDate())
                .addBodyParameter("trans_time",transactionSatuanModel.getTransactionTime())
                .addBodyParameter("trans_price",transactionSatuanModel.getTransactionBalance()+"")
                .addBodyParameter("trans_user_email",transactionSatuanModel.getUserEmail())
                .addBodyParameter("category_general",generalCat)
                .addBodyParameter("trans_tipe_pembayaran",transactionSatuanModel.getTipePembayaran())
                .addBodyParameter("trans_detail_produk",detailProduct)
                .addBodyParameter("trans_detail_jumlah",detailJumlah)
                .addBodyParameter("trans_detail_resep",detailResep)
                .addBodyParameter("hpp",hpp+"")
                .addBodyParameter("total_balance_id",balanceID)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UpdateResponseModel.class, new ParsedRequestListener<UpdateResponseModel>() {
                    @Override
                    public void onResponse(UpdateResponseModel updateResponseModel) {
                        // do anything with response

                        if(updateResponseModel.getErrorMessage()!=null){
                            onFailed(updateResponseModel.getErrorMessage());
                        }
                        else {
                            addTransactionView.hideProgressBar();
                            addTransactionView.showSuccessAddTransaction(updateResponseModel.getSuccessMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed("ERROR");
                        Log.e("ERROR",anError.getErrorDetail());

                    }
                });
    }

    private boolean isEmpty(TransactionSatuanModel transactionSatuanModel,String generalCat, String detailProduct, String detailJumlah, String detailResep, long hpp){

        String transID = transactionSatuanModel.getTransactionID();
        String tipeBayar = transactionSatuanModel.getTipePembayaran();
        long price = transactionSatuanModel.getTransactionBalance();
        String time = transactionSatuanModel.getTransactionTime();
        String date = transactionSatuanModel.getTransactionDate();
        String mCategory = transactionSatuanModel.getTransactionCategory();

        if(transID.equalsIgnoreCase("")){

            onFailed("ID cannot be empty");
            return  true;
        }
        else if(tipeBayar.equalsIgnoreCase("")){
            onFailed("Payment method cannot be empty");
            return true;
        }
        else if(time.equalsIgnoreCase("")){
            onFailed("Time cannot be empty");
            return true;
        }
        else if(date.equalsIgnoreCase("")){
            onFailed("Date cannot be empty");
            return true;
        }
        else if(mCategory.equalsIgnoreCase("")){
            onFailed("Category cannot be empty");
            return true;
        }
        else if(generalCat.equalsIgnoreCase("")){
            onFailed("Category cannot be empty");
            return true;
        }
        else if(detailProduct.equalsIgnoreCase("")){
            onFailed("Product cannot be empty");
            return true;
        }
        else if(detailJumlah.equalsIgnoreCase("")){
            onFailed("Jumlah cannot be empty");
            return true;
        }
        else if(detailResep.equalsIgnoreCase("")){
            onFailed("Recipe cannot be empty");
            return true;
        }
        else if(price==0){
            onFailed("Price cannot be zero");
            return true;
        }
        else if(hpp==0){
            onFailed("Hpp cannot be zero");
            return true;
        }
        else {
            return  false;
        }
    }
}
