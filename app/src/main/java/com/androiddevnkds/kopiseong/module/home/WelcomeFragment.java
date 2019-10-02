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
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentWelcomeBinding;
import com.androiddevnkds.kopiseong.module.main.MainActivity;
import com.androiddevnkds.kopiseong.module.resep.ResepActivity;
import com.androiddevnkds.kopiseong.module.resep.ResepFragment;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.wallet.WalletActivity;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
import com.androiddevnkds.kopiseong.module.wallet.WalletFragment;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends BaseFragment implements HomeContract.homeView {

    private FragmentWelcomeBinding mBinding;
    private Context mContext;
    private HomePresenter homePresenter;
    private String userRole = "";
    private String titleFix = "You Not Allowed to Use This Feature";
    private String messageFix = "Please choose transaction or stock store feature on bottom navigation";

    public WelcomeFragment() {
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

        if(DataManager.can().getUserInfoFromStorage().getUserRole()!=null){
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
                        intentTrans.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            Objects.requireNonNull(getActivity()).finish();
//                        }

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(mContext, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_WAREHOUSE);
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

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
            }
        });

        //wallet
        mBinding.linearWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {
                    Intent intentStock = new Intent(mContext, WalletActivity.class);
                    intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                    startActivity(intentStock);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
                else {

                    showError();
                }
            }
        });

        //resep
        mBinding.linearRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {
                    Intent intentStock = new Intent(mContext, ResepActivity.class);
                    intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                    startActivity(intentStock);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
                else {
                    showError();
                }
            }
        });

        //product
        mBinding.linearProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                }
                else {
                    showError();
                }
            }
        });

        //regis
        mBinding.linearRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                }
                else {

                }
            }
        });

        //jurnal
        mBinding.linearJurnal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                }
                else {

                }
            }
        });

        //setting
        mBinding.linearSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userRole.equalsIgnoreCase(K.KEY_ROLE_MASTER)) {

                }
                else {

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
