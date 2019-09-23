package com.androiddevnkds.kopiseong.module.resep;

import com.androiddevnkds.kopiseong.model.ResepItemModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.model.UpdateResponseModel;

import java.util.List;

public interface ResepContract {

    interface resepView{

        void setHPP(long hpp, int tipe, String hasilItem, String hasilJumlahItem);

        void showAllStock(StockModel stockModel, int selectedPos);

        void succesDeleteResep(String message, int position);

        void successAddResep(String message, ResepModel.ResepModelSatuan resepModelSatuan);

        void showProgressBar();

        void hideProgressBar();

        void onFailed(int tipe,String message);

        void showAllResep(ResepModel resepModel);

        void showSuccessUpdate(String message);

        void updateResepItem(ResepItemModel resepItemModelList, int pos);

        void showEditDialog(ResepItemModel resepItemModel);

        void showResepSatuan(ResepModel.ResepModelSatuan resepModelSatuan, List<ResepItemModel> resepItemModelList);
    }

    interface resepPresenter{

        void getAllResep();

        void updateResep(ResepModel.ResepModelSatuan resepModelSatuan);

        void setOnClickResep(ResepModel resepModel, int position);

        void setOnClickEditResep(List<ResepItemModel> resepItemModel, int position);

        void onFailed(int tipe,String message);

        void getDataAfterEdit(ResepItemModel resepItemModel, int position);

        void deleteResep(String resepID,int position);

        void addResep(ResepModel.ResepModelSatuan resepModelSatuan);

        void getCountHPP(String item, String jumlahItem, int tipe);

        void getAllStock(String stock);
    }
}
