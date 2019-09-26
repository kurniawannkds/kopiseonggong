package com.androiddevnkds.kopiseong.module.stock.stockStore;

import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.StockModel;

public interface StockStoreContract {

    interface stockView{

        void showProgressBar();

        void hideProgressBar();

        void showAllStock(StockModel stockModel);

        void onFailed(int tipe, String message);

        void showStockDetail(StockModel.StockSatuanModel stockSatuanModel, int position);

        void showSuccessDelete(int position, String message);

        void showSuccessEditStock(String message, StockModel.StockSatuanModel stockSatuanModel, int pos);

    }

    interface stockPresenter{

        void getAllStock();

        void setOnClickStock(StockModel stockModel, int position);

        void deleteStock(String stockID, String dateSort, int position);

        void editStock(StockModel.StockSatuanModel stockSatuanModel, int position);
    }
}
