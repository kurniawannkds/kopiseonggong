package com.androiddevnkds.kopiseong.module.product;

import android.util.Log;

import com.androiddevnkds.kopiseong.model.HPPModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.List;

public class ProductPresenter implements ProductContract.productPresenter {

    private ProductContract.productView productView;
    private ResepModel resepModelGlobal;
    private List<String> generalCatModelGlobal;
    private String generalCatGlobal ="";
    private String resepGlobal ="";
    private int size =0, pos = 0;

    public ProductPresenter(ProductContract.productView productView){

        this.productView = productView;
    }
    @Override
    public void getAllProduct(String generalCat) {

        productView.showProgressBar();

        AndroidNetworking.post(K.URL_GET_PRODUCT)
                .addBodyParameter("general_category", generalCat)
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

                            productView.hideProgressBar();
                            productView.showAllProduct(productModel);
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
    public void setOnClickProduct(ProductModel product, int pos) {

        ProductModel.ProductSatuan productSatuan = product.getProductSatuanList().get(pos);
        productView.showDetailProduct(productSatuan,pos);

    }

    @Override
    public void editProduct(final ProductModel.ProductSatuan productSatuan, final int pos) {

        productView.showProgressBar();

        if(!isEmpty(productSatuan)) {
            AndroidNetworking.post(K.URL_EDIT_PRODUCT)
                    .addBodyParameter("update_product", "update")
                    .addBodyParameter("product_id", productSatuan.getProductID())
                    .addBodyParameter("product_name", productSatuan.getProductName())
                    .addBodyParameter("product_price", productSatuan.getProductPrice() + "")
                    .addBodyParameter("product_resep_id", productSatuan.getProductResepID())
                    .addBodyParameter("product_general_category", productSatuan.getProductGeneralCategory())
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

                                productView.hideProgressBar();
                                productView.showSuccessEdit(updateResponseModel.getSuccessMessage(), productSatuan, pos);
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
    public void deleteProduct(String productID, final int pos) {

        productView.showProgressBar();

        AndroidNetworking.post(K.URL_EDIT_PRODUCT)
                .addBodyParameter("delete_product", "delete")
                .addBodyParameter("product_id", productID)
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

                            productView.hideProgressBar();
                            productView.showSuccessDelete(updateResponseModel.getErrorMessage(),pos);
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
    public void getResepList(ResepModel resepModelT, final String resepID) {

        boolean flag = false;
        if(resepModelT!=null) {
            if(resepModelT.getResepModelSatuanList()!=null){
                if(resepModelT.getResepModelSatuanList().size()>0){

                    resepModelGlobal = resepModelT;
                    resepGlobal = resepID;
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
            productView.showProgressBar();
            AndroidNetworking.post(K.URL_GET_ALL_RESEP)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(ResepModel.class, new ParsedRequestListener<ResepModel>() {
                        @Override
                        public void onResponse(ResepModel resepModel) {
                            // do anything with response
                            if (resepModel.getErrorMessage() != null) {
                                onFailed(resepModel.getErrorMessage());
                            } else {

                                resepModelGlobal = resepModel;
                                resepGlobal = resepID;
                                setCustomeList(1);
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
    }

    @Override
    public void countHPP(String resepID) {

        productView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_COUNT_HPP)
                .addBodyParameter("resep_id", resepID)
                .addBodyParameter("resep_jumlah", "1")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(HPPModel.class, new ParsedRequestListener<HPPModel>() {
                    @Override
                    public void onResponse(HPPModel hppModel) {
                        // do anything with response
                        if (hppModel.getErrorMessage() != null) {
                            onFailed(hppModel.getErrorMessage());

                        } else {

                            productView.hideProgressBar();
                            productView.showHpp(hppModel.getHpp());
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

    @Override
    public void addProduct(final ProductModel.ProductSatuan productSatuan, final int sizeTotal) {

        productView.showProgressBar();
        if(!isEmpty(productSatuan)){

            AndroidNetworking.post(K.URL_EDIT_PRODUCT)
                    .addBodyParameter("add_product", "add")
                    .addBodyParameter("product_id", productSatuan.getProductID())
                    .addBodyParameter("product_name", productSatuan.getProductName())
                    .addBodyParameter("product_price", productSatuan.getProductPrice()+"")
                    .addBodyParameter("product_resep_id", productSatuan.getProductResepID())
                    .addBodyParameter("product_general_category", productSatuan.getProductGeneralCategory())
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

                                productView.hideProgressBar();
                                productView.showSuccessAdd(updateResponseModel.getSuccessMessage(),productSatuan,sizeTotal);
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
    public void getCategoryGeneral(List<String> generalCat, String general) {
        productView.showProgressBar();
        generalCatModelGlobal = generalCat;
        generalCatGlobal = general;
        setCustomeList(2);
    }

    private void onFailed(String message){

        productView.hideProgressBar();
        productView.onFailed(message);
    }

    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (resepModelGlobal.getResepModelSatuanList() != null) {
                size = resepModelGlobal.getResepModelSatuanList().size();
                if (size > 0) {

                    pos = findPosition(1);
                    productView.hideProgressBar();
                    productView.showResepList(resepModelGlobal, pos);
                }
            }
        }
        else if(tipe==2) {
            if (generalCatModelGlobal != null) {
                size = generalCatModelGlobal.size();
                if (size > 0) {

                    pos = findPosition(2);
                    productView.hideProgressBar();
                    productView.showCatList(generalCatModelGlobal, pos);
                }
            }
        }

    }

    private int findPosition(int tipe) {

        int tempPosition = -1;

        if(tipe==1) {
            if (!resepGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < resepModelGlobal.getResepModelSatuanList().size(); i++) {
                    if (resepModelGlobal.getResepModelSatuanList().get(i).getResepID().equalsIgnoreCase(resepGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }
        else if(tipe==2) {
            if (!generalCatGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < generalCatModelGlobal.size(); i++) {
                    if (generalCatModelGlobal.get(i).equalsIgnoreCase(generalCatGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }

    private boolean isEmpty(ProductModel.ProductSatuan productSatuan){

        if(productSatuan.getProductID().equalsIgnoreCase("")){

            onFailed("Product ID cannot be empty");
            return true;

        }
        else if(productSatuan.getProductName().equalsIgnoreCase("")){

            onFailed("Product Name cannot be empty");
            return true;
        }

        else if(productSatuan.getProductGeneralCategory().equalsIgnoreCase("")){

            onFailed("Product General Category cannot be empty");
            return true;
        }

        else if(productSatuan.getProductResepID().equalsIgnoreCase("")){

            onFailed("Product Recipe cannot be empty");
            return true;
        }
        else if(productSatuan.getProductPrice()==0){

            onFailed("Product Price cannot be empty");
            return true;
        }
        else {
            return false;
        }
    }
}
