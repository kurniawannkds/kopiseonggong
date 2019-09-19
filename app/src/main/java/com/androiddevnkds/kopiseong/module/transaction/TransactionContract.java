package com.androiddevnkds.kopiseong.module.transaction;

import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
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

        void showAddTransactionLayout();

        void showDialogListCategory(CategoryModel categoryModel);

        void showDialogListProduct(ProductModel productModel);

        void showDialogListPaymentMethode(PaymentMethodeModel paymentMethodeModel);

    }
    interface transactionPresenter{

        void getAllTransaction(int currentPage, String cat, String date, String user, String pembayaran);

        void setOnClickTransaction(TransactionModel transactionModel, int position);

        void initialAddTransactionLayout();

        void addNewTransaction(String transIDADD, int transDateSortAdd, String transDateAdd, String transTimeAdd,
                               String transUserEmailAdd, String transCatAdd, long transPriceAdd,
                               List<DetailTransactionModel.DetailTransaction> detailTransactionModel);

        void getCategoryTransaction();

        void getAllProduct();

        void getAllPaymentMethod();

    }
}
