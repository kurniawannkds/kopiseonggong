package com.androiddevnkds.kopiseong.module.transaction;

import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;

import java.util.List;

public interface TransactionContract {

    interface transactionView{

        void showAllTransaction(TransactionModel transactionModel);

        void showProgressBar();

        void hideProgressBar();

        void onFailedGetAllAPI(String message);

        void onSuccessAddtAllAPI(String message);

        void showOnClickTransaction(String transID, String transCat, String transDate,
                                    String transTime, String transUserName, String transEmail,
                                    long transBalance, DetailTransactionModel detailTransactionModel);

//        void showCurrentUserEmail(String email);
//
//        void showCurrentDateTime(String date, String time, int dateInt);

        void showAddTransactionLayout();

        void showDialogListCategory(CategoryModel categoryModel);

        void showDialogListProduct(ProductModel productModel);

    }
    interface transactionPresenter{

        void getAllTransaction(int currentPage);

        void setOnClickTransaction(TransactionModel transactionModel, int position);

        void initialAddTransactionLayout();

        void addNewTransaction(String transIDADD, int transDateSortAdd, String transDateAdd, String transTimeAdd,
                               String transUserEmailAdd, String transCatAdd, long transPriceAdd,
                               List<DetailTransactionModel.DetailTransaction> detailTransactionModel);

        void getCategoryTransaction();

        void getAllProduct();

    }
}
