package com.androiddevnkds.kopiseong.module.home;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentWelcomeBinding;
import com.androiddevnkds.kopiseong.module.resep.ResepFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
import com.androiddevnkds.kopiseong.module.wallet.WalletFragment;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends BaseFragment implements HomeContract.homeView {

    private FragmentWelcomeBinding mBinding;
    private Context mContext;
    private HomePresenter homePresenter;

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
    }

    @Override
    public void initEvent() {

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Toast.makeText(mContext,"home",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.transaction_menu:
                        FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                                ((AppCompatActivity) mContext).getSupportFragmentManager(),
                                new TransactionFragment(), null, false);
                        return true;
                    case R.id.stock_menu:
                        Toast.makeText(mContext,"stock",Toast.LENGTH_SHORT).show();

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

        mBinding.linearWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                        ((AppCompatActivity) mContext).getSupportFragmentManager(),
                        new WalletFragment(), null, false);
            }
        });

        mBinding.linearPreOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBinding.linearStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBinding.linearTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                        ((AppCompatActivity) mContext).getSupportFragmentManager(),
                        new ResepFragment(), null, false);
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

    //end
}
