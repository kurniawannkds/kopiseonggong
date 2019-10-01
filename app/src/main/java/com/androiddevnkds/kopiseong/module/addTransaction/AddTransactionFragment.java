package com.androiddevnkds.kopiseong.module.addTransaction;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.DetailTransactionAdapter;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.DialogAddTransactionBinding;
import com.androiddevnkds.kopiseong.databinding.FragmentLoginBinding;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.login.LoginContract;
import com.androiddevnkds.kopiseong.module.login.LoginPresenter;
import com.androiddevnkds.kopiseong.module.login.model.LoginCredential;
import com.androiddevnkds.kopiseong.module.register.RegisterFragment;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransactionFragment extends BaseFragment implements AddTransactionContract.addTransactionView {

    private DialogAddTransactionBinding mBinding;
    private Context mContext;
    private String mDateString="", mTimeString = "", mTransID = "", mCategory ="", mCategoryGeneral ="",mPaymentType = "", mResep ="",mResepTemp="", mProduct="",mProductTemp ="", mJumlahProduk ="",mJumlahProdukTemp ="";
    private long mPrice = 0;
    private int mJumlahInt = 0;
    private boolean isDialogCustome = false, isPrice = false, isTipeExpense = false;
    private ListCustomAdapter listCustomAdapter;
    private DetailTransactionAdapter detailTransactionAdapter, detailTransactionAdapterExp;
    private List<DetailTransactionModel.DetailTransaction> detailTransactionList, detailTransactionListExp;

    private CategoryModel categoryForList;
    private ProductModel productForList;
    private PaymentMethodeModel paymentForList;
    private DetailTransactionModel detailTransactionModelGlobal,detailTransactionModelGlobalexp;
    private List<String> tipeExpenseGlobal;

    private Calendar myCalendar;
    private SimpleDateFormat sdf;

    private AddTransactionPresenter mPresenter;

    public AddTransactionFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getArguments() != null) {
            if (getArguments().containsKey(K.KEY_MAIN_FIRST_TIME)){

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_transaction, container, false);
        mPresenter = new AddTransactionPresenter(this);

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        mPresenter.getDateTimeToday(K.FORMAT_TANGGAL_STRING,K.FORMAT_TANGGAL_SORT,K.FORMAT_TIME_STRING,K.FORMAT_TIME_SORT);
//        mPresenter.getCategoryList(categoryForList,mCategory);
//        mPresenter.getProductList(productForList,mProduct);
//        mPresenter.getPaymentList(paymentForList,mPaymentType);

        detailTransactionList = new ArrayList<>();
        detailTransactionListExp = new ArrayList<>();

        detailTransactionModelGlobal = new DetailTransactionModel();
        detailTransactionModelGlobalexp = new DetailTransactionModel();

        detailTransactionAdapter = new DetailTransactionAdapter(mContext, detailTransactionModelGlobal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvTransactionAdd.setLayoutManager(layoutManager);
        mBinding.rvTransactionAdd.setAdapter(detailTransactionAdapter);

        detailTransactionAdapterExp = new DetailTransactionAdapter(mContext, detailTransactionModelGlobalexp);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(mContext);
        mBinding.rvTransactionAddExpense.setLayoutManager(layoutManager2);
        mBinding.rvTransactionAddExpense.setAdapter(detailTransactionAdapter);

        tipeExpenseGlobal.add(K.TIPE_EXPENSE_BIAYA_MAINTENANCE);
        tipeExpenseGlobal.add(K.TIPE_EXPENSE_BIAYA_STOCK_MINOR);
        if(DataManager.can().getUserInfoFromStorage().getUserRole()!=null){

            if(DataManager.can().getUserInfoFromStorage().getUserRole().equalsIgnoreCase(K.KEY_ROLE_MASTER)){
                tipeExpenseGlobal.add(K.TIPE_EXPENSE_BIAYA_GAJI);
            }
        }
        tipeExpenseGlobal.add(K.TIPE_EXPENSE_BIAYA_LAINNYA);
    }

    @Override
    public void initEvent() {


        mBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                        ((AppCompatActivity) mContext).getSupportFragmentManager(),
                        new TransactionFragment(), null, false);
            }
        });

        //category
        mBinding.layoutRelatifCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mPresenter.getCategoryList(categoryForList,mCategory);
            }
        });

        //produk
        mBinding.layoutRelatifProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                if(mCategory.equalsIgnoreCase("EXPENSE")){
                    mPresenter.getTipeExpenseLainnya(tipeExpenseGlobal,mProductTemp);
                }
                else {
                    mPresenter.getProductList(productForList, mProductTemp);
                }
            }
        });

        //payment
        mBinding.layoutRelatifPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mPresenter.getPaymentList(paymentForList,mPaymentType);
            }
        });

        //date
        mBinding.layoutRelatifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //time
        mBinding.layoutRelatifTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTime();
            }
        });

        //price
        mBinding.layoutRelatifPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isDialogCustome = false;
                isPrice = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Price Transaction With Number Only");
                if(mPrice>0) {
                    mBinding.lyDoneEditText.etNumber.setText(mPrice + "");
                }
                else {
                    mBinding.lyDoneEditText.etNumber.setText( "");

                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
            }
        });

        mBinding.layoutRelatifJumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isDialogCustome = false;
                isPrice = false;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Total Detail With Number Only");
                if(mJumlahInt!=0) {
                    mBinding.lyDoneEditText.etNumber.setText(mJumlahInt+"");
                }
                else {
                    mBinding.lyDoneEditText.etNumber.setText( "");

                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
            }
        });

        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim();

                if(isPrice){

                    try {
                        mPrice = Integer.parseInt(temp.replace(",",""));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    Log.e("add",mPrice+"");

                    MataUangHelper mataUangHelper= new MataUangHelper();
                    mBinding.tvPrice.setText(mataUangHelper.formatRupiah(mPrice));

                }
                else if(isTipeExpense){
                    mProductTemp = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    mBinding.tvProduct.setText(mProductTemp);

                }
                else {

                    mBinding.tvJumlah.setText(temp);
                    try {
                        mJumlahInt = Integer.parseInt(temp.replace(",",""));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    mJumlahProdukTemp = mJumlahInt+"";
                    Log.e("add",mJumlahProdukTemp);
                }

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
            }
        });

        //black
        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDialogCustome){

                    mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(GONE);
                    isDialogCustome = false;
                }
                else {
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                    if(isTipeExpense){
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
                        isTipeExpense = false;
                    }
                    else {

                        mBinding.lyBlack.lyBlack.setVisibility(GONE);
                    }
                }
            }
        });

        mBinding.btnAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mProductTemp.equalsIgnoreCase("") && !mResepTemp.equalsIgnoreCase("")
                        && !mJumlahProdukTemp.equalsIgnoreCase("")&&!mTransID.equalsIgnoreCase("")){

                    if (mProduct.equalsIgnoreCase("")) {
                        mProduct = mProductTemp;
                    } else {
                        mProduct = mProduct + "," + mProductTemp;
                    }

                    if (mResep.equalsIgnoreCase("")) {
                        mResep = mResepTemp;
                    } else {
                        mResep = mResep + "," + mResepTemp;
                    }

                    if (mJumlahProduk.equalsIgnoreCase("")) {
                        mJumlahProduk = mJumlahProdukTemp;
                    } else {
                        mJumlahProduk = mJumlahProduk + "," + mJumlahProdukTemp;
                    }

                    DetailTransactionModel.DetailTransaction detailTransaction = new DetailTransactionModel().new DetailTransaction();
                    detailTransaction.setDetailProductID(mProductTemp);
                    detailTransaction.setDetailJumlah(mJumlahInt);
                    detailTransaction.setDetailTransactionID(mTransID);

                    if(mCategory.equalsIgnoreCase("INCOME")) {
                        detailTransactionList.add(detailTransaction);
                        detailTransactionModelGlobal.setDetailTransactionList(detailTransactionList);
                        detailTransactionAdapter.setNewItemList(detailTransactionModelGlobal);
                        detailTransactionAdapter.notifyDataSetChanged();
                    }
                    else {
                        detailTransactionListExp.add(detailTransaction);
                        detailTransactionModelGlobalexp.setDetailTransactionList(detailTransactionListExp);
                        detailTransactionAdapterExp.setNewItemList(detailTransactionModelGlobalexp);
                        detailTransactionAdapterExp.notifyDataSetChanged();
                    }
                }
                else {
                    if(mCategory.equalsIgnoreCase("EXPENSE")){
                        showError("Expense Detail or total cannot be empty");
                    }
                    else {
                        showError("Product or total cannot be empty");
                    }
                }
            }
        });

        mBinding.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email ="";
                if(DataManager.can().getUserInfoFromStorage()!=null){
                    if(DataManager.can().getUserInfoFromStorage().getUserEmail()!=null){
                        email = DataManager.can().getUserInfoFromStorage().getUserEmail();
                    }
                }
                TransactionSatuanModel transactionSatuanModel = new TransactionSatuanModel();
                transactionSatuanModel.setTransactionID(mTransID);
                transactionSatuanModel.setTipePembayaran(mPaymentType);
                transactionSatuanModel.setTransactionBalance(mPrice);
                transactionSatuanModel.setTransactionTime(mTimeString);
                transactionSatuanModel.setTransactionDate(mDateString);
                transactionSatuanModel.setTransactionCategory(mCategory);
                transactionSatuanModel.setUserEmail(email);
                mPresenter.addTransaction(transactionSatuanModel,mCategoryGeneral,mProduct,mJumlahProduk,mResep);
            }
        });
    }


    @Override
    public void showProgressBar() {

        mBinding.viewBackground.setVisibility(View.VISIBLE);
        mBinding.pbBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {

        mBinding.viewBackground.setVisibility(View.GONE);
        mBinding.pbBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailedGetAllAPI(String message) {
        hideProgressBar();
        showError(message);
    }

    @Override
    public void showCategoryList(CategoryModel categoryModel, int pos) {

        isDialogCustome = true;

        categoryForList = categoryModel;
        setCustomeList(1);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (categoryModel.getCategorySatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProductList(ProductModel productModel, int pos) {

        isDialogCustome = true;

        productForList = productModel;
        setCustomeList(2);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (productModel.getProductSatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPaymentList(PaymentMethodeModel paymentMethodeModel, int pos) {

        isDialogCustome = true;

        paymentForList = paymentMethodeModel;
        setCustomeList(3);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (paymentMethodeModel.getPaymentMethodeSatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTipeExpenseList(List<String> tipeExpenseModel, int pos) {

        isDialogCustome = true;

        tipeExpenseGlobal = tipeExpenseModel;
        setCustomeList(4);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (tipeExpenseModel.size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDateTimeToday(String dateString, String timeString, String transID) {

        mDateString = dateString;
        mTimeString = timeString;
        mTransID = transID;

        mBinding.tvDate.setText(mDateString);
        mBinding.tvTime.setText(mTimeString);
    }

    @Override
    public void showSuccessAddTransaction(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                Intent intent = new Intent(mContext, TransactionActivity.class);
                pDialog.dismiss();
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).finish();
                }

            }
        });
    }


    private void showError(String message){

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                pDialog.dismiss();
            }
        });
    }

    private void setCustomeList(final int tipe) {

        if(tipe==1) {
            listCustomAdapter = new ListCustomAdapter(mContext, categoryForList, 1);
        }
        else if(tipe==2){

            listCustomAdapter = new ListCustomAdapter(mContext,productForList,3);
        }
        else if(tipe==3){

            listCustomAdapter = new ListCustomAdapter(mContext,paymentForList,4);
        }
        else if(tipe==4){
            listCustomAdapter = new ListCustomAdapter(mContext,tipeExpenseGlobal,7);
        }

        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //cat
                if (tipe == 1) {
                    if(!mCategory.equalsIgnoreCase(categoryForList.getCategorySatuanList().
                            get(position).getCategoryID())){
                        mProductTemp = "";
                        mProduct = "";
                        mResepTemp = "";
                        mResep = "";
                        mJumlahInt = 0;
                        mJumlahProdukTemp = "";
                        mJumlahProduk = "";

                        mBinding.tvProduct.setText("");
                        mBinding.tvJumlah.setText("");

                        detailTransactionModelGlobal = new DetailTransactionModel();
                        detailTransactionModelGlobalexp = new DetailTransactionModel();

                        detailTransactionAdapter.setNewItemList(detailTransactionModelGlobal);
                        detailTransactionAdapter.notifyDataSetChanged();
                        detailTransactionAdapterExp.setNewItemList(detailTransactionModelGlobalexp);
                        detailTransactionAdapterExp.notifyDataSetChanged();

                        if(detailTransactionList!=null && detailTransactionList.size()>0) {
                            detailTransactionList.clear();
                        }

                        if(detailTransactionListExp!=null && detailTransactionListExp.size()>0) {
                            detailTransactionListExp.clear();
                        }

                    }
                    mCategory = categoryForList.getCategorySatuanList().get(position).getCategoryID();
                    if(mCategory.equalsIgnoreCase("INCOME")){
                        mBinding.tvDetailProdukTitle.setText("Detail Product");
                        mBinding.rvTransactionAdd.setVisibility(View.VISIBLE);
                        mBinding.rvTransactionAddExpense.setVisibility(GONE);
                    }
                    else {
                        mBinding.tvDetailProdukTitle.setText("Detail Expense");
                        mBinding.rvTransactionAdd.setVisibility(GONE);
                        mBinding.rvTransactionAddExpense.setVisibility(View.VISIBLE);
                    }
                    mCategoryGeneral = categoryForList.getCategorySatuanList().get(position).getCategoryGeneral();
                    mBinding.tvCategory.setText(categoryForList.getCategorySatuanList().get(position).getCategoryName());
                }
                //product
                else if(tipe==2) {

                    if(mCategory.equalsIgnoreCase("INCOME")) {
                        mResepTemp = productForList.getProductSatuanList().get(position).getProductResepID();
                    }
                    mProductTemp = productForList.getProductSatuanList().get(position).getProductID();

                    mBinding.tvProduct.setText(productForList.getProductSatuanList().get(position).getProductName());

                }
                //payment
                else if(tipe==3){
                    mPaymentType = paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethodeID();
                    mBinding.tvPayment.setText(paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethode());
                }

                //tipe expense
                else if(tipe==4){

                    String temp = tipeExpenseGlobal.get(position);
                    if(temp.equalsIgnoreCase(K.TIPE_EXPENSE_BIAYA_LAINNYA)){

                        isDialogCustome = false;
                        isPrice = false;
                        isTipeExpense = true;

                        mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Tipe Expense Transaction(Max 100 char)");
                        if(mProductTemp.equalsIgnoreCase("")) {
                            mBinding.lyDoneEditText.etKarakter.setText("");
                        }
                        else {
                            mBinding.lyDoneEditText.etKarakter.setText(mProductTemp);
                        }
                        mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    }
                    else {
                        mProductTemp = temp;
                        mBinding.tvProduct.setText(mProductTemp);
                    }
                }

                isDialogCustome = false;
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    private void showDate() {

        myCalendar = Calendar.getInstance();
        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_STRING);

                mDateString = sdf.format(myCalendar.getTime());
                mBinding.tvDate.setText(mDateString);

            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTime(){
        myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        final int second = myCalendar.get(Calendar.SECOND);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minuteOfDay) {
                        String timeString = "", secondString = "";
                        if (second < 9) {
                            secondString = "0" + second;
                        } else {
                            secondString = second + "";
                        }

                        if (hourOfDay < 10) {
                            if (minuteOfDay < 10) {
                                timeString = "0" + hourOfDay + ":" + "0" + minuteOfDay + ":" + secondString;
                            } else {
                                timeString = "0" + hourOfDay + ":" + minuteOfDay + ":" + secondString;
                            }
                        } else {
                            if (minuteOfDay < 10) {
                                timeString = hourOfDay + ":" + "0" + minuteOfDay + ":" + secondString;
                            } else {
                                timeString = hourOfDay + ":" + minuteOfDay + ":" + secondString;
                            }
                        }

                        mTimeString = timeString;
                        mBinding.tvTime.setText(mTimeString);
                    }
                }, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}
