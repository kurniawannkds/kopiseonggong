package com.androiddevnkds.kopiseong.module.stock.stockWareHouse;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.StockAdapter;
import com.androiddevnkds.kopiseong.databinding.FragmentStockWareHouseBinding;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockWareHouseFragment extends BaseFragment implements StockWHContract.stockView {

    private FragmentStockWareHouseBinding mBinding;
    private Context mContext;
    private StockWHPresenter stockWHPresenter;
    private StockAdapter stockAdapter;
    private StockModel stockModelGlobal;
    private String stockID = "", stockName = "", stockDate = "";
    private long stockPrice = 0, stockJumlah = 0;
    private double stockPricePerGram = 0;

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
        stockWHPresenter.getAllStock();
    }

    @Override
    public void initEvent() {

        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
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

                stockWHPresenter.setOnClickStock(stockModel, position);
                Log.e("TES", "TES");
            }
        });

    }

    @Override
    public void onFailed(final int tipe, String message) {

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
        hideProgressBar();
        if (tipe != 1) {
            mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showStockDetail(StockModel.StockSatuanModel stockSatuanModel, int position) {

        stockID = stockSatuanModel.getStockID();
        stockName = stockSatuanModel.getStockName();
        stockDate = stockSatuanModel.getStockDate();
        stockPrice = stockSatuanModel.getStockPrice();
        stockJumlah = stockSatuanModel.getStockGram();
        stockPricePerGram = stockSatuanModel.getStockPricePerGram();

        MataUangHelper mataUangHelper = new MataUangHelper();

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


            }
        });

        mBinding.lyDialogDetailStock.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showSuccessDelete(final int position, String message) {

        //delete
        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
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
                pDialog.dismiss();
            }
        });
    }
}
