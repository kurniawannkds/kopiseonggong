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
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentWalletBinding;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends BaseFragment implements WalletContract.walletView {

    private FragmentWalletBinding mBinding;
    private Context mContext;
    private String colorExpense ="#f5211d",colorIncome="#37DC74",colorTax="#FFA340";
    private TotalBalanceModel totalBalanceModelGlobal;
    private BalanceAdapter balanceAdapter;
    private MataUangHelper mataUangHelper;
    private WalletPresenter walletPresenter;

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

        mataUangHelper = new MataUangHelper();
        HeaderHelper.initialize(mBinding.getRoot());
        walletPresenter.getUserName();
        walletPresenter.getBalance();
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

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
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
    public void showDiagram(long totalIncome, long totalExpense, long totalTax) {

        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90);
        config.addData(new SimplePieInfo(totalIncome, Color.parseColor(colorIncome),mataUangHelper.formatRupiah(totalIncome)));
        config.addData(new SimplePieInfo(totalTax, Color.parseColor(colorTax),mataUangHelper.formatRupiah(totalTax)));
        config.addData(new SimplePieInfo(totalExpense, Color.parseColor(colorExpense),mataUangHelper.formatRupiah(totalExpense)));
        config.drawText(true).focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV).strokeMode(false).textSize(13);
        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {



            }
        });
        config.duration(1000);
        config.floatDownDuration(1000);

        mBinding.mainPieChart.applyConfig(config);
        mBinding.mainPieChart.start();
    }

    @Override
    public void onFailed(String message) {

        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBalance(long totalIncome, long totalExpense, long totalTax, long avalaibleBalance, TotalBalanceModel totalBalanceModel) {

        totalBalanceModelGlobal = totalBalanceModel;

        mBinding.tvTotalIncome.setText(mataUangHelper.formatRupiah(totalIncome));
        mBinding.tvTotalExpense.setText(mataUangHelper.formatRupiah(totalExpense));
        mBinding.tvTotalTax.setText(mataUangHelper.formatRupiah(totalTax));
        mBinding.tvAvalaibleBalance.setText(mataUangHelper.formatRupiah(avalaibleBalance));

        balanceAdapter = new BalanceAdapter(mContext, totalBalanceModelGlobal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvTransactionWallet.setLayoutManager(layoutManager);
        mBinding.rvTransactionWallet.setAdapter(balanceAdapter);
    }

    //end
}
