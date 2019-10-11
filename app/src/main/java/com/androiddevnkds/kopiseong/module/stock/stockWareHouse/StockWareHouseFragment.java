package com.androiddevnkds.kopiseong.module.stock.stockWareHouse;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.StockAdapter;
import com.androiddevnkds.kopiseong.databinding.FragmentStockWareHouseBinding;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
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
public class StockWareHouseFragment extends BaseFragment implements StockWHContract.stockView {

    private FragmentStockWareHouseBinding mBinding;
    private Context mContext;
    private StockWHPresenter stockWHPresenter;
    private StockAdapter stockAdapter;
    private StockModel stockModelGlobal, stockForList, stockForListAdd;
    private PaymentMethodeModel paymentForList;
    private String stockID = "", stockName = "", stockDate = "", stockDateSort = "";
    private long stockPrice = 0, stockJumlah = 0;
    private long stockPricePerGram = 0;
    private int positionDetail = 0;
    private StockModel.StockSatuanModel stockSatuanModelAdd;

    private Calendar myCalendar;
    private SimpleDateFormat sdf;

    private boolean isEditDetailStock = false, isAddStock = false, isDialogCustomeAdd = false, isDialogEditText = false;
    private boolean isAddJumlah = false, isAddPrice = false, isAddStockID = false, isAddNewStock = false, isAddStockName = false;

    private boolean isInitList = false;

    private ListCustomAdapter listCustomAdapter;
    private String addStockID = "", addStockName = "", addStockDateSort = "", addStockDate ="", addCategory ="", addPaymentMethode="", addTime ="";
    private long addStockPrice = 0, addStockJumlah = 0;

    private boolean isSellStock = false, isSellStockGram = false, isSellStockPrice = false;
    private String sellStockID = "", sellStockName = "", sellStockDateSort = "",sellStockDateSortUpdate = "", sellStockDate ="", sellCategory ="", sellPaymentMethode="", sellTime ="";
    private long sellStockPrice = 0, sellStockJumlah = 0, sellStockJumlahCurrent = 0, sellPerGram = 0;
    private int positionSell = 0;

    private boolean isSentStock = false, isSentStockGram = false, isSentStockID = false;
    private String sentStockID = "", sentStockName = "", sentStockDateSort = "", sentStockDate ="";
    private long sentStockPrice = 0, sentStockJumlah = 0, sentPerGram = 0, sentStockJumlahCurrent = 0;
    private int positionSent = 0;
    private boolean isDoneAddOnce = false;

    public StockWareHouseFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getArguments() != null) {
            if (getArguments().containsKey(K.KEY_MAIN_FIRST_TIME)) {

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_ware_house, container, false);
        stockWHPresenter = new StockWHPresenter(this);

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelText("Stock Warehouse");
        HeaderHelper.setLabelVisible(true);
        HeaderHelper.setLinearStockWHVisible(true);
        stockWHPresenter.getAllStock();
        mBinding.lyBottomNav.navigation.setSelectedItemId(R.id.stock_menu);
        stockSatuanModelAdd = new StockModel().new StockSatuanModel();
    }

    @Override
    public void initEvent() {

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        return true;
                    case R.id.transaction_menu:
                        Intent intentTrans = new Intent(mContext, TransactionActivity.class);
                        intentTrans.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(mContext, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentStock);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                }

                return false;
            }
        });

        mBinding.lyHeaderData.linearStockStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, StockActivity.class);
                intent.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_STORE);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });

        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEditDetailStock){
                    mBinding.lyDialogEditStock.lyDialogLayoutEditStock.setVisibility(View.GONE);
                    mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.VISIBLE);
                    isEditDetailStock = false;
                }
                else if(isAddStock){
                    Log.e("keklik ga",isAddStock+"");
                    if(isDialogCustomeAdd){

                        mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(View.VISIBLE);
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        isDialogCustomeAdd = false;
                    }
                    else if(isDialogEditText){
                        mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                        isDialogEditText = false;
                    }
                    else {
                        Log.e("keklik ga",isAddStock+"");
                        mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                        mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
                        isAddStock = false;
                    }
                }
                else if(isSellStock){
                    Log.e("keklik ga",isAddStock+"");
                    if(isDialogCustomeAdd){

                        mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.VISIBLE);
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        isDialogCustomeAdd = false;
                    }
                    else if(isDialogEditText){
                        mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                        isDialogEditText = false;
                    }
                    else {
                        Log.e("keklik ga",isAddStock+"");
                        mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                        mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
                        isSellStock = false;
                    }
                }

                else if(isSentStock){

                    if(isDialogCustomeAdd){

                        mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.VISIBLE);
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        isDialogCustomeAdd = false;
                    }
                    else if(isDialogEditText){
                        mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                        isDialogEditText = false;
                    }
                    else {
                        Log.e("keklik ga",isAddStock+"");
                        mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                        mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(GONE);
                        isSentStock = false;
                    }
                }

                else {
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                    mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
                }

                try {
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        imm = (InputMethodManager) Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE));
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //----------------------------------------edit stock------------------------------------
        mBinding.lyDialogEditStock.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stockDate = mBinding.lyDialogEditStock.tvStockDate.getText().toString().trim();
                String tempjumlah = mBinding.lyDialogEditStock.etStockJumlah.getText().toString().trim().replace(",","");
                String tempPrice = mBinding.lyDialogEditStock.etStockPrice.getText().toString().trim().replace(",","");

                long stockJumlah = 0, stockPrice = 0;
                try {
                    stockPrice = Long.parseLong(tempPrice);
                    stockJumlah = Integer.parseInt(tempjumlah);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();

                stockSatuanModel.setStockID(stockModelGlobal.getStockSatuanModelList().get(positionDetail).getStockID());
                stockSatuanModel.setStockDateSort(stockModelGlobal.getStockSatuanModelList().get(positionDetail).getStockDateSort());
                stockSatuanModel.setStockDate(stockDate);
                stockSatuanModel.setStockGram(stockJumlah);
                stockSatuanModel.setStockPrice(stockPrice);

                stockWHPresenter.editStock(stockSatuanModel,positionDetail);
            }
        });

        mBinding.lyDialogEditStock.ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //------------------------------------------edit  stock finish-------------------------------------------------------------

        //----------------------------------------- stock add ---------------------------------------------------
        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateAndTime dateAndTime = new DateAndTime();
                addStockDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
                mBinding.lyDialogAddStock.tvDate.setText(addStockDate);

                addStockDateSort = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT);
                addTime = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
                addStockDateSort = addStockDateSort + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);

                addCategory = K.ID_CATEGORY_STOCK_BUY;

                isAddStock = true;
                isSellStock = false;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(View.VISIBLE);

                Log.e("BASE","ADD");
            }
        });

        //id
        mBinding.lyDialogAddStock.layoutRelatifStockID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAddStockID = true;
                stockWHPresenter.setStockListForAdd(stockForListAdd, addStockID,true);
            }
        });

        //name
        mBinding.lyDialogAddStock.layoutRelatifNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!addStockID.equalsIgnoreCase("") && isAddNewStock){
                    isAddStockName = true;
                    isDialogEditText = true;
                    mBinding.lyDoneEditText.tvEditTextLabel.setText("Input New Stock Name");
                    mBinding.lyDoneEditText.etKarakter.setText(addStockName);
                    mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                    mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
                }
            }
        });

        //date
        mBinding.lyDialogAddStock.layoutRelatifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //gram
        mBinding.lyDialogAddStock.layoutRelatifGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAddJumlah = true;
                isDialogEditText = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Jumlah Stock With Number Only");
                if(addStockJumlah>0) {
                    mBinding.lyDoneEditText.etNumber.setText(addStockJumlah + "");
                }
                else {
                    mBinding.lyDoneEditText.etNumber.setText( "");

                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);

            }
        });

        //price
        mBinding.lyDialogAddStock.layoutRelatifPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAddPrice = true;
                isDialogEditText = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Price Stock With Number Only");
                if(addStockPrice>0) {
                    mBinding.lyDoneEditText.etNumber.setText(addStockPrice + "");
                }
                else {
                    mBinding.lyDoneEditText.etNumber.setText("");
                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
            }
        });

        mBinding.lyDialogAddStock.layoutRelatifTipeBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stockWHPresenter.getAllPayment(paymentForList,addPaymentMethode);
            }
        });

        //submit
        mBinding.lyDialogAddStock.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();
                stockSatuanModel.setStockDateSort(addStockDateSort);
                stockSatuanModel.setStockID(addStockID);
                stockSatuanModel.setStockName(addStockName);
                stockSatuanModel.setStockGram(addStockJumlah);
                stockSatuanModel.setStockPrice(addStockPrice);
                stockSatuanModel.setStockDate(addStockDate);

                stockWHPresenter.addNewStock(stockSatuanModel,addPaymentMethode,addCategory,addTime);
            }
        });

        //------------------------------------------ add stock finish --------------------------------------------------------------

        // ----------------------------------------- sell stock ---------------------------------------------------------------

        mBinding.btnSellStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateAndTime dateAndTime = new DateAndTime();
                sellStockDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
                mBinding.lyDialogSellStock.tvDate.setText(sellStockDate);

                sellStockDateSort = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT);
                sellTime = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
                sellStockDateSort = sellStockDateSort + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);

                sellCategory = K.ID_CATEGORY_STOCK_SELL;

                isSellStock = true;
                isAddStock = false;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.VISIBLE);

                Log.e("BASE","SELL");

            }
        });

        //id
        mBinding.lyDialogSellStock.layoutRelatifStockID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stockWHPresenter.setStockList(stockModelGlobal, sellStockID);
            }
        });

        //gram
        mBinding.lyDialogSellStock.layoutRelatifGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sellStockID.equalsIgnoreCase("")) {
                    showError("Please Choose StockID First");
                } else {
                    isSellStockGram = true;
                    isDialogEditText = true;

                    mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Jumlah Stock With Number Only (Max : " + sellStockJumlahCurrent + " gr)");
                    if (sellStockJumlah > 0) {
                        mBinding.lyDoneEditText.etNumber.setText(sellStockJumlah + "");
                    } else {
                        mBinding.lyDoneEditText.etNumber.setText("");

                    }
                    mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                    mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
                }
            }
        });

        //price
        mBinding.lyDialogSellStock.layoutRelatifPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isSellStockPrice = true;
                isDialogEditText = true;

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Price Stock With Number Only");
                if(sellStockPrice>0) {
                    mBinding.lyDoneEditText.etNumber.setText(sellStockPrice + "");
                }
                else {
                    mBinding.lyDoneEditText.etNumber.setText("");
                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
            }
        });

        //date
        mBinding.lyDialogSellStock.layoutRelatifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        //payment
        mBinding.lyDialogSellStock.layoutRelatifTipeBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stockWHPresenter.getAllPayment(paymentForList,addPaymentMethode);
            }
        });

        //submit
        mBinding.lyDialogSellStock.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();
                stockSatuanModel.setStockDateSort(sellStockDateSortUpdate);
                stockSatuanModel.setStockID(sellStockID);
                stockSatuanModel.setStockName(sellStockName);
                stockSatuanModel.setStockGram(sellStockJumlahCurrent);
                stockSatuanModel.setStockPrice(sellStockPrice);
                stockSatuanModel.setStockPricePerGram(sellPerGram);
                stockSatuanModel.setStockDate(sellStockDate);

                stockWHPresenter.sellStock(stockSatuanModel,sellStockDateSort,sellPaymentMethode,
                        sellCategory,sellTime,sellStockJumlah,positionSell);
            }
        });

        //----------------------------------------- sell stock finish -----------------------------------------------

        //---------------------------------------- sent stock to store ------------------------------------------

        mBinding.btnSentStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateAndTime dateAndTime = new DateAndTime();
                sentStockDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
//                sentStockDateSort = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT);
//
//                sentStockDateSort = sentStockDateSort + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);

                isSentStock = true;

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.VISIBLE);

                Log.e("BASE","SEnt");

            }
        });

        //id
        mBinding.lyDialogSentStock.layoutRelatifStockID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stockWHPresenter.setStockList(stockModelGlobal, sellStockID);
            }
        });

        //jumlah
        mBinding.lyDialogSentStock.layoutRelatifGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sentStockID.equalsIgnoreCase("")) {

                    showError("Please Choose StockID First");

                } else {
                    isSentStockGram = true;
                    isDialogEditText = true;

                    mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Jumlah Stock With Number Only (Max : " + sentStockJumlahCurrent + " gr)");
                    if (sentStockJumlah > 0) {
                        mBinding.lyDoneEditText.etNumber.setText(sentStockJumlah + "");
                    } else {
                        mBinding.lyDoneEditText.etNumber.setText("");

                    }
                    mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                    mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(GONE);
                }
            }
        });

        mBinding.lyDialogSentStock.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();
                stockSatuanModel.setStockDateSort(sentStockDateSort);
                stockSatuanModel.setStockID(sentStockID);
                stockSatuanModel.setStockName(sentStockName);
                stockSatuanModel.setStockGram(sentStockJumlahCurrent);
                stockSatuanModel.setStockPrice(sentStockPrice);
                stockSatuanModel.setStockPricePerGram(sentPerGram);
                stockSatuanModel.setStockDate(sentStockDate);

                stockWHPresenter.sentStock(stockSatuanModel,sentStockJumlah,positionSent);
            }
        });


        //---------------------------------------- sent stock finish ---------------------------------------------
        //button submit
        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isAddJumlah){

                    String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");
                    mBinding.lyDialogAddStock.tvJumlahStock.setText(temp+" gram");
                    temp = temp.replace(",","");
                    try {
                        addStockJumlah = Integer.parseInt(temp);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if(isAddPrice){
                    String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");;
                    MataUangHelper mataUangHelper = new MataUangHelper();

                    try {
                        addStockPrice = Long.parseLong(temp);
                        mBinding.lyDialogAddStock.tvPrice.setText(mataUangHelper.formatRupiah(addStockPrice));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if(isAddStockID){

                    String temp = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    if(temp.length()>40){
                        temp = temp.substring(0,40);
                    }
                    addStockID = temp;
                    stockSatuanModelAdd.setStockID(temp);
                    if(stockSatuanModelAdd.getStockName()!=null &&
                            !stockSatuanModelAdd.getStockName().equalsIgnoreCase("") && !isDoneAddOnce) {
                        stockForListAdd.getStockSatuanModelList().add(stockForListAdd.getStockSatuanModelList().size()-1,stockSatuanModelAdd);
                        isDoneAddOnce = true;
                    }
                    mBinding.lyDialogAddStock.tvStockId.setText(addStockID);
                }

                else if(isAddStockName){
                    addStockName = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    mBinding.lyDialogAddStock.tvStockName.setText(addStockName);
                    stockSatuanModelAdd.setStockName(addStockName);
                    if(stockSatuanModelAdd.getStockID()!=null &&
                            !stockSatuanModelAdd.getStockID().equalsIgnoreCase("") && !isDoneAddOnce) {
                        stockForListAdd.getStockSatuanModelList().add(stockForListAdd.getStockSatuanModelList().size()-1,stockSatuanModelAdd);
                        isDoneAddOnce = true;
                    }
                }
                //sellll
                else if(isSellStockGram){

                    String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");
                    mBinding.lyDialogSellStock.tvJumlahStock.setText(temp+" gram");
                    temp = temp.replace(",","");
                    try {
                        sellStockJumlah = Integer.parseInt(temp);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if(isSellStockPrice){

                    String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");;
                    MataUangHelper mataUangHelper = new MataUangHelper();

                    try {
                        sellStockPrice = Long.parseLong(temp);
                        mBinding.lyDialogSellStock.tvPrice.setText(mataUangHelper.formatRupiah(sellStockPrice));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                //sent
                else if(isSentStockGram){

                    String temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");
                    mBinding.lyDialogSentStock.tvJumlahStock.setText(temp+" gram");
                    temp = temp.replace(",","");
                    try {
                        sentStockJumlah = Integer.parseInt(temp);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if(isAddStock) {
                    mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(View.VISIBLE);
                }
                else if(isSellStock){
                    mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.VISIBLE);
                }
                else {
                    mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.VISIBLE);
                }

                try {
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);

                isAddJumlah = false;
                isDialogEditText = false;
                isAddPrice = false;
                isAddStockID = false;
                isAddStockName = false;
                isSellStockGram = false;
                isSellStockPrice = false;
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
    public void showAllStock(final StockModel stockModel) {

        stockModelGlobal = stockModel;

        stockAdapter = new StockAdapter(mContext, stockModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvStock.setLayoutManager(layoutManager);
        mBinding.rvStock.setAdapter(stockAdapter);

        stockAdapter.setOnItemClickListener(new StockAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                positionDetail = position;
                stockWHPresenter.setOnClickStock(stockModel, position);
                Log.e("TES", "TES");
            }
        });

    }

    @Override
    public void onFailed(final int tipe, String message) {

        showError(message);
        hideProgressBar();
        //delete
    }

    @Override
    public void onFailed(int tipe, String message, boolean fromList) {

        if(message.equalsIgnoreCase("stock not found !") && fromList){

            isDialogCustomeAdd = true;
            isAddStock = true;
            mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
            mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
            StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();
            stockSatuanModel.setStockID(K.ADD_NEW_STOCK);
            stockSatuanModel.setStockName(K.ADD_NEW_STOCK);
            stockForListAdd = new StockModel();
            List<StockModel.StockSatuanModel> stockSatuanModelList = new ArrayList<>();
            stockSatuanModelList.add(stockSatuanModel);
            stockForListAdd.setStockSatuanModelList(stockSatuanModelList);
            isInitList = true;
            setCustomeList(3);
        }
        else {
            showError(message);
        }
    }

    @Override
    public void showStockDetail(final StockModel.StockSatuanModel stockSatuanModel, final int position) {

        stockDateSort = stockSatuanModel.getStockDateSort();
        stockID = stockSatuanModel.getStockID();
        stockName = stockSatuanModel.getStockName();
        stockDate = stockSatuanModel.getStockDate();
        stockPrice = stockSatuanModel.getStockPrice();
        stockJumlah = stockSatuanModel.getStockGram();
        stockPricePerGram = stockSatuanModel.getStockPricePerGram();

        final MataUangHelper mataUangHelper = new MataUangHelper();

        mBinding.lyDialogDetailStock.tvStockId.setText(stockID);
        mBinding.lyDialogDetailStock.tvStockName.setText(stockName);
        mBinding.lyDialogDetailStock.tvStockDate.setText(stockDate);
        mBinding.lyDialogDetailStock.tvStockJumlah.setText(stockJumlah + " gram");
        mBinding.lyDialogDetailStock.tvStockPrice.setText(mataUangHelper.formatRupiah(stockPrice));
        mBinding.lyDialogDetailStock.tvStockHpp.setText(mataUangHelper.formatRupiah(stockPricePerGram));

        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.VISIBLE);

        mBinding.lyDialogDetailStock.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
            }
        });

        mBinding.lyDialogDetailStock.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Delete Warning");
                pDialog.setContentText("Are you sure you want to delete "+stockSatuanModel.getStockID()+" ?");
                pDialog.setCancelText("No");
                pDialog.setConfirmText("Yes");
                pDialog.showCancelButton(true);
                pDialog.show();
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("Cancelled!")
                                .setContentText("Your stock is safe :)")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        stockWHPresenter.deleteStock(stockID,stockDateSort, positionDetail);
                        pDialog.dismiss();
                    }
                });

            }
        });

        mBinding.lyDialogDetailStock.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditDetailStock = true;
                mBinding.lyDialogEditStock.etStockJumlah.setText(stockSatuanModel.getStockGram()+"");
                mBinding.lyDialogEditStock.etStockPrice.setText(stockSatuanModel.getStockPrice()+"");
                mBinding.lyDialogEditStock.tvStockName.setText(stockSatuanModel.getStockName());
                mBinding.lyDialogEditStock.tvStockDate.setText(stockSatuanModel.getStockDate());
                mBinding.lyDialogEditStock.lyDialogLayoutEditStock.setVisibility(View.VISIBLE);
                mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showSuccessDelete(final int position, final String message) {

        //delete
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                stockModelGlobal.getStockSatuanModelList().remove(position);
                stockAdapter.resetStock(stockModelGlobal);
                stockAdapter.notifyDataSetChanged();
                mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessEditStock(final String message, final StockModel.StockSatuanModel stockSatuanModel, final int pos) {

        final String[] split = message.trim().split("---");
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(split[0]);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                Log.e("STOCK",split[1]);
                long temp = 0;
                try {
                    temp = Long.parseLong(split[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Log.e("STOCK",temp+"");
                stockModelGlobal.getStockSatuanModelList().get(pos).setStockDate(stockSatuanModel.getStockDate());
                stockModelGlobal.getStockSatuanModelList().get(pos).setStockPrice(stockSatuanModel.getStockPrice());
                stockModelGlobal.getStockSatuanModelList().get(pos).setStockGram(stockSatuanModel.getStockGram());
                stockModelGlobal.getStockSatuanModelList().get(pos).setStockPricePerGram(temp);

                stockAdapter.resetStock(stockModelGlobal);
                stockAdapter.notifyDataSetChanged();
                mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(GONE);
                mBinding.lyDialogEditStock.lyDialogLayoutEditStock.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);

                pDialog.dismiss();
                isEditDetailStock = false;
            }
        });
    }

    @Override
    public void showStockCustomeList(StockModel stockModel, int pos) {

        isDialogCustomeAdd = true;
        if(isAddStock) {
            mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
        }
        else {
            mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
        }
        stockForList = stockModel;
        setCustomeList(1);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (stockModel.getStockSatuanModelList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStockCustomeListAdd(StockModel stockModel, int pos) {

        isDialogCustomeAdd = true;
        if(isAddStock) {
            mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
        }
        else {
            mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
        }
        stockForListAdd = stockModel;
        setCustomeList(3);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (stockModel.getStockSatuanModelList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessAddStock(final String message, final StockModel.StockSatuanModel stockSatuanModel) {

        final String[] splited = message.trim().split("---");
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(splited[0]);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                isAddStock = false;
                addStockID ="";
                addStockDate ="";
                addStockDateSort = "";
                addStockJumlah = 0;
                addStockName = "";
                addStockPrice = 0;

                mBinding.lyDialogAddStock.tvStockId.setText("");
                mBinding.lyDialogAddStock.tvStockName.setText("");
                mBinding.lyDialogAddStock.tvDate.setText("");
                mBinding.lyDialogAddStock.tvPrice.setText("");
                mBinding.lyDialogAddStock.tvJumlahStock.setText("");
                mBinding.lyDialogAddStock.tvTipeBayar.setText("");

                long perGram = 0;
                try {
                    perGram = Long.parseLong(splited[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                stockSatuanModel.setStockPricePerGram(perGram);
                if(stockModelGlobal!=null && stockModelGlobal.getStockSatuanModelList()!=null) {
                    stockModelGlobal.getStockSatuanModelList().add(0, stockSatuanModel);
                    stockAdapter.resetStock(stockModelGlobal);
                    stockAdapter.notifyDataSetChanged();
                    mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(GONE);
                }
                else {
                    Intent intentStock = new Intent(mContext, StockActivity.class);
                    intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                    startActivity(intentStock);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessSellStock(String message, final int position) {

        final String[] splited = message.trim().split("---");
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(splited[0]);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                isSellStock = false;
                sellStockID ="";
                sellStockDate ="";
                sellStockDateSort = "";
                sellStockJumlah = 0;
                sellStockName = "";
                sellStockPrice = 0;

                mBinding.lyDialogSellStock.tvStockId.setText("");
                mBinding.lyDialogSellStock.tvDate.setText("");
                mBinding.lyDialogSellStock.tvPrice.setText("");
                mBinding.lyDialogSellStock.tvJumlahStock.setText("");
                mBinding.lyDialogSellStock.tvTipeBayar.setText("");

                int newGram = 0;
                try {
                    newGram = Integer.parseInt(splited[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if(newGram>0){
                    stockModelGlobal.getStockSatuanModelList().get(position).setStockGram(newGram);
                }
                else {
                    stockModelGlobal.getStockSatuanModelList().remove(position);
                }

                stockAdapter.resetStock(stockModelGlobal);
                stockAdapter.notifyDataSetChanged();
                mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessSentStock(String message, final int position) {

        final String[] splited = message.trim().split("---");
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(splited[0]);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                isSentStock = false;
                sentStockID ="";
                sentStockDate ="";
                sentStockDateSort = "";
                sentStockJumlah = 0;
                sentStockName = "";
                sentStockPrice = 0;

                mBinding.lyDialogSentStock.tvStockId.setText("");
                mBinding.lyDialogSentStock.tvJumlahStock.setText("");

                int newGram = 0;
                try {
                    newGram = Integer.parseInt(splited[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if(newGram>0){
                    stockModelGlobal.getStockSatuanModelList().get(position).setStockGram(newGram);
                }
                else {
                    stockModelGlobal.getStockSatuanModelList().remove(position);
                }

                stockAdapter.resetStock(stockModelGlobal);
                stockAdapter.notifyDataSetChanged();
                mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showAllPayment(PaymentMethodeModel paymentMethodeModel, int pos) {

        isDialogCustomeAdd = true;
        if(isAddStock) {
            mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);
        }
        else {
            mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(GONE);
        }
        paymentForList = paymentMethodeModel;
        setCustomeList(2);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (paymentMethodeModel.getPaymentMethodeSatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }


    private void showDate(){

        myCalendar = Calendar.getInstance();
        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_STRING);
                if(isEditDetailStock) {
                    mBinding.lyDialogEditStock.tvStockDate.setText(sdf.format(myCalendar.getTime()));
                }
                else if(isAddStock) {
                    addStockDate = sdf.format(myCalendar.getTime());
                    mBinding.lyDialogAddStock.tvDate.setText(addStockDate);

                    sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_SORT);
                    addStockDateSort = sdf.format(myCalendar.getTime());
                    DateAndTime dateAndTime = new DateAndTime();
                    addStockDateSort = addStockDateSort + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
                    addTime = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
                }
                else if(isSellStock){

                    sellStockDate = sdf.format(myCalendar.getTime());
                    mBinding.lyDialogSellStock.tvDate.setText(sellStockDate);

                    sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_SORT);
                    sellStockDateSort = sdf.format(myCalendar.getTime());
                    DateAndTime dateAndTime = new DateAndTime();
                    sellStockDateSort = sellStockDateSort + dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
                    sellTime = dateAndTime.getCurrentTime(K.FORMAT_TIME_STRING);
                }
                else {

                    sentStockDate = sdf.format(myCalendar.getTime());
                }

            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void setCustomeList(final int tipe) {

        if(tipe==1) {
            listCustomAdapter = new ListCustomAdapter(mContext, stockForList, 6);
        }
        else if(tipe==2){
            listCustomAdapter = new ListCustomAdapter(mContext,paymentForList,4);
        }
        else if(tipe==3){
            if (!isInitList) {
                StockModel.StockSatuanModel stockSatuanModel = new StockModel().new StockSatuanModel();
                stockSatuanModel.setStockID(K.ADD_NEW_STOCK);
                stockSatuanModel.setStockName(K.ADD_NEW_STOCK);
                stockForListAdd.getStockSatuanModelList().add(stockSatuanModel);
                isInitList = true;
            }
            listCustomAdapter = new ListCustomAdapter(mContext, stockForListAdd, 5);
        }

        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (tipe == 1) {

                    if(isSellStock) {
                        sellStockID = stockForList.getStockSatuanModelList().get(position).getStockID();
                        sellStockName = stockForList.getStockSatuanModelList().get(position).getStockName();
                        sellStockDateSortUpdate = stockForList.getStockSatuanModelList().get(position).getStockDateSort();
                        sellStockJumlahCurrent = stockForList.getStockSatuanModelList().get(position).getStockGram();
                        sellPerGram = stockForList.getStockSatuanModelList().get(position).getStockPricePerGram();

                        positionSell = position;
                        mBinding.lyDialogSellStock.tvStockId.setText(sellStockID);
                    }
                    else {

                        sentStockID = stockForList.getStockSatuanModelList().get(position).getStockID();
                        sentStockName = stockForList.getStockSatuanModelList().get(position).getStockName();
                        sentStockDateSort = stockForList.getStockSatuanModelList().get(position).getStockDateSort();
                        sentStockJumlahCurrent = stockForList.getStockSatuanModelList().get(position).getStockGram();
                        sentPerGram = stockForList.getStockSatuanModelList().get(position).getStockPricePerGram();
                        sentStockPrice = stockForList.getStockSatuanModelList().get(position).getStockPrice();

                        positionSent = position;
                        mBinding.lyDialogSentStock.tvStockId.setText(sentStockID);
                    }
                    isDialogCustomeAdd = false;
                }
                else if(tipe==2) {

                    if(isAddStock) {
                        addPaymentMethode = paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethodeID();
                        mBinding.lyDialogAddStock.tvTipeBayar.setText(paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethode());
                    }
                    else {
                        sellPaymentMethode = paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethodeID();
                        mBinding.lyDialogSellStock.tvTipeBayar.setText(paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethode());
                    }
                }
                else if(tipe==3){
                    if (stockForListAdd.getStockSatuanModelList().get(position).getStockID().equalsIgnoreCase(K.ADD_NEW_STOCK)) {

                        isAddNewStock = true;
                        isDoneAddOnce = false;
                        isDialogEditText = true;
                        isDialogCustomeAdd = false;
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(GONE);

                        mBinding.lyDoneEditText.tvEditTextLabel.setText("Please fill StockID without space");
                        mBinding.lyDoneEditText.etKarakter.setText("");
                        mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                        mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    } else {
                        isAddNewStock = false;
                        addStockID = stockForListAdd.getStockSatuanModelList().get(position).getStockID();
                        addStockName = stockForListAdd.getStockSatuanModelList().get(position).getStockName();

                        mBinding.lyDialogAddStock.tvStockId.setText(addStockID);
                        mBinding.lyDialogAddStock.tvStockName.setText(addStockName);
                    }
                }

                isDialogCustomeAdd = false;
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                if(isAddStock) {
                    mBinding.lyDialogAddStock.lyDialogLayoutAddStock.setVisibility(View.VISIBLE);
                }
                else if(isSellStock){
                    mBinding.lyDialogSellStock.lyDialogLayoutSellStock.setVisibility(View.VISIBLE);
                }
                else {
                    mBinding.lyDialogSentStock.lyDialogLayoutSentStock.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
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
}
