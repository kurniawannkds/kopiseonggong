package com.androiddevnkds.kopiseong.module.asset;

import com.androiddevnkds.kopiseong.model.AssetModel;

public interface AssetContract {
    interface assetView{

        void showProgressBar();

        void hideProgressBar();

        void onFailed(String message);
    }
    interface assetPresenter{

        void getAllAsset();

        void getAssetForList(AssetModel assetModelT, String assetName);
    }
}
