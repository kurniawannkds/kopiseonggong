package com.androiddevnkds.kopiseong.module.stock.stockStore;


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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.StockAdapter;
import com.androiddevnkds.kopiseong.databinding.FragmentStockStoreBinding;
import com.androiddevnkds.kopiseong.databinding.FragmentStockWareHouseBinding;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.stock.stockWareHouse.StockWHContract;
import com.androiddevnkds.kopiseong.module.stock.stockWareHouse.StockWHPresenter;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionFragment;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockStoreFragment extends BaseFragment implements StockStoreContract.stockView {

    private FragmentStockStoreBinding mBinding;
    private Context mContext;
    private StockStorePresenter stockStorePresenter;
    private StockAdapter stockAdapter;
    private StockModel stockModelGlobal;
    private String stockID = "", stockName = "", stockDate = "", stockDateSort = "";
    private long stockPrice = 0, stockJumlah = 0;
    private long stockPricePerGram = 0;
    private int positionDetail = 0;

    private Calendar myCalendar;
    private SimpleDateFormat sdf;

    private boolean isEditDetailStock = false;

    public StockStoreFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_store, container, false);
        stockStorePresenter = new StockStorePresenter(this);

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelText("Stock Store");
        HeaderHelper.setLabelVisible(true);
        HeaderHelper.setLinearStockSTVisible(true);
        stockStorePresenter.getAllStock();
        mBinding.lyBottomNav.navigation.setSelectedItemId(R.id.stock_menu);
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

        mBinding.lyHeaderData.linearStockWh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, StockActivity.class);
                intent.putExtra(K.KEY_STOCK,K.VALUE_KEY_STOCK_WAREHOUSE);
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

                else {
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                    mBinding.lyDialogDetailStock.lyDialogLayoutDetailStock.setVisibility(View.GONE);
                }
            }
        });


        //----------------------------------------edit stock------------------------------------
        mBinding.lyDialogEditStock.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stockDate = mBinding.lyDialogEditStock.tvStockDate.getText().toString().trim();
                String tempjumlah = mBinding.lyDialogEditStock.etStockJumlah.getText().toString().trim();
                String tempPrice = mBinding.lyDialogEditStock.etStockPrice.getText().toString().trim().replace(",","");

                long stockJumlah = 0, stockPrice = 0;
                try {
                    stockPrice = Integer.parseInt(tempPrice);
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

                stockStorePresenter.editStock(stockSatuanModel,positionDetail);
            }
        });

        mBinding.lyDialogEditStock.ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //------------------------------------------edit  stock finish-------------------------------------------------------------

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
                stockStorePresenter.setOnClickStock(stockModel, position);
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

                        stockStorePresenter.deleteStock(stockID,stockDateSort, positionDetail);
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
                    temp = Integer.parseInt(split[1]);
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

            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
