package com.androiddevnkds.kopiseong.module.transaction;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.GeneralAddResponseModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.List;

public class TransactionPresenter implements TransactionContract.transactionPresenter {

    private TransactionContract.transactionView transactionView;
    private String transID ="";
    private String transCat="";
    private String transDate="";
    private String transTime="";
    private String transUserName="";
    private String transEmail="";
    private long transBalance = 0;
    private String transUserEmailAdd = "";
    private String transDateAdd="",transTimeAdd="";

    private int transDateSortAdd = 0;

    private static String FORMAT_TANGGAL_STRING = "dd-MM-yyyy", FORMAT_TANGGAL_SORT = "yyyyMMdd";
    private static String FORMAT_TIME = "hh:mm:ss";

    public TransactionPresenter(TransactionContract.transactionView transactionView){
        this.transactionView = transactionView;
    }

    @Override
    public void getAllTransaction(int currentPage) {

        transactionView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_TRANSACTION)
                .addBodyParameter("currentPage",currentPage+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(TransactionModel.class, new ParsedRequestListener<TransactionModel>() {
                    @Override
                    public void onResponse(TransactionModel transactionModels) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(transactionModels));
                        if(transactionModels.getErrorMessage()!=null){
                            onFailed(transactionModels.getErrorMessage());
                        }
                        else {
                            onSuccess(transactionModels);
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

    @Override
    public void setOnClickTransaction(TransactionModel transactionModelGlobal, int position) {

        transactionView.showProgressBar();
        if (transactionModelGlobal.getTransactionModelLists() != null) {

            if (transactionModelGlobal.getTransactionModelLists().size() > 0) {

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionID() != null) {
                    transID = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionID();
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionCategory() != null) {
                    transCat = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionCategory();
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionDate() != null) {
                    transDate = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionDate();
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionTime() != null) {
                    transTime = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionTime();
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionBalance() != 0) {
                    transBalance = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionBalance();
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getUserEmail() != null) {
                    transEmail = transactionModelGlobal.getTransactionModelLists().get(position).getUserEmail();
                }
            }
        }

        if (!transID.equalsIgnoreCase("") ) {

            AndroidNetworking.post(K.URL_GET_DETAIL_TRANSACTION)
                    .addBodyParameter("transaction_id",transID)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(DetailTransactionModel.class, new ParsedRequestListener<DetailTransactionModel>() {
                        @Override
                        public void onResponse(DetailTransactionModel detailTransactionModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(detailTransactionModel));
                            if(detailTransactionModel.getErrorMessage()!=null){
                                onFailed(detailTransactionModel.getErrorMessage());
                            }
                            else {

                                transactionView.hideProgressBar();
                                transactionView.showOnClickTransaction(transID,transCat,transDate,transTime,transUserName,transEmail,
                                        transBalance,detailTransactionModel);
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
    public void initialAddTransactionLayout() {

        transactionView.showProgressBar();

        getCurrentuser();
        getCurrentDateTime();

        transactionView.hideProgressBar();
        transactionView.showAddTransactionLayout();
    }

    @Override
    public void addNewTransaction(String transIDADD, int transDateSortAdd, String transDateAdd,
                                  String transTimeAdd, String transUserEmailAdd, String transCatAdd,
                                  long transPriceAdd, List<DetailTransactionModel.DetailTransaction> detailTransactionModel) {

        transactionView.showProgressBar();

        TransactionSatuanModel transactionModelAdd = new TransactionSatuanModel();
        transactionModelAdd.setTransactionID(transIDADD);
        transactionModelAdd.setTransactionDateSort(transDateSortAdd);
        transactionModelAdd.setTransactionDate(transDateAdd);
        transactionModelAdd.setTransactionTime(transTimeAdd);
        transactionModelAdd.setUserEmail(transUserEmailAdd);
        transactionModelAdd.setTransactionCategory(transCatAdd);
        transactionModelAdd.setTransactionBalance(transPriceAdd);

        AndroidNetworking.post(K.URL_ADD_TRANSACTION)
                .addBodyParameter(transactionModelAdd)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GeneralAddResponseModel.class, new ParsedRequestListener<GeneralAddResponseModel>() {
                    @Override
                    public void onResponse(GeneralAddResponseModel generalAddResponseModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(generalAddResponseModel));
                        if(generalAddResponseModel.getErrorMessage()!=null){
                            onFailed(generalAddResponseModel.getErrorMessage());
                        }
                        else {
                            onSuccessAdd(generalAddResponseModel.getSuccessMessage());
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

    @Override
    public void getCategoryTransaction() {
        transactionView.showProgressBar();

        AndroidNetworking.post(K.URL_GET_CATEGORY_TRANSACTION)
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
                            successGetCategoryTransaction(categoryModel);
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
    public void getAllProduct() {
        transactionView.showProgressBar();
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
                            successGetProductTransaction(productModel);
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
    public void getAllPaymentMethod() {

        transactionView.showProgressBar();
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
                            successGetPaymentMethodeTransaction(paymentMethodeModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed("ERROR");
                    }
                });
    }

    private void getCurrentuser() {
        if (DataManager.can().getUserInfoFromStorage() != null) {
            RegisterInteractor userInfoModel = DataManager.can().getUserInfoFromStorage();
            transUserEmailAdd = userInfoModel.getUserEmail();
        }
    }

    private void getCurrentDateTime() {
        DateAndTime dateAndTime = new DateAndTime();
        transDateAdd = dateAndTime.getCurrentDate(FORMAT_TANGGAL_STRING);
//        mBinding.lyAddTransaction.tvDate.setText(transDateAdd);

        transTimeAdd = dateAndTime.getCurrentTime(FORMAT_TIME);
//        mBinding.lyAddTransaction.tvTime.setText(transTimeAdd);
        String dateSort = dateAndTime.getCurrentDate(FORMAT_TANGGAL_SORT);
        try {
            transDateSortAdd = Integer.parseInt(dateSort);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void onFailed(String message){

        transactionView.hideProgressBar();
        transactionView.onFailedGetAllAPI(message);
    }

    private void onSuccess(TransactionModel transactionModel){

        transactionView.hideProgressBar();
        transactionView.showAllTransaction(transactionModel);
    }

    private void onSuccessAdd(String message){
        transactionView.hideProgressBar();
        transactionView.onSuccessAddtAllAPI(message);
    }

    private void successGetCategoryTransaction(CategoryModel categoryModel){
        transactionView.hideProgressBar();
        transactionView.showDialogListCategory(categoryModel);
    }

    private void successGetProductTransaction(ProductModel productModel){
        transactionView.hideProgressBar();
        transactionView.showDialogListProduct(productModel);
    }

    private void successGetPaymentMethodeTransaction(PaymentMethodeModel paymentMethodeModel){
        transactionView.hideProgressBar();
        transactionView.showDialogListPaymentMethode(paymentMethodeModel);
    }

}
