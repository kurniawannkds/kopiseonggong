package com.androiddevnkds.kopiseong.module.asset;

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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.ProductAdapter;
import com.androiddevnkds.kopiseong.databinding.ActivityAssetBinding;
import com.androiddevnkds.kopiseong.databinding.ActivityProductBinding;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.product.ProductContract;
import com.androiddevnkds.kopiseong.module.product.ProductPresenter;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

public class AssetActivity extends BaseActivity implements AssetContract.assetView {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityAssetBinding mBinding;
    private AssetPresenter assetPresenter;


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
        HeaderHelper.setLabelContentText("Product");
        HeaderHelper.setRelativeContentVisible(true);
        HeaderHelper.setLabelContentVisible(true);
        HeaderHelper.setLinearReportVisible(true);
        HeaderHelper.setLinearContentVisible(false);

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


            }
        });

        //-----------------------------------------edit-------------------------------------------


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
    //end
}
