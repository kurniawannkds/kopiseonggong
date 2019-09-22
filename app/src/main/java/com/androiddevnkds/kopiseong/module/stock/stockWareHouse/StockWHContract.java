package com.androiddevnkds.kopiseong.module.stock.stockWareHouse;

import com.androiddevnkds.kopiseong.model.StockModel;

public interface StockWHContract {

    interface stockView{

        void showProgressBar();

        void hideProgressBar();

        void showAllStock(StockModel stockModel);

        void onFailed(int tipe, String message);

        void showStockDetail(StockModel.StockSatuanModel stockSatuanModel, int position);

        void showSuccessDelete(int position,String message);
    }

    interface stockPresenter{

        void getAllStock();

        void setOnClickStock(StockModel stockModel, int position);

        void deleteStock(String stockID, String dateSort);
    }
}
