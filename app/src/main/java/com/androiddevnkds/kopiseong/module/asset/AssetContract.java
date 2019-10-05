package com.androiddevnkds.kopiseong.module.asset;

import com.androiddevnkds.kopiseong.model.AssetModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;

public interface AssetContract {
    interface assetView{

        void showProgressBar();

        void hideProgressBar();

        void onFailed(String message);

        void showAllAsset(AssetModel assetModel);

        void showAssetList(AssetModel assetModel, int pos);

        void showPaymentList(PaymentMethodeModel paymentMethodeModel, int pos);

        void showAssetDetail(AssetModel.AssetModelSatuan assetModelSatuan, int pos, long currentPrice);

        void showSuccessAdd(String message);

        void showSuccessSell(String message, int pos);

        void showSuccessEdit(String message, int pos);

        void showSuccessDelete(String message, int pos);
    }
    interface assetPresenter{

        void getAllAsset();

        void getAssetForList(AssetModel assetModelT, String assetName);

        void getTipeBayarList(PaymentMethodeModel paymentMethodeModelT, String payment);

        void setOnCLickAsset(AssetModel asset, int pos);

        void addAsset(AssetModel.AssetModelSatuan assetModelSatuan, String tipeBayar);

        void sellAsset(String assetID, long transPrice, String tipeBayar, int pos);

        void editAsset(AssetModel.AssetModelSatuan assetModelSatuan, int pos);

        void deleteAsset(String assetID, int pos);
    }
}
