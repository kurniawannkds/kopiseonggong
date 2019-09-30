package com.androiddevnkds.kopiseong.module.transaction;

import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.ListUserModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;

import java.util.List;

public interface TransactionContract {

    interface transactionView{

        void showAllTransaction(TransactionModel transactionModel);

        void showProgressBar();

        void hideProgressBar();

        void onFailedGetAllAPI(String message);

        void showOnClickTransaction(TransactionSatuanModel transactionSatuanModel, DetailTransactionModel detailTransactionModel);

        void showMoreDetailTransaction(DetailTransactionModel.DetailTransaction detailTransaction);

        void showAllCategory(CategoryModel categoryModel, int pos);

        void showAllPayment(PaymentMethodeModel paymentMethodeModel, int pos);

        void showAllUser(ListUserModel listUserModel, int pos);
    }
    interface transactionPresenter{

        void getAllTransaction(int currentPage, String cat, String date, String user, String pembayaran);

        void setOnClickTransaction(TransactionModel transactionModel, int position, int page);

        void setOnClickDetailTransaction(DetailTransactionModel detailTransactionModel, int position);

        void showListCategory(CategoryModel categoryModel, String category);

        void showListPayment(PaymentMethodeModel paymentMethodeModelT, String payment);

        void showListUser(ListUserModel listUserModel, String userEmail);

    }
}
