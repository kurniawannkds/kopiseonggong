package com.androiddevnkds.kopiseong;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telecom.Call;
import android.util.Log;

import com.androiddevnkds.kopiseong.MyApplication;
import com.androiddevnkds.kopiseong.model.AddDetailTransactionRequestModel;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.GeneralAddResponseModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseFragment extends Fragment {

    private static FragmentManager mFragMgr;
    public MyApplication app = MyApplication.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getActivity().getApplicationContext());

        mFragMgr = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * method used for first UI initialization & manipulation
     */
    public abstract void initUI();

    /**
     * method used for first event initialization & manipulation
     */
    public abstract void initEvent();


    //register api
    protected void callRegisterUserAPI(final UserInfoModel userInfoModel){

        AndroidNetworking.post(K.URL_REGISTER_USER)

                .addBodyParameter(userInfoModel)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UserInfoModel.class, new ParsedRequestListener<UserInfoModel>() {
                    @Override
                    public void onResponse(UserInfoModel user) {
                        // do anything with response
                        if(user.getErrorMessage()!=null){
                            failedRegister(user.getErrorMessage());
                        }
                        else {
                            successRegister(user);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedRegister("ERROR");
                    }
                });
    }

    protected void successRegister(UserInfoModel userResponseModel) {

    }

    protected void failedRegister(String message) {

    }

    //login api
    protected void callLoginUserAPI(final UserInfoModel userInfoModel){

        AndroidNetworking.post(K.URL_LOGIN)
                .addBodyParameter("user_email",userInfoModel.getUserEmail())
                .addBodyParameter("user_pass",userInfoModel.getUserPass())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UserInfoModel.class, new ParsedRequestListener<UserInfoModel>() {
                    @Override
                    public void onResponse(UserInfoModel user) {
                        // do anything with response
                        if(user.getErrorMessage()!=null){
                            failedLogin(user.getErrorMessage());
                        }
                        else {
                            successLogin(user);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedLogin("ERROR");
                        Log.e("base",anError.getErrorDetail());
                    }
                });
    }

    protected void successLogin(UserInfoModel userResponseModel) {

    }

    protected void failedLogin(String message) {

    }

    //get all transaction api
    protected void callAllTransactionAPI(String currentpage){

        AndroidNetworking.post(K.URL_GET_ALL_TRANSACTION)
                .addBodyParameter("currentPage",currentpage)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(TransactionModel.class, new ParsedRequestListener<TransactionModel>() {
                    @Override
                    public void onResponse(TransactionModel transactionModels) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(transactionModels));
                        if(transactionModels.getErrorMessage()!=null){
                            failedGetAllTransaction(transactionModels.getErrorMessage());
                        }
                        else {
                            successGetAllTransaction(transactionModels);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllTransaction("ERROR");
                        Log.e("ERROR",anError.getErrorDetail());

                    }
                });
    }

    protected void successGetAllTransaction(TransactionModel transactionModel) {

    }

    protected void failedGetAllTransaction(String message) {

    }

    //add transaction api
    protected void callAddTransactionAPI(TransactionSatuanModel transactionModel){

        Log.e("transaction model",new Gson().toJson(transactionModel));
        AndroidNetworking.post(K.URL_ADD_TRANSACTION)
                .addBodyParameter(transactionModel)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GeneralAddResponseModel.class, new ParsedRequestListener<GeneralAddResponseModel>() {
                    @Override
                    public void onResponse(GeneralAddResponseModel generalAddResponseModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(generalAddResponseModel));
                        if(generalAddResponseModel.getErrorMessage()!=null){
                            failedAddResponse(generalAddResponseModel.getErrorMessage());
                        }
                        else {
                            successAddResponseTransaction(generalAddResponseModel.getSuccessMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllTransaction("ERROR");
                        Log.e("ERROR",anError.getErrorDetail());

                    }
                });
    }

    protected void successAddResponseTransaction(String message) {

    }

    protected void failedAddResponse(String message) {

    }

    //get detail transaction api
    protected void getDetailTransactionAPI(String transID, String transEmail){

        AndroidNetworking.post(K.URL_GET_DETAIL_TRANSACTION)
                .addBodyParameter("transaction_id",transID)
                .addBodyParameter("user_email",transEmail)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(DetailTransactionModel.class, new ParsedRequestListener<DetailTransactionModel>() {
                    @Override
                    public void onResponse(DetailTransactionModel detailTransactionModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(detailTransactionModel));
                        if(detailTransactionModel.getErrorMessage()!=null){
                            failedGetDetailTransaction(detailTransactionModel.getErrorMessage());
                        }
                        else {
                            successGetDetailTransaction(detailTransactionModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetDetailTransaction("ERROR");
                    }
                });
    }

    protected void successGetDetailTransaction(DetailTransactionModel detailTransactionModel) {

    }

    protected void failedGetDetailTransaction(String message) {

    }


    //add detail transaction
    protected void callAddDetailTransactionAPI(DetailTransactionModel detailTransactionModel){

        Log.e("transaction model",new Gson().toJson(detailTransactionModel));

        AddDetailTransactionRequestModel addDetail = new AddDetailTransactionRequestModel();
        addDetail.setTransactionID(detailTransactionModel.getDetailTransactionList().get(0).getDetailTransactionID());

        int sizeModel = detailTransactionModel.getDetailTransactionList().size();
        addDetail.setSizeModel(sizeModel);

        String[] detailProduct =new String[sizeModel];
        String[] detailJumlah=new String[sizeModel];
        for(int i=0;i<sizeModel;i++){

            detailJumlah[i] = detailTransactionModel.getDetailTransactionList().get(i).getDetailJumlah()+"";
            detailProduct[i] = detailTransactionModel.getDetailTransactionList().get(i).getDetailProductID();
        }

        addDetail.setDetailJumlah(detailJumlah);
        addDetail.setDetailProduct(detailProduct);

        Log.e("transaction model",new Gson().toJson(addDetail));
        AndroidNetworking.post(K.URL_ADD_DETAIL_TRANSACTION)
                .addBodyParameter(addDetail)
                .addBodyParameter("detail_jumlah[]", Arrays.toString(detailJumlah))
                .addBodyParameter("detail_product[]",Arrays.toString(detailProduct))
                .addBodyParameter("transaction_id",addDetail.getTransactionID())
                .addBodyParameter("size_model",sizeModel+"")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GeneralAddResponseModel.class, new ParsedRequestListener<GeneralAddResponseModel>() {
                    @Override
                    public void onResponse(GeneralAddResponseModel generalAddResponseModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(generalAddResponseModel));
                        if(generalAddResponseModel.getErrorMessage()!=null){
                            failedAddResponse(generalAddResponseModel.getErrorMessage());
                        }
                        else {
                            successAddDetailResponseTransaction(generalAddResponseModel.getSuccessMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllTransaction("ERROR");
                        Log.e("ERROR",anError.getErrorDetail());
                        Log.e("ERROR",anError.getErrorBody());
                    }
                });
    }

    protected void successAddDetailResponseTransaction(String message) {

    }

    //get category transaction api
    protected void getCategoryTransactionAPI(){

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
                            failedGetCategotyTransaction(categoryModel.getErrorMessage());
                        }
                        else {
                            successGetCategoryTransaction(categoryModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetCategotyTransaction("ERROR");
                    }
                });
    }

    protected void successGetCategoryTransaction(CategoryModel categoryModel) {

    }

    protected void failedGetCategotyTransaction(String message) {

    }


    //get all product api
    protected void getAllProductAPI(){

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
                            failedGetAllProduct(productModel.getErrorMessage());
                        }
                        else {
                            successGetAllProduct(productModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllProduct("ERROR");
                    }
                });
    }

    protected void successGetAllProduct(ProductModel productModel) {

    }

    protected void failedGetAllProduct(String message) {

    }

    //get all product api
    protected void getProductByIDAPI(String productID){

        AndroidNetworking.post(K.URL_GET_PRODUCT)
                .addBodyParameter("product_id",productID)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ProductModel.class, new ParsedRequestListener<ProductModel>() {
                    @Override
                    public void onResponse(ProductModel productModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(productModel));
                        if(productModel.getErrorMessage()!=null){
                            failedGetAllProduct(productModel.getErrorMessage());
                        }
                        else {
                            successGetAllProduct(productModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllProduct("ERROR");
                    }
                });
    }

    //get all product api
    protected void getAllBalanceAPI(){

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
                            failedGetAllBalance(totalBalanceModel.getErrorMessage());
                        }
                        else {
                            successGetAllBalance(totalBalanceModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        failedGetAllProduct("ERROR");
                    }
                });
    }

    protected void successGetAllBalance(TotalBalanceModel totalBalanceModel) {

    }

    protected void failedGetAllBalance(String message) {

    }
}
