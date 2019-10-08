package com.androiddevnkds.kopiseong.module.home;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentWelcomeBinding;
import com.androiddevnkds.kopiseong.module.asset.AssetActivity;
import com.androiddevnkds.kopiseong.module.main.MainActivity;
import com.androiddevnkds.kopiseong.module.product.ProductActivity;
import com.androiddevnkds.kopiseong.module.register.RegisterActivity;
import com.androiddevnkds.kopiseong.module.resep.ResepActivity;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.wallet.WalletActivity;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends BaseFragment implements HomeContract.homeView {

    private FragmentWelcomeBinding mBinding;
    private Context mContext;
    private HomePresenter homePresenter;
    private String userRole = "";
    private String dateNow = "", dateBefore = "", dateString = "";
    private String titleFix = "You Not Allowed to Use This Feature";
    private String messageFix = "Please choose transaction or stock store feature on bottom navigation";
    private boolean isDialogText = false, isCash = false;
    private long cash = 0, account = 0;

    public WelcomeFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false);
        homePresenter = new HomePresenter(this);

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        homePresenter.getUserName();
        mBinding.lyBottomNav.navigation.setSelectedItemId(R.id.home_menu);
        HeaderHelper.setLinearLogOut(true);
        DateAndTime dateAndTime = new DateAndTime();
        dateNow = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_SORT).substring(0, 6);
        dateString = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);

        if (DataManager.can().getTanggal() != null) {

            dateBefore = DataManager.can().getTanggal();
            if (!dateNow.equalsIgnoreCase(dateBefore)) {

                homePresenter.initBalance(dateNow, dateBefore);
            }
        } else {

            mBinding.lyDialogAddBalance.tvBalanceId.setText(dateNow);
            mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
            mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
//            homePresenter.insertBalance();
            //insert balanceeeeeeeeee fragment
        }

        if (DataManager.can().getUserInfoFromStorage().getUserRole() != null) {
            userRole = DataManager.can().getUserInfoFromStorage().getUserRole();
        }
    }

    @Override
    public void initEvent() {


        //setting profile
        mBinding.lyHeaderData.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //logout
        mBinding.lyHeaderData.linearHeaderLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataManager.can().setUserStatusToStorage(false);
                if (DataManager.can().getUserInfoFromStorage() != null) {
                    DataManager.can().removeUserInfoFromStorage();
                }
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });

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
                        intentTrans.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            Objects.requireNonNull(getActivity()).finish();
//                        }

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(mContext, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentStock);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            Objects.requireNonNull(getActivity()).finish();
//                        }

                        return true;
                }

                return false;
            }
        });


        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isDialogText) {
                    mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
                    mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
                    isDialogText = false;
                }
//                } else {
//                    mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.GONE);
//                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
//                }
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

        //wallet
        mBinding.linearWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {
                    Intent intentStock = new Intent(mContext, WalletActivity.class);
                    intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                    startActivity(intentStock);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                } else {

                    showError();
                }
            }
        });

        //resep
        mBinding.linearRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {
                    Intent intentStock = new Intent(mContext, ResepActivity.class);
                    intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                    startActivity(intentStock);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                } else {
                    showError();
                }
            }
        });

        //product
        mBinding.linearProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                    Intent intent = new Intent(mContext, ProductActivity.class);
                    startActivity(intent);
                } else {
                    showError();
                }
            }
        });

        //regis
        mBinding.linearRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                    Intent intent = new Intent(mContext, RegisterActivity.class);
                    startActivity(intent);

                } else {

                    showError();
                }
            }
        });

        //jurnal
        mBinding.linearJurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                } else {

                    showError();
                }
            }
        });

        //setting
        mBinding.linearAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                    Intent intent = new Intent(mContext, AssetActivity.class);
                    startActivity(intent);

                } else {

                    showError();
                }
            }
        });

        //add balance
        mBinding.lyDialogAddBalance.layoutRelatifCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isDialogText = true;
                isCash = true;

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Cash balance with number only");
                if (cash != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(cash + "");
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

                isDialogText = true;
                isCash = false;

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Account balance with number only");
                if (account != 0) {
                    mBinding.lyDoneEditText.etNumber.setText(account + "");
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
                        cash = Long.parseLong(tempNumber);
                        mBinding.lyDialogAddBalance.tvCashBalance.setText(mataUangHelper.formatRupiah(cash));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MataUangHelper mataUangHelper = new MataUangHelper();
                    try {
                        account = Long.parseLong(tempNumber);
                        mBinding.lyDialogAddBalance.tvAccountBalance.setText(mataUangHelper.formatRupiah(account));
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
                isDialogText = false;
                isCash = false;
            }
        });

        mBinding.lyDialogAddBalance.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(GONE);
                homePresenter.insertBalance(dateNow,dateString,cash,account);
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
    public void successInitBalance(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                DataManager.can().setTanggal(dateNow);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void onfailed(String message) {

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
                mBinding.lyDialogAddBalance.tvBalanceId.setText(dateNow);
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddBalance.lyDialogAddBalance.setVisibility(View.VISIBLE);
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

    private void showError() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(titleFix);
        pDialog.setContentText(messageFix);
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

    //end
}
