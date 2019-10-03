package com.androiddevnkds.kopiseong.module.product;

import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.ResepModel;

import java.util.List;

public interface ProductContract {

    interface productView{
        void showProgressBar();

        void hideProgressBar();

        void showAllProduct(ProductModel productModel);

        void showDetailProduct(ProductModel.ProductSatuan productSatuan, int pos);

        void showSuccessEdit(String message,ProductModel.ProductSatuan productSatuan, int pos);

        void showSuccessDelete(String message,int pos);

        void showResepList(ResepModel resepModel, int pos);

        void showHpp(long hpp);

        void showSuccessAdd(String message,ProductModel.ProductSatuan product, int sizeArray);

        void onFailed(String message);

        void showCatList(List<String> generalCat, int pos);

    }

    interface productPresenter{

        void getAllProduct(String generalCat);

        void setOnClickProduct(ProductModel product, int pos);

        void editProduct(ProductModel.ProductSatuan productSatuan, int pos);

        void deleteProduct(String productID, int pos);

        void getResepList(ResepModel resepModel, String resepID);

        void countHPP(String resepID);

        void addProduct(ProductModel.ProductSatuan productSatuan, int sizeTotal);

        void getCategoryGeneral(List<String> generalCat, String general);

    }
}
