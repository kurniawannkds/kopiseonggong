package com.androiddevnkds.kopiseong.module.wallet;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentWalletBinding;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.adapter.BalanceAdapter;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.google.gson.Gson;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends BaseFragment implements WalletContract.walletView {

    private FragmentWalletBinding mBinding;
    private Context mContext;
    private String colorExpense ="#f5211d",colorExpenseAcc ="#f7382a",colorIncome="#37DC74",colorIncomeAcc="#11fa8a",colorTax="#FFA340";
    private TotalBalanceModel totalBalanceModelGlobal;
    private long cashTotalTemp =0, tempAcctTotal = 0;
    private BalanceAdapter balanceAdapter;
    private MataUangHelper mataUangHelper;
    private WalletPresenter walletPresenter;
    private String dateID = "";
    private long totalIncomeAll = 0, totalExpenseAll = 0, totalHPP = 0, totalCash =0, totalAccount = 0;
    private boolean isAddBalance = false, isEditText = false, isCash = false;
    private String dateString = "";

    public WalletFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        walletPresenter = new WalletPresenter(this);
        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        DateAndTime dateAndTime = new DateAndTime();
        dateID = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT).substring(0,6);
        dateString = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
        mataUangHelper = new MataUangHelper();
        HeaderHelper.initialize(mBinding.getRoot());
        walletPresenter.getUserName();
        walletPresenter.getBalance(dateID);
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


        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isAddBalance){
                    if(isEditText){
                        mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
                        isEditText = false;
                    }
                    else {
                        mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.GONE);
                        mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                    }
                }
                else {
                    mBinding.lyDetailBalance.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
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

        mBinding.lyDetailBalance.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                mBinding.lyDetailBalance.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);
            }
        });

        mBinding.linearCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tempAcctTotal = totalAccount;
                cashTotalTemp = totalCash;
                MataUangHelper mataUangHelper = new MataUangHelper();
                isAddBalance = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddBalance.tvBalanceId.setText(dateID);
                mBinding.lyDialogAddBalance.tvTitle.setText("Edit Balance");
                mBinding.lyDialogAddBalance.tvCashBalance.setText(mataUangHelper.formatRupiah(totalCash));
                mBinding.lyDialogAddBalance.tvAccountBalance.setText(mataUangHelper.formatRupiah(totalAccount));
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
            }
        });

        //add balance
        mBinding.lyDialogAddBalance.layoutRelatifCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditText = true;
                isCash = true;

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Cash balance with number only");
                if (totalCash != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(totalCash + "");
                } else {
                    mBinding.lyDoneEditText.etNumber.setText("");
                }

                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(GONE);
            }
        });

        mBinding.lyDialogAddBalance.layoutRelatifAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditText = true;
                isCash = false;

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Account balance with number only");
                if (totalAccount != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(totalAccount + "");
                } else {
                    mBinding.lyDoneEditText.etNumber.setText("");
                }

                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(GONE);
            }
        });

        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempNumber = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",", "");

                if (isCash) {

                    MataUangHelper mataUangHelper = new MataUangHelper();
                    try {
                        totalCash = Long.parseLong(tempNumber);
                        mBinding.lyDialogAddBalance.tvCashBalance.setText(mataUangHelper.formatRupiah(totalCash));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MataUangHelper mataUangHelper = new MataUangHelper();
                    try {
                        totalAccount = Long.parseLong(tempNumber);
                        mBinding.lyDialogAddBalance.tvAccountBalance.setText(mataUangHelper.formatRupiah(totalAccount));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
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

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
                isEditText = false;
                isCash = false;
            }
        });

        mBinding.lyDialogAddBalance.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(GONE);
                if(totalCash == cashTotalTemp &&
                        totalAccount == tempAcctTotal){

                }
                else {
                    walletPresenter.insertBalance(dateID, dateString, totalCash, totalAccount);
                }
            }
        });

    }

    @Override
    public void showUserName(String userName, String status) {

        mBinding.lyHeaderData.tvName.setText(userName);
        mBinding.lyHeaderData.tvStatus.setText(status);
        HeaderHelper.setLinearContentVisible(true);
        HeaderHelper.setRelativeContentVisible(false);
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
    public void showDiagram(long totalIncome, long totalExpense,long hpp) {

        totalIncomeAll = totalIncome;
        totalExpenseAll = totalExpense;
        totalHPP = hpp;
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90);
        config.addData(new SimplePieInfo(totalIncomeAll, Color.parseColor(colorIncome),"Income"));
        config.addData(new SimplePieInfo(totalHPP, Color.parseColor(colorTax),"Hpp"));
        config.addData(new SimplePieInfo(totalExpenseAll, Color.parseColor(colorExpense),"Expense"));
        config.drawText(true).focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV).strokeMode(false).textSize(13);
        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {

                       if(isFloatUp){
                           showDetailPie(pieInfo.getDesc());
                       }
            }
        });
        config.duration(1000);
        config.floatDownDuration(1000);

        mBinding.mainPieChart.applyConfig(config);
        mBinding.mainPieChart.start();
    }

    @Override
    public void onFailed(String message) {

        showError(message);
    }

    @Override
    public void showDetailBalance(TotalBalanceModel.TotalBalanceSatuan totalBalanceSatuan, int pos) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String date = totalBalanceSatuan.getTotalBalanceID()+"";
        date = date.substring(4,6)+"-"+date.substring(0,4);
        mBinding.lyDetailBalance.tvDate.setText(date);
        mBinding.lyDetailBalance.tvCashBalance.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalCashBalance()));
        mBinding.lyDetailBalance.tvAccountBalance.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalAccBalance()));
        mBinding.lyDetailBalance.tvExpenseCash.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalBalancePengeluaran()));
        mBinding.lyDetailBalance.tvExpenseAcc.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalBalancePengeluaranRek()));
        mBinding.lyDetailBalance.tvIncomeCash.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalBalancePemasukan()));
        mBinding.lyDetailBalance.tvIncomeAcc.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalBalancePemasukanRek()));
        mBinding.lyDetailBalance.tvHpp.setText(mataUangHelper.formatRupiah(totalBalanceSatuan.getTotalBalanceHpp()));

        mBinding.lyDetailBalance.lyDialogLayoutDetailTransaction.setVisibility(View.VISIBLE);
        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);

    }

    @Override
    public void showBalance(long cash, long acc,long totalIncome, long totalExpense, long totalIncomeRek,long totalExpenseRek, long totalHpp, long laba, TotalBalanceModel totalBalanceModel) {

        totalBalanceModelGlobal = totalBalanceModel;

        totalCash = cash;
        totalAccount = acc;
        long totalIncomeAll = totalIncome +totalIncomeRek;
        long totalExpenseAll = totalExpense + totalExpenseRek;
        long totalAllBalance = cash+acc + totalIncomeAll - totalExpenseAll;
        mBinding.tvTotalCash.setText(mataUangHelper.formatRupiah(cash));
        mBinding.tvTotalAcc.setText(mataUangHelper.formatRupiah(acc));
        mBinding.tvTotalIncome.setText(mataUangHelper.formatRupiah(totalIncomeAll));
        mBinding.tvTotalExpense.setText(mataUangHelper.formatRupiah(totalExpenseAll));
        mBinding.tvTotalHpp.setText(mataUangHelper.formatRupiah(totalHpp));
        mBinding.tvLaba.setText(mataUangHelper.formatRupiah(laba));
        mBinding.tvTotalBalanceAll.setText(mataUangHelper.formatRupiah(totalAllBalance));

        balanceAdapter = new BalanceAdapter(mContext, totalBalanceModelGlobal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvTransactionWallet.setLayoutManager(layoutManager);
        mBinding.rvTransactionWallet.setAdapter(balanceAdapter);

        balanceAdapter.setOnItemClickListener(new BalanceAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                walletPresenter.getDetailBalance(totalBalanceModelGlobal,position);
            }
        });
    }

    @Override
    public void showSuccessInit(String message) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
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

    private void showError(String message) {

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

    private void showDetailPie(String desc){

        MataUangHelper mataUangHelper = new MataUangHelper();

        if(desc.equalsIgnoreCase("Income")){
            final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
            pDialog.setTitleText(desc + " this month");
            pDialog.setContentText(mataUangHelper.formatRupiah(totalIncomeAll));
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
        else if(desc.equalsIgnoreCase("Expense")){
            final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
            pDialog.setTitleText(desc + " this month");
            pDialog.setContentText(mataUangHelper.formatRupiah(totalExpenseAll));
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
        else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
            pDialog.setTitleText(desc+ " this month");
            pDialog.setContentText(mataUangHelper.formatRupiah(totalHPP));
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
    //end
}
