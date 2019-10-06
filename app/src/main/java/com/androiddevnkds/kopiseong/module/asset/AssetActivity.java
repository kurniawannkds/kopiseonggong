package com.androiddevnkds.kopiseong.module.asset;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.AssetAdapter;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.ProductAdapter;
import com.androiddevnkds.kopiseong.databinding.ActivityAssetBinding;
import com.androiddevnkds.kopiseong.databinding.ActivityProductBinding;
import com.androiddevnkds.kopiseong.model.AssetModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.product.ProductContract;
import com.androiddevnkds.kopiseong.module.product.ProductPresenter;
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

public class AssetActivity extends BaseActivity implements AssetContract.assetView {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityAssetBinding mBinding;
    private AssetPresenter assetPresenter;
    private AssetAdapter assetAdapter;
    private ListCustomAdapter listCustomAdapter;
    private AssetModel assetModelGlobal, assetForList;
    private PaymentMethodeModel paymentForList;
    private AssetModel.AssetModelSatuan assetModelSatuanGlobal;
    private int positionDetail = 0;
    private boolean isDetailAdd = false,isSell=false, isDialogCustome = false, isEditText = false, isInitList = false, isPrice = false, isPercent = false;
    private boolean isAssetName = false;
    private String assetID = "", assetName = "", assetDate = "", mTipeBayar;
    private long assetPrice = 0, assetcCurrentPrice = 0;
    private int assetPercent = 0;

    private Calendar myCalendar;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_asset);
        assetPresenter = new AssetPresenter(this);

        initUI();
        initEvent();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelContentText("Asset");
        HeaderHelper.setRelativeContentVisible(true);
        HeaderHelper.setLabelContentVisible(true);
        assetPresenter.getAllAsset();

    }

    @Override
    public void initEvent() {

        mBinding.lyHeaderData.tvIconFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent intent = new Intent(AssetActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.transaction_menu:
                        Intent intentTrans = new Intent(AssetActivity.this, TransactionActivity.class);
                        intentTrans.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
                        finish();

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(AssetActivity.this, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentStock);
                        finish();

                        return true;
                }

                return false;
            }
        });

        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isDialogCustome) {
                    mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                    mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
                    isDialogCustome = false;
                } else if (isEditText) {
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                    mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
                    isEditText = false;
                } else {
                    mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(GONE);
                }

                isSell = false;
                isDetailAdd = false;
                try {
                    InputMethodManager imm = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        imm = (InputMethodManager) Objects.requireNonNull(Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE)));
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateAndTime dateAndTime = new DateAndTime();
                isDetailAdd = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);

                assetID = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT) +
                        dateAndTime.getCurrentTime(K.FORMAT_TIME_SORT);
                assetName = "";
                assetDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
                assetPrice = 0;
                assetPercent = 0;
                assetcCurrentPrice = 0;

                mBinding.lyDetailAsset.ivDelete.setVisibility(GONE);
                mBinding.lyDetailAsset.ivSell.setVisibility(GONE);
                mBinding.lyDetailAsset.tvTitle.setText("Add Asset Detail");
                mBinding.lyDetailAsset.tvAssetName.setText(assetName);
                mBinding.lyDetailAsset.tvAssetDate.setText(assetDate);
                mBinding.lyDetailAsset.tvAssetPrice.setText("");
                mBinding.lyDetailAsset.tvAssetPercent.setText("");
                mBinding.lyDetailAsset.tvAssetCurrent.setText("");

                mBinding.lyDetailAsset.layoutRelatifMethode.setVisibility(GONE);
                mBinding.lyDetailAsset.layoutRelatifCurrent.setVisibility(View.VISIBLE);
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
            }
        });

        //-----------------------------------------detail-------------------------------------------

        //delete
        mBinding.lyDetailAsset.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Are you sure want to DELETE this detail ?");
                pDialog.setCancelText("No");
                pDialog.setConfirmText("Yes");
                pDialog.showCancelButton(true);
                pDialog.show();
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        assetPresenter.deleteAsset(assetID,positionDetail);
                        pDialog.dismiss();
                    }
                });
            }
        });

        //sell
        mBinding.lyDetailAsset.ivSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Are you sure want to SELL this detail ?");
                pDialog.setCancelText("No");
                pDialog.setConfirmText("Yes");
                pDialog.showCancelButton(true);
                pDialog.show();
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        isSell = true;
                        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                        assetPresenter.getTipeBayarList(paymentForList, mTipeBayar);
                        pDialog.dismiss();
                    }
                });
            }
        });

        //asset name
        mBinding.lyDetailAsset.layoutRelatifName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isDetailAdd) {
                    mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                    assetPresenter.getAssetForList(assetForList,assetName);
                }
                else {

                    isAssetName = true;
                    isEditText = true;
                    mBinding.lyDoneEditText.tvEditTextLabel.setText("Please Fill AssetName");
                    mBinding.lyDoneEditText.etKarakter.setText("");
                    mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                    mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                }
            }
        });

        //methode
        mBinding.lyDetailAsset.layoutRelatifMethode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isDetailAdd) {
                    mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                    assetPresenter.getTipeBayarList(paymentForList, mTipeBayar);
                }
            }
        });

        //date
        mBinding.lyDetailAsset.layoutRelatifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //percent
        mBinding.lyDetailAsset.layoutRelatifPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditText = true;
                isPercent = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Percent With Number Only[1-99]");
                if (assetPercent != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(assetPercent + "");
                } else {
                    mBinding.lyDoneEditText.etNumber.setText("");

                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
            }
        });

        //price
        mBinding.lyDetailAsset.layoutRelatifPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditText = true;
                isPrice = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Price With Number Only");
                if (assetPrice != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(assetPrice + "");
                } else {
                    mBinding.lyDoneEditText.etNumber.setText("");

                }
                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
            }
        });

        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempNumber = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");
                String temp = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();

                if (isPrice) {

                    try {
                        assetPrice = Integer.parseInt(tempNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    MataUangHelper mataUangHelper = new MataUangHelper();
                    mBinding.lyDetailAsset.tvAssetPrice.setText(mataUangHelper.formatRupiah(assetPrice));
                    isPrice = false;
                }

                else if (isPercent) {

                    try {
                        assetPercent = Integer.parseInt(tempNumber);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    mBinding.lyDetailAsset.tvAssetPercent.setText(assetPercent +" %");
                    isPercent = false;
                }

                else if (isAssetName) {

                    assetName = temp;
                    mBinding.lyDetailAsset.tvAssetName.setText(assetName);
                    isAssetName = false;
                }

                try {
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        imm = (InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE));
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isEditText =false;
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);

                if(isSell){

                    assetPresenter.sellAsset(assetID,assetPrice,mTipeBayar,positionDetail);
                    isSell = false;
                }
                else {
                    mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
                }
            }
        });

        mBinding.lyDetailAsset.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetModel.AssetModelSatuan assetModelSatuan = new AssetModel().new AssetModelSatuan();
                assetModelSatuan.setAssetID(assetID);
                assetModelSatuan.setAssetName(assetName);
                assetModelSatuan.setAssetDate(assetDate);
                assetModelSatuan.setAssetPercent(assetPercent);
                assetModelSatuan.setAssetPrice(assetPrice);
                if (isDetailAdd) {

                    assetPresenter.addAsset(assetModelSatuan, mTipeBayar);
                } else {

                    if (assetID.equalsIgnoreCase(assetModelSatuanGlobal.getAssetID()) &&
                            assetName.equalsIgnoreCase(assetModelSatuanGlobal.getAssetName()) &&
                            assetDate.equalsIgnoreCase(assetModelSatuanGlobal.getAssetDate()) &&
                            assetPercent == assetModelSatuanGlobal.getAssetPercent() &&
                            assetPrice == assetModelSatuanGlobal.getAssetPrice()) {

                        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                        mBinding.lyBlack.lyBlack.setVisibility(GONE);
                    } else {

                        assetPresenter.editAsset(assetModelSatuan, positionDetail);
                    }
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
    public void onFailed(String message) {

        showError(message);
    }

    @Override
    public void showAllAsset(final AssetModel assetModel) {

        assetModelGlobal = assetModel;
        assetAdapter = new AssetAdapter(this, assetModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvAsset.setLayoutManager(layoutManager);
        mBinding.rvAsset.setAdapter(assetAdapter);

        assetAdapter.setOnItemClickListener(new AssetAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                positionDetail = position;
                assetPresenter.setOnCLickAsset(assetModel, position);
            }
        });

    }

    @Override
    public void showAssetList(AssetModel assetModel, int pos) {

        isDialogCustome = true;

        assetForList = assetModel;
        setCustomeList(1);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (assetModel.getAssetModelSatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPaymentList(PaymentMethodeModel paymentMethodeModel, int pos) {

        isDialogCustome = true;

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

    @Override
    public void showAssetDetail(AssetModel.AssetModelSatuan assetModelSatuan, int pos, long curPrice) {

        isDetailAdd = false;
        assetModelSatuanGlobal = assetModelSatuan;
        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        MataUangHelper mataUangHelper = new MataUangHelper();

        assetID = assetModelSatuan.getAssetID();
        assetName = assetModelSatuan.getAssetName();
        assetDate = assetModelSatuan.getAssetDate();
        assetPrice = assetModelSatuan.getAssetPrice();
        assetPercent = assetModelSatuan.getAssetPercent();
        assetcCurrentPrice = curPrice;

        mBinding.lyDetailAsset.ivDelete.setVisibility(View.VISIBLE);
        mBinding.lyDetailAsset.ivSell.setVisibility(View.VISIBLE);
        mBinding.lyDetailAsset.tvTitle.setText("Asset Detail");
        mBinding.lyDetailAsset.tvAssetName.setText(assetName);
        mBinding.lyDetailAsset.tvAssetDate.setText(assetDate);
        mBinding.lyDetailAsset.tvAssetPrice.setText(mataUangHelper.formatRupiah(assetPrice));
        mBinding.lyDetailAsset.tvAssetPercent.setText(assetPercent + " %");
        mBinding.lyDetailAsset.tvAssetCurrent.setText(mataUangHelper.formatRupiah(assetcCurrentPrice));
        mBinding.lyDetailAsset.layoutRelatifMethode.setVisibility(GONE);
        mBinding.lyDetailAsset.layoutRelatifCurrent.setVisibility(View.VISIBLE);

        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessAdd(String message, final AssetModel.AssetModelSatuan assetModelSatuan) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                assetModelGlobal.getAssetModelSatuanList().add(assetModelSatuan);
                assetAdapter.resetListTransaction(assetModelGlobal);
                assetAdapter.notifyDataSetChanged();
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessSell(String message, final int pos) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                assetModelGlobal.getAssetModelSatuanList().remove(pos);
                assetAdapter.resetListTransaction(assetModelGlobal);
                assetAdapter.notifyDataSetChanged();
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessEdit(String message, final AssetModel.AssetModelSatuan assetModelSatuan, final int pos) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                assetModelGlobal.getAssetModelSatuanList().remove(pos);
                assetModelGlobal.getAssetModelSatuanList().add(pos, assetModelSatuan);
                assetAdapter.resetListTransaction(assetModelGlobal);
                assetAdapter.notifyDataSetChanged();
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessDelete(final String message, final int pos) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                assetModelGlobal.getAssetModelSatuanList().remove(pos);
                assetAdapter.resetListTransaction(assetModelGlobal);
                assetAdapter.notifyDataSetChanged();
                mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    private void showError(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(AssetActivity.this, SweetAlertDialog.ERROR_TYPE);
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

        if (tipe == 1) {
            if (!isInitList) {
                AssetModel.AssetModelSatuan assetModelSatuan = new AssetModel().new AssetModelSatuan();
                assetModelSatuan.setAssetID(K.ADD_NEW_ASSET);
                assetModelSatuan.setAssetName(K.ADD_NEW_ASSET);
                assetForList.getAssetModelSatuanList().add(assetModelSatuan);
                isInitList = true;
            }
            listCustomAdapter = new ListCustomAdapter(this, assetForList, 9);
        } else if (tipe == 2) {
            listCustomAdapter = new ListCustomAdapter(this, paymentForList, 4);
        }

        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //asset
                if (tipe == 1) {
                    if(assetForList.getAssetModelSatuanList().get(position).getAssetName().
                            equalsIgnoreCase(K.ADD_NEW_ASSET)){

                        isAssetName = true;
                        isEditText = true;
                        mBinding.lyDoneEditText.tvEditTextLabel.setText("Please Fill AssetName");
                        mBinding.lyDoneEditText.etKarakter.setText("");
                        mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                        mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                    }
                    else {
                        assetName = assetForList.getAssetModelSatuanList().get(position).getAssetName();
                        mBinding.lyDetailAsset.tvAssetName.setText(assetName);
                        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
                    }
                }
                //payment
                else if (tipe == 2) {
                    mTipeBayar = paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethodeID();
                    if(isSell){

                        isEditText = true;
                        isPrice = true;
                        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Price SELL With Number Only");
                        if (assetPrice != 0) {
                            mBinding.lyDoneEditText.etNumber.setText(assetPrice + "");
                        } else {
                            mBinding.lyDoneEditText.etNumber.setText("");

                        }
                        mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                        mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
                    }
                    else {
                        mBinding.lyDetailAsset.tvAssetMethode.setText(paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethode());
                        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(View.VISIBLE);
                    }
                }

                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                isDialogCustome = false;

            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(AssetActivity.this);
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
        mBinding.lyDetailAsset.lyDialogAddAsset.setVisibility(GONE);
    }

    private void showDate() {

        myCalendar = Calendar.getInstance();
        new DatePickerDialog(AssetActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_STRING);

                assetDate = sdf.format(myCalendar.getTime());
                mBinding.lyDetailAsset.tvAssetDate.setText(assetDate);

            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    //end
}
