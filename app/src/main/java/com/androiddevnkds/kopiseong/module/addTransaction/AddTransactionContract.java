package com.androiddevnkds.kopiseong.module.addTransaction;

import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;

import java.util.List;

public interface AddTransactionContract {

    interface addTransactionView{

        void showProgressBar();

        void hideProgressBar();

        void onFailedGetAllAPI(String message);

        void showCategoryList(CategoryModel categoryModel, int pos);

        void showProductList(ProductModel productModel, int pos);

        void showPaymentList(PaymentMethodeModel paymentMethodeModel, int pos);

        void showTipeExpenseList(List<String> tipeExpenseModel , int pos);

        void showDateTimeToday(String dateString, String timeString, String transID);

        void showSuccessAddTransaction(String message);

    }
    interface addTransactionPresenter{

        void getCategoryList(CategoryModel categoryModel, String category);

        void getDateTimeToday(String dateFormatString, String dateFormatSort,String timeFormatString, String timeFormatSort);

        void getProductList(ProductModel productModel, String product);

        void getPaymentList(PaymentMethodeModel paymentMethodeModel, String payment);

        void addTransaction(TransactionSatuanModel transactionSatuanModel,String generalCat, String detailProduct, String detailJumlah,String detailResep);

        void addTransactionExp(TransactionSatuanModel transactionSatuanModel,String generalCat, String detailProduct, String detailJumlah);

        void getTipeExpenseLainnya(List<String> tipeExpenseModel, String tipeExpense);

    }
}
