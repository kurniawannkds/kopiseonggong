package com.androiddevnkds.kopiseong.module.asset;

import com.androiddevnkds.kopiseong.model.AssetModel;

public class AssetPresenter implements AssetContract.assetPresenter {

    private AssetContract.assetView assetView;

    public AssetPresenter(AssetContract.assetView assetView){
        this.assetView = assetView;
    }

    @Override
    public void getAllAsset() {

    }

    @Override
    public void getAssetForList(AssetModel assetModelT, String assetName) {

    }
}
