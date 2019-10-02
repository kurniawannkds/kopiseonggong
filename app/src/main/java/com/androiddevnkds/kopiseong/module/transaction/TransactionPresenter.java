package com.androiddevnkds.kopiseong.module.transaction;

import android.util.Log;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.GeneralAddResponseModel;
import com.androiddevnkds.kopiseong.model.ListUserModel;
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
    private String transTipePembayaran = "";
    private long transBalance = 0;
    private int sizeArray = 0, selectedPos = 0;

    private CategoryModel categoryModelGlobal;
    private String categoryGlobal ="";

    private PaymentMethodeModel paymentMethodeModelGlobal;
    private String paymentGlobal = "";

    private ListUserModel listUserModelGlobal;
    private String userGlobal = "";



    public TransactionPresenter(TransactionContract.transactionView transactionView){
        this.transactionView = transactionView;
    }

    @Override
    public void getAllTransaction(int currentPage, String category, String date, String userName, String pembayaran) {

        transactionView.showProgressBar();
        Log.e("trans",currentPage+"");
        String userRole = "";
        if(DataManager.can().getUserInfoFromStorage()!=null){
            if(DataManager.can().getUserInfoFromStorage().getUserRole()!=null){
                userRole = DataManager.can().getUserInfoFromStorage().getUserRole();
            }
        }
        userRole = "Master";
        Log.e("TRANS",userRole);
        AndroidNetworking.post(K.URL_GET_ALL_TRANSACTION)
                .addBodyParameter("currentPage",currentPage+"")
                .addBodyParameter("trans_category",category)
                .addBodyParameter("trans_date",date)
                .addBodyParameter("trans_user_email",userName)
                .addBodyParameter("trans_user_role",userRole)
                .addBodyParameter("trans_tipe_pembayaran",pembayaran)
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
    public void setOnClickTransaction(TransactionModel transactionModelGlobal, int position, int page) {

        transactionView.showProgressBar();
        Log.e("trans pos",position+"");
        if(page>0) {
            position = position + (page * 20);
        }
        Log.e("trans size",transactionModelGlobal.getTransactionModelLists().size()+"");
        Log.e("trans pos",position+"");
        Log.e("trans",new Gson().toJson(transactionModelGlobal.getTransactionModelLists().get(position)));
        final TransactionSatuanModel transactionSatuanModel = new TransactionSatuanModel();
        if (transactionModelGlobal.getTransactionModelLists() != null) {

            if (transactionModelGlobal.getTransactionModelLists().size() > 0) {

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionID() != null) {
                    transID = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionID();
                    transactionSatuanModel.setTransactionID(transID);
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionCategory() != null) {
                    transCat = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionCategory();
                    transactionSatuanModel.setTransactionCategory(transCat);
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionDate() != null) {
                    transDate = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionDate();
                    transactionSatuanModel.setTransactionDate(transDate);
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionTime() != null) {
                    transTime = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionTime();
                    transactionSatuanModel.setTransactionTime(transTime);
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTransactionBalance() != 0) {
                    transBalance = transactionModelGlobal.getTransactionModelLists().get(position).getTransactionBalance();
                    transactionSatuanModel.setTransactionBalance(transBalance);
                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getUserEmail() != null) {
                    transEmail = transactionModelGlobal.getTransactionModelLists().get(position).getUserEmail();
                    transactionSatuanModel.setUserEmail(transEmail);
                    Log.e("trans",transEmail);

                }

                if (transactionModelGlobal.getTransactionModelLists().get(position).getTipePembayaran() != null) {
                    transTipePembayaran = transactionModelGlobal.getTransactionModelLists().get(position).getTipePembayaran();
                    transactionSatuanModel.setTipePembayaran(transTipePembayaran);

                }
            }
        }

        Log.e("trans",transID);
        if (!transID.equalsIgnoreCase("") ) {

            Log.e("trans",transID);

            if(transCat.equalsIgnoreCase("INCOME")) {

                AndroidNetworking.post(K.URL_GET_DETAIL_TRANSACTION)
                        .addBodyParameter("transaction_id", transID)
                        .addBodyParameter("category_income","INCOME")
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsObject(DetailTransactionModel.class, new ParsedRequestListener<DetailTransactionModel>() {
                            @Override
                            public void onResponse(DetailTransactionModel detailTransactionModel) {
                                // do anything with response
                                Log.e("trans", new Gson().toJson(detailTransactionModel));
                                if (detailTransactionModel.getErrorMessage() != null) {
                                    onFailed(detailTransactionModel.getErrorMessage());
                                } else {

                                    transactionView.hideProgressBar();
                                    transactionView.showOnClickTransaction(transactionSatuanModel, detailTransactionModel);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // handle error
                                onFailed("ERROR");
                            }
                        });
            }
            else {

                AndroidNetworking.post(K.URL_GET_DETAIL_TRANSACTION)
                        .addBodyParameter("transaction_id", transID)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsObject(DetailTransactionModel.class, new ParsedRequestListener<DetailTransactionModel>() {
                            @Override
                            public void onResponse(DetailTransactionModel detailTransactionModel) {
                                // do anything with response
                                Log.e("trans", new Gson().toJson(detailTransactionModel));
                                if (detailTransactionModel.getErrorMessage() != null) {
                                    onFailed(detailTransactionModel.getErrorMessage());
                                } else {

                                    transactionView.hideProgressBar();
                                    transactionView.showOnClickTransaction(transactionSatuanModel, detailTransactionModel);
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
    }

    @Override
    public void setOnClickDetailTransaction(DetailTransactionModel detailTransactionModel, int position) {

        DetailTransactionModel.DetailTransaction detailTransaction = new DetailTransactionModel().new DetailTransaction();
        detailTransaction = detailTransactionModel.getDetailTransactionList().get(position);
        transactionView.showMoreDetailTransaction(detailTransaction,transCat);
    }

    @Override
    public void showListCategory(CategoryModel categoryModelT, final String category) {

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
//            //dummy
//            userRole = "Master";
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
    public void showListPayment(PaymentMethodeModel paymentMethodeModelT, final String payment) {
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
    public void showListUser(ListUserModel listUserModelT, final String userEmail) {

        boolean flag = false;
        if(listUserModelT!=null) {
            if(listUserModelT.getUserInfoModelList()!=null){
                if(listUserModelT.getUserInfoModelList().size()>0){

                    listUserModelGlobal = listUserModelT;
                    userGlobal = userEmail;
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


            AndroidNetworking.post(K.URL_GET_ALL_USER)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(ListUserModel.class, new ParsedRequestListener<ListUserModel>() {
                        @Override
                        public void onResponse(ListUserModel listUserModel) {
                            // do anything with response
                            Log.e("BASE",new Gson().toJson(listUserModel));
                            if(listUserModel.getErrorMessage()!=null){
                                onFailed(listUserModel.getErrorMessage());
                            }
                            else {

                                listUserModelGlobal = listUserModel;
                                userGlobal = userEmail;
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

    private void onFailed(String message){

        transactionView.hideProgressBar();
        transactionView.onFailedGetAllAPI(message);
    }

    private void onSuccess(TransactionModel transactionModel){

        transactionView.hideProgressBar();
        transactionView.showAllTransaction(transactionModel);
    }


    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (categoryModelGlobal.getCategorySatuanList() != null) {
                sizeArray = categoryModelGlobal.getCategorySatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(1);
                    transactionView.hideProgressBar();
                    transactionView.showAllCategory(categoryModelGlobal, selectedPos);
                }
            }
        }
        else if(tipe==2){

            if (paymentMethodeModelGlobal.getPaymentMethodeSatuanList() != null) {
                sizeArray = paymentMethodeModelGlobal.getPaymentMethodeSatuanList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(2);
                    transactionView.hideProgressBar();
                    transactionView.showAllPayment(paymentMethodeModelGlobal, selectedPos);
                }
            }
        }

        else if(tipe==3){

            if (listUserModelGlobal.getUserInfoModelList() != null) {
                sizeArray = listUserModelGlobal.getUserInfoModelList().size();
                if (sizeArray > 0) {

                    selectedPos = findPosition(3);
                    transactionView.hideProgressBar();
                    transactionView.showAllUser(listUserModelGlobal, selectedPos);
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

        else if(tipe==3){

            if (!userGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < listUserModelGlobal.getUserInfoModelList().size(); i++) {
                    if (listUserModelGlobal.getUserInfoModelList().get(i).getUserEmail().equalsIgnoreCase(userGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }


}
