package com.androiddevnkds.kopiseong.module.transaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import com.androiddevnkds.kopiseong.R;

import com.androiddevnkds.kopiseong.databinding.FragmentTransactionBinding;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.ListUserModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;

import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.module.addTransaction.AddTransactionActivity;
import com.androiddevnkds.kopiseong.module.addTransaction.AddTransactionFragment;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.login.LoginFragment;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.utils.DateAndTime;

import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

import com.androiddevnkds.kopiseong.adapter.DetailTransactionAdapter;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.TransactionAdapter;
import com.androiddevnkds.kopiseong.BaseFragment;
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
public class TransactionFragment extends BaseFragment implements TransactionContract.transactionView {

    private FragmentTransactionBinding mBinding;

    private Context mContext;
    private TransactionModel transactionModelGlobal;
    private int page = 0, rows = 0;
    private TransactionAdapter transactionAdapter;
    private DetailTransactionAdapter detailTransactionAdapter;
    private boolean isPanelShown = false;

    private boolean isFilter = false, isDialogCustome = false, isDetailTrans = false, isMoreDetailTrans = false;

    private CategoryModel categoryForList;
    private PaymentMethodeModel paymentForList;
    private ListUserModel userForList;

    private ListCustomAdapter listCustomAdapter;
    private MataUangHelper mataUangHelper;
    private TransactionPresenter transactionPresenter;

    //date
    private Calendar myCalendar;
    private SimpleDateFormat sdf;


    //filter
    private String filterCategory = "NONE", filterDate = "NONE", filterUser = "NONE", filterMethod = "NONE";

    @Override
    public void onResume() {
        super.onResume();
        //callAllTransaction();
    }


    public TransactionFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false);
        transactionPresenter = new TransactionPresenter(this);
        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelContentText("Transaction");
        HeaderHelper.setRelativeContentVisible(true);
        HeaderHelper.setLabelContentVisible(true);
        HeaderHelper.setLinearReportVisible(true);
        HeaderHelper.setLinearContentVisible(false);

        mataUangHelper = new MataUangHelper();
        mBinding.lyBottomNav.navigation.setSelectedItemId(R.id.transaction_menu);

        DateAndTime dateAndTime = new DateAndTime();
        filterDate = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING);
        mBinding.lyBottomUpSliderFilter.tvFilterDate.setText(filterDate);
        mBinding.lyHeaderData.tvDate.setText(filterDate);
        transactionPresenter.getAllTransaction(page, filterCategory, filterDate, filterUser, filterMethod);
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
                        intentTrans.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(mContext, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentStock);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                }

                return false;
            }
        });

        mBinding.lyHeaderData.tvIconFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpDown(mBinding.lyBottomUpSliderFilter.bottomSheet);
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
            }
        });

        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPanelShown) {
                    slideUpDown(mBinding.lyBottomUpSliderFilter.bottomSheet);
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                } else if (isDialogCustome) {
                    isDialogCustome = false;
                    mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                } else if (isMoreDetailTrans) {
                    isMoreDetailTrans = false;
                    mBinding.lyMoreDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);
                    mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.VISIBLE);
                } else {
                    isDetailTrans = false;
                    mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                }
            }
        });


        //--------------------------- FILTER --------------------------------------------------------

        //category choosen, disappear border
        mBinding.lyBottomUpSliderFilter.relatifCategoryChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBottomUpSliderFilter.tvFilterCat.setText("");
                filterCategory = "NONE";
                mBinding.lyBottomUpSliderFilter.relatifCategoryChoosen.setVisibility(GONE);
                mBinding.lyBottomUpSliderFilter.relatifCategoryNotChoosen.setVisibility(View.VISIBLE);
            }
        });

        //categorynot choosen, show list cat
        mBinding.lyBottomUpSliderFilter.relatifCategoryNotChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transactionPresenter.showListCategory(categoryForList, filterCategory);
            }
        });

        //payment choosen, disappear border
        mBinding.lyBottomUpSliderFilter.relatifMethodChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBottomUpSliderFilter.tvFilterMethod.setText("");
                filterMethod = "NONE";
                mBinding.lyBottomUpSliderFilter.relatifMethodChoosen.setVisibility(GONE);
                mBinding.lyBottomUpSliderFilter.relatifMethodNotChoosen.setVisibility(View.VISIBLE);
            }
        });

        //payment not choosen, show payment list
        mBinding.lyBottomUpSliderFilter.relatifMethodNotChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transactionPresenter.showListPayment(paymentForList, filterMethod);
            }
        });

        //date filter not choosen, show date
        mBinding.lyBottomUpSliderFilter.relatifDateNotChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDate();
            }
        });

        //date filter choosen, disappear border
        mBinding.lyBottomUpSliderFilter.relatifDateChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBottomUpSliderFilter.tvFilterDate.setText("");
                filterDate = "NONE";
                mBinding.lyBottomUpSliderFilter.relatifDateChoosen.setVisibility(GONE);
                mBinding.lyBottomUpSliderFilter.relatifDateNotChoosen.setVisibility(View.VISIBLE);
                DateAndTime dateAndTime = new DateAndTime();
                mBinding.lyHeaderData.tvDate.setText(dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING));
            }
        });

        //user not choosen, show userList
        mBinding.lyBottomUpSliderFilter.relatifUserNotChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transactionPresenter.showListUser(userForList, filterUser);
            }
        });

        //user choosen, disappear border
        mBinding.lyBottomUpSliderFilter.relatifUserChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBottomUpSliderFilter.tvFilterUser.setText("");
                filterUser = "NONE";
                mBinding.lyBottomUpSliderFilter.relatifUserChoosen.setVisibility(GONE);
                mBinding.lyBottomUpSliderFilter.relatifUserNotChoosen.setVisibility(View.VISIBLE);
            }
        });

        //submit filter
        mBinding.lyBottomUpSliderFilter.btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                page = 0;
                isPanelShown = true;
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                slideUpDown(mBinding.lyBottomUpSliderFilter.bottomSheet);
                isFilter = true;
                if(!filterDate.equalsIgnoreCase("NONE")){
                    mBinding.lyHeaderData.tvDate.setText(filterDate);
                }
                transactionPresenter.getAllTransaction(page, filterCategory, filterDate, filterUser, filterMethod);
            }
        });

        //---------------------------------------- filter finish------------------------------------------------------------


        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.btnPrevius.setVisibility(View.VISIBLE);
                //hit api
                page++;
                rows = rows - (page * 20);
                if (transactionModelGlobal.getTransactionModelLists().size() > (page * 20)) {

                    TransactionModel transactionModel = new TransactionModel();
                    int sizeGlobal = transactionModelGlobal.getTransactionModelLists().size();
                    List<TransactionSatuanModel> transactionSatuanModelList = new ArrayList<>();
                    if (sizeGlobal - (page * 20) > 20) {
                        transactionSatuanModelList = transactionModelGlobal.getTransactionModelLists().subList(page * 20, (page * 20) + 20);
                    } else {
                        transactionSatuanModelList = transactionModelGlobal.getTransactionModelLists().subList(page * 20, transactionModelGlobal.getTransactionModelLists().size());
                    }

                    transactionModel.setTransactionModelLists(transactionSatuanModelList);
                    setAdapter(transactionModel, "");
                } else {
                    Log.e("trans", "hit baru");
                    transactionPresenter.getAllTransaction(page, filterCategory, filterDate, filterUser, filterMethod);
                }

            }
        });
        mBinding.btnPrevius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page--;

                rows = rows + 20;
                //hit api
                TransactionModel transactionModel = new TransactionModel();
                List<TransactionSatuanModel> transactionSatuanModelList = transactionModelGlobal.getTransactionModelLists().subList(page * 20, (page * 20) + 20);
                transactionModel.setTransactionModelLists(transactionSatuanModelList);
                setAdapter(transactionModel, "");

            }
        });

        mBinding.lyDetailTransaction.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);

            }
        });

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AddTransactionActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });

    }

    private void slideUpDown(final View view) {
        if (!isPanelShown) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_up);

            mBinding.lyBottomUpSliderFilter.bottomSheet.startAnimation(bottomUp);
            mBinding.lyBottomUpSliderFilter.bottomSheet.setVisibility(View.VISIBLE);
            isPanelShown = true;
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(mContext,
                    R.anim.bottom_down);

            mBinding.lyBottomUpSliderFilter.bottomSheet.startAnimation(bottomDown);
            mBinding.lyBottomUpSliderFilter.bottomSheet.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }
    }


    //MVP
    @Override
    public void showAllTransaction(TransactionModel transactionModel) {

        if (isFilter || transactionModelGlobal == null || transactionModelGlobal.getTransactionModelLists() == null || transactionModelGlobal.getTransactionModelLists().size() == 0) {
            transactionModelGlobal = transactionModel;
            rows = transactionModel.getTotalRow();

            isFilter = false;
            setAdapter(transactionModel, "init");
        } else {

            Log.e("TRAN PARM", transactionModel.getTransactionModelLists().size() + "");
            int sizeTemp = transactionModelGlobal.getTransactionModelLists().size();
            Log.e("TRAN SIZE", sizeTemp + "");

            transactionModelGlobal.getTransactionModelLists().addAll(sizeTemp, transactionModel.getTransactionModelLists());
            setAdapter(transactionModel, "");
        }
        Log.e("TRAN GLOBAL", transactionModelGlobal.getTransactionModelLists().size() + "");

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
    public void onFailedGetAllAPI(String message) {
        if (isFilter) {
            transactionModelGlobal = new TransactionModel();
            transactionAdapter.resetListTransaction(transactionModelGlobal);
            transactionAdapter.notifyDataSetChanged();
        }
        showError(message);
        hideProgressBar();
    }

    @Override
    public void showOnClickTransaction(TransactionSatuanModel transactionSatuanModel, final DetailTransactionModel detailTransactionModel) {

        mBinding.lyDetailTransaction.tvName.setText("Detail Transaction");
        mBinding.lyDetailTransaction.tvCategory.setText(transactionSatuanModel.getTransactionCategory());
        mBinding.lyDetailTransaction.tvTransDate.setText(transactionSatuanModel.getTransactionDate());
        mBinding.lyDetailTransaction.tvTransTime.setText(transactionSatuanModel.getTransactionTime());
        mBinding.lyDetailTransaction.tvUserEmail.setText(transactionSatuanModel.getUserEmail());
        mBinding.lyDetailTransaction.tvPaymentType.setText(transactionSatuanModel.getTipePembayaran());
        mBinding.lyDetailTransaction.tvTotalBalance.setText(mataUangHelper.formatRupiah(transactionSatuanModel.getTransactionBalance()));


        detailTransactionAdapter = new DetailTransactionAdapter(mContext, detailTransactionModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.lyDetailTransaction.rvTransactionDetail.setLayoutManager(layoutManager);
        mBinding.lyDetailTransaction.rvTransactionDetail.setAdapter(detailTransactionAdapter);

        detailTransactionAdapter.setOnItemClickListener(new DetailTransactionAdapter.ClickListener() {
            @Override
            public void onItemClick(int pos, View v) {

                isMoreDetailTrans = true;
                transactionPresenter.setOnClickDetailTransaction(detailTransactionModel, pos);
            }
        });

        isDetailTrans = true;
        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.VISIBLE);

    }

    @Override
    public void showMoreDetailTransaction(DetailTransactionModel.DetailTransaction detailTransaction) {

        mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(GONE);
        mBinding.lyMoreDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.VISIBLE);

        mBinding.lyMoreDetailTransaction.tvCategory.setText(detailTransaction.getProductGeneralCat());
        mBinding.lyMoreDetailTransaction.tvProductName.setText(detailTransaction.getProductName());
        mBinding.lyMoreDetailTransaction.tvResep.setText(detailTransaction.getProductResep());
        mBinding.lyMoreDetailTransaction.tvTotalProduct.setText(detailTransaction.getDetailJumlah()+" unit");
    }

    @Override
    public void showAllCategory(CategoryModel categoryModel, int pos) {

        isDialogCustome = true;

        categoryForList = categoryModel;
        setCustomeList(1);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (categoryModel.getCategorySatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAllPayment(PaymentMethodeModel paymentMethodeModel, int pos) {

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
    public void showAllUser(ListUserModel listUserModel, int pos) {

        isDialogCustome = true;

        userForList = listUserModel;
        setCustomeList(3);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (listUserModel.getUserInfoModelList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    private void showDate() {

        myCalendar = Calendar.getInstance();
        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                sdf = new SimpleDateFormat(K.FORMAT_TANGGAL_STRING);

                filterDate = sdf.format(myCalendar.getTime());
                mBinding.lyHeaderData.tvDate.setText(filterDate);

                mBinding.lyBottomUpSliderFilter.tvFilterDate.setText(filterDate);
                mBinding.lyBottomUpSliderFilter.relatifDateChoosen.setVisibility(View.VISIBLE);
                mBinding.lyBottomUpSliderFilter.relatifDateNotChoosen.setVisibility(GONE);

            }
        },
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setAdapter(TransactionModel transactionModel, String action) {

        if (action.equalsIgnoreCase("INIT")) {
            transactionAdapter = new TransactionAdapter(mContext, transactionModel);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            mBinding.rvTransaction.setLayoutManager(layoutManager);
            mBinding.rvTransaction.setAdapter(transactionAdapter);

            transactionAdapter.setOnItemClickListener(new TransactionAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    transactionPresenter.setOnClickTransaction(transactionModelGlobal, position, page);
                }
            });
        } else {

            Log.e("TRANS", transactionModel.getTransactionModelLists().size() + "");
            transactionAdapter.resetListTransaction(transactionModel);
            transactionAdapter.notifyDataSetChanged();
        }

        if (rows > 20) {
            mBinding.btnNext.setVisibility(View.VISIBLE);
        } else {
            mBinding.btnNext.setVisibility(View.GONE);
        }

        if (page == 0) {
            mBinding.btnPrevius.setVisibility(View.GONE);
        } else {
            mBinding.btnPrevius.setVisibility(View.VISIBLE);

        }
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

    private void setCustomeList(final int tipe) {

        if (tipe == 1) {
            listCustomAdapter = new ListCustomAdapter(mContext, categoryForList, 1);
        } else if (tipe == 2) {
            listCustomAdapter = new ListCustomAdapter(mContext, paymentForList, 4);
        } else {
            listCustomAdapter = new ListCustomAdapter(mContext, userForList, 2);
        }
        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (tipe == 1) {
                    filterCategory = categoryForList.getCategorySatuanList().get(position).getCategoryID();
                    mBinding.lyBottomUpSliderFilter.tvFilterCat.setText
                            (categoryForList.getCategorySatuanList().get(position).getCategoryName());
                    mBinding.lyBottomUpSliderFilter.relatifCategoryChoosen.setVisibility(View.VISIBLE);
                    mBinding.lyBottomUpSliderFilter.relatifCategoryNotChoosen.setVisibility(GONE);
                } else if (tipe == 2) {

                    filterMethod = paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethodeID();
                    mBinding.lyBottomUpSliderFilter.tvFilterMethod.setText
                            (paymentForList.getPaymentMethodeSatuanList().get(position).getPaymentMethode());
                    mBinding.lyBottomUpSliderFilter.relatifMethodChoosen.setVisibility(View.VISIBLE);
                    mBinding.lyBottomUpSliderFilter.relatifMethodNotChoosen.setVisibility(GONE);

                } else {
                    filterUser = userForList.getUserInfoModelList().get(position).getUserEmail();
                    mBinding.lyBottomUpSliderFilter.tvFilterUser.setText
                            (userForList.getUserInfoModelList().get(position).getNameUser());
                    mBinding.lyBottomUpSliderFilter.relatifUserChoosen.setVisibility(View.VISIBLE);
                    mBinding.lyBottomUpSliderFilter.relatifUserNotChoosen.setVisibility(GONE);
                }

                isDialogCustome = false;
                slideUpDown(mBinding.lyBottomUpSliderFilter.bottomSheet);
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        slideUpDown(mBinding.lyBottomUpSliderFilter.bottomSheet);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    //end
}
