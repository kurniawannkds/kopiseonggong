package com.androiddevnkds.kopiseong.module.transaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentTransactionBinding;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.model.TransactionSatuanModel;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;
import com.androiddevnkds.kopiseong.adapter.DetailTransactionAdapter;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.TransactionAdapter;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.module.home.WelcomeFragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends BaseFragment implements TransactionContract.transactionView {

    private FragmentTransactionBinding mBinding;
    private static String FORMAT_TANGGAL_STRING = "dd-MM-yyyy", FORMAT_TANGGAL_SORT = "yyyyMMdd";
    private static String FORMAT_TIME = "hh:mm:ss";
    private Context mContext;
    private TransactionModel transactionModelGlobal;
    private TransactionSatuanModel transactionModelAdd;
    private int page = 0, rows = 0;
    private TransactionAdapter transactionAdapter;
    private DetailTransactionAdapter detailTransactionAdapter, detailTransactionAddAdapter;
    private boolean isPanelShown = false, isDone = false;
    private DetailTransactionModel detailTransactionModelAdd;
    private List<DetailTransactionModel.DetailTransaction> detailTransactionList;

    //untuk tambah
    private String transIDADD = "", transCatAdd = "", transDateAdd = "", transTimeAdd = "", transUserEmailAdd = "", detailProductAdd = "";
    private int transDateSortAdd = 0, transPriceAdd = 0, detailJumlahAdd = 0, detailPriceAdd = 0, detailPriceAddSatuan = 0;

    private int selectedPositionCategory = -1, selectedPositionUser = -1, selectedPositionProduct = -1;
    private boolean isPrice = false, isCustomer = false;

    private CategoryModel categoryModelGlobal;
    private int flagListChoosen = -1, sizeArray = 0;
    private ListCustomAdapter listCustomAdapter;
    private MataUangHelper mataUangHelper;
    private ProductModel productModelGlobal;
    private TransactionPresenter transactionPresenter;

    //date
    private Calendar myCalendar;
    private SimpleDateFormat sdf;

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
        detailTransactionList = new ArrayList<>();

        transactionPresenter.getAllTransaction(page);
    }

    @Override
    public void initEvent() {

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                                ((AppCompatActivity) mContext).getSupportFragmentManager(),
                                new WelcomeFragment(), null, false);
                        return true;
                    case R.id.transaction_menu:
                        Toast.makeText(mContext, "txn", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.stock_menu:
                        Toast.makeText(mContext, "stock", Toast.LENGTH_SHORT).show();

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
                } else {
                    mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);
                    mBinding.lyAddTransaction.lyDialogLayoutAddTransaction.setVisibility(View.GONE);

                }
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
            }
        });

        mBinding.lyBlack2.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.GONE);
            }
        });
        //category
        mBinding.lyBottomUpSliderFilter.relatifCategoryDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifCategoryDisable.setVisibility(View.GONE);
                mBinding.lyBottomUpSliderFilter.relatifCategory.setVisibility(View.VISIBLE);
            }
        });

        //prod
        mBinding.lyBottomUpSliderFilter.relatifProductDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifProductDisable.setVisibility(View.GONE);
                mBinding.lyBottomUpSliderFilter.relatifProduct.setVisibility(View.VISIBLE);
            }
        });

        //prod
        mBinding.lyBottomUpSliderFilter.relatifStatusDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifStatusDisable.setVisibility(View.GONE);
                mBinding.lyBottomUpSliderFilter.relatifStatus.setVisibility(View.VISIBLE);
            }
        });

        //category
        mBinding.lyBottomUpSliderFilter.relatifCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifCategoryDisable.setVisibility(View.VISIBLE);
                mBinding.lyBottomUpSliderFilter.relatifCategory.setVisibility(View.GONE);
            }
        });

        //prod
        mBinding.lyBottomUpSliderFilter.relatifProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifProductDisable.setVisibility(View.VISIBLE);
                mBinding.lyBottomUpSliderFilter.relatifProduct.setVisibility(View.GONE);
            }
        });

        //sta
        mBinding.lyBottomUpSliderFilter.relatifStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyBottomUpSliderFilter.relatifStatusDisable.setVisibility(View.VISIBLE);
                mBinding.lyBottomUpSliderFilter.relatifStatus.setVisibility(View.GONE);
            }
        });

        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.btnPrevius.setVisibility(View.VISIBLE);
                //hit api
                page++;
                transactionPresenter.getAllTransaction(page);

            }
        });
        mBinding.btnPrevius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page--;

                //hit api
                transactionPresenter.getAllTransaction(page);
            }
        });

        mBinding.lyDetailTransaction.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.GONE);

            }
        });


        //--------------------------------ADD DATA---------------------------------------------------------
        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transactionPresenter.initialAddTransactionLayout();

                mBinding.lyAddTransaction.lyDialogLayoutAddTransaction.setVisibility(View.VISIBLE);
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);

            }
        });

        //submit transaction
        mBinding.lyAddTransaction.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!transCatAdd.equalsIgnoreCase("") && transPriceAdd != 0 && detailTransactionList != null && detailTransactionList.size() > 0) {
                    transactionModelAdd = new TransactionSatuanModel();

                    transIDADD = transDateAdd + transTimeAdd;
                    transactionPresenter.addNewTransaction( transIDADD, transDateSortAdd, transDateAdd, transTimeAdd,
                            transUserEmailAdd, transCatAdd, transPriceAdd, detailTransactionList);

                } else {

                    if (transPriceAdd == 0) {
                        Toast.makeText(mContext, "Please fill total price on the left", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Please detail transaction on the right", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        mBinding.lyAddTransaction.btnAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!detailProductAdd.equalsIgnoreCase("") && detailJumlahAdd != 0) {

                    DetailTransactionModel.DetailTransaction detailTransactionSatuan = new DetailTransactionModel().new DetailTransaction();
                    detailTransactionSatuan.setDetailProductID(detailProductAdd);
                    detailTransactionSatuan.setDetailJumlah(detailJumlahAdd);
                    detailTransactionSatuan.setDetailTransactionID(transIDADD);
                    detailTransactionList.add(detailTransactionSatuan);

                    DetailTransactionModel detailTemp = new DetailTransactionModel();
                    detailTemp.setDetailTransactionList(detailTransactionList);
                    detailTransactionAddAdapter.setNewItemList(detailTemp);
                    detailTransactionAddAdapter.notifyDataSetChanged();

                } else {
                    if (detailProductAdd.equalsIgnoreCase("")) {
                        Toast.makeText(mContext, "Please fill product on the right", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Please fill quantity of product on the right", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //price transaction
        mBinding.lyAddTransaction.layoutRelatifPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etKarakter.setVisibility(View.GONE);

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.VISIBLE);

                isPrice = true;
            }
        });

        // customer transaction
        mBinding.lyAddTransaction.layoutRelatifCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDoneEditText.etNumber.setVisibility(View.GONE);
                mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.VISIBLE);

                isCustomer = true;
            }
        });


        //edit text
        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = "";
                if (isPrice) {

                    temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim();

                    try {
                        transPriceAdd = Integer.parseInt(temp);
                        String uang = mataUangHelper.formatRupiah(transPriceAdd);
                        mBinding.lyAddTransaction.tvPrice.setText(uang);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (isCustomer) {

                    transUserEmailAdd = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    mBinding.lyAddTransaction.tvCutsomer.setText(transUserEmailAdd);

                } else {

                    //jumlah
                    temp = mBinding.lyDoneEditText.etNumber.getText().toString().trim();
                    try {
                        detailJumlahAdd = Integer.parseInt(temp);
                        if (detailPriceAddSatuan != 0) {

                            detailPriceAdd = detailJumlahAdd * detailPriceAddSatuan;
                            String uang = mataUangHelper.formatRupiah(detailPriceAdd);
                            mBinding.lyAddTransaction.tvDetailPrice.setText(uang);
                        }
                        mBinding.lyAddTransaction.tvJumlah.setText(temp);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(mContext,"satuan :"+detailPriceAddSatuan,Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext,"jumlah :"+detailJumlahAdd,Toast.LENGTH_SHORT).show();

                }

                isCustomer = false;
                isPrice = false;

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.GONE);
            }
        });


        //category transaction
        mBinding.lyAddTransaction.layoutRelatifCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Choose Category Transaction");
                transactionPresenter.getCategoryTransaction();

            }
        });

        //date
        mBinding.lyAddTransaction.layoutRelatifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        sdf = new SimpleDateFormat(FORMAT_TANGGAL_STRING);
                        transDateAdd = sdf.format(myCalendar.getTime());
                        mBinding.lyAddTransaction.tvDate.setText(transDateAdd);

                        sdf = new SimpleDateFormat(FORMAT_TANGGAL_SORT);
                        String temp = sdf.format(myCalendar.getTime());
                        try {
                            transDateSortAdd = Integer.parseInt(temp);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //time
        mBinding.lyAddTransaction.layoutRelatifTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                final int second = myCalendar.get(Calendar.SECOND);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minuteOfDay) {
                                String timeString = "", secondString = "";
                                if (second < 9) {
                                    secondString = "0" + second;
                                } else {
                                    secondString = second + "";
                                }

                                if (hourOfDay < 10) {
                                    if (minuteOfDay < 10) {
                                        timeString = "0" + hourOfDay + ":" + "0" + minuteOfDay + ":" + secondString;
                                    } else {
                                        timeString = "0" + hourOfDay + ":" + minuteOfDay + ":" + secondString;
                                    }
                                } else {
                                    if (minuteOfDay < 10) {
                                        timeString = hourOfDay + ":" + "0" + minuteOfDay + ":" + secondString;
                                    } else {
                                        timeString = hourOfDay + ":" + minuteOfDay + ":" + secondString;
                                    }
                                }

                                transTimeAdd = timeString;
                                mBinding.lyAddTransaction.tvTime.setText(transTimeAdd);
                            }
                        }, hour, minute, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        //product
        mBinding.lyAddTransaction.layoutRelatifProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Choose Product");
                transactionPresenter.getAllProduct();
            }
        });

        mBinding.lyAddTransaction.layoutRelatifJumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etKarakter.setVisibility(View.GONE);

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.VISIBLE);
            }
        });

        //------------------------------- sampai sini -------------------------------------------------------
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

    private int findPosition(String kata, int tipe) {
        ArrayList<String> temp = new ArrayList<>();
        int tempPosition = -1;

        if (!kata.equalsIgnoreCase("")) {
            //category
            if (tipe == 1) {

                for (int i = 0; i < categoryModelGlobal.getCategorySatuanList().size(); i++) {
                    if (categoryModelGlobal.getCategorySatuanList().get(i).getCategoryID().equalsIgnoreCase(kata)) {
                        tempPosition = i;
                        break;
                    }
                }
            }

            //user
            else if (tipe == 2) {


            }

            //product
            else if (tipe == 3) {

                for (int i = 0; i < productModelGlobal.getProductSatuanList().size(); i++) {
                    if (productModelGlobal.getProductSatuanList().get(i).getProductID().equalsIgnoreCase(kata)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }


    private void callCustomList(int flag, int selectedPosition, int sizeArray) {

        flagListChoosen = flag;
        setCustomeList();

        if (selectedPosition != -1) {
            listCustomAdapter.selectedPosition = selectedPosition;
            listCustomAdapter.notifyDataSetChanged();

            if (sizeArray > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(selectedPosition);
            }
        }
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }


    private void setCustomeList() {

        if (flagListChoosen == 1) {
            listCustomAdapter = new ListCustomAdapter(mContext, categoryModelGlobal, 1);
        } else {
            //dummy
            listCustomAdapter = new ListCustomAdapter(mContext, productModelGlobal, 3);
        }


        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (flagListChoosen) {

                    //category
                    case 1:
                        selectedPositionCategory = position;
                        transCatAdd = categoryModelGlobal.getCategorySatuanList().get(position).getCategoryID();
                        mBinding.lyAddTransaction.tvCategory.setText(categoryModelGlobal.getCategorySatuanList().get(position).getCategoryName());

                        break;

                    //user
                    case 2:


                        break;

                    //produk
                    case 3:

                        selectedPositionProduct = position;
                        detailProductAdd = productModelGlobal.getProductSatuanList().get(position).getProductID();
                        mBinding.lyAddTransaction.tvProduct.setText(productModelGlobal.getProductSatuanList().get(position).getProductName());
                        detailPriceAddSatuan = productModelGlobal.getProductSatuanList().get(position).getProductPrice();

                        if(detailJumlahAdd > 0){

                            if(detailJumlahAdd>0){
                                detailPriceAdd = detailPriceAddSatuan * detailJumlahAdd;
                                mBinding.lyAddTransaction.tvDetailPrice.setText(mataUangHelper.formatRupiah(detailPriceAdd));
                            }
                        }

                        Toast.makeText(mContext,"stauan price p: "+detailPriceAddSatuan,Toast.LENGTH_SHORT).show();

                        break;
                }

                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                mBinding.lyBlack2.lyBlack.setVisibility(View.GONE);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
        mBinding.lyBlack2.lyBlack.setVisibility(View.VISIBLE);
    }

    //MVP
    @Override
    public void showAllTransaction(TransactionModel transactionModel) {

        transactionModelGlobal = transactionModel;
        transactionAdapter = new TransactionAdapter(mContext, transactionModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvTransaction.setLayoutManager(layoutManager);
        mBinding.rvTransaction.setAdapter(transactionAdapter);

        rows = transactionModel.getTotalRow();
        if (rows > 0) {
            mBinding.btnNext.setVisibility(View.VISIBLE);
        } else {
            mBinding.btnNext.setVisibility(View.GONE);
        }

        if (page == 0) {
            mBinding.btnPrevius.setVisibility(View.GONE);
        } else {
            mBinding.btnPrevius.setVisibility(View.VISIBLE);

        }

        transactionAdapter.setOnItemClickListener(new TransactionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                transactionPresenter.setOnClickTransaction(transactionModelGlobal,position);
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
    public void onFailedGetAllAPI(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessAddtAllAPI(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showOnClickTransaction(String transID, String transCat, String transDate,
                                       String transTime, String transUserName, String transEmail,
                                       long transBalance, DetailTransactionModel detailTransactionModel) {

        mBinding.lyDetailTransaction.tvCategory.setText(transCat);
        mBinding.lyDetailTransaction.tvTransDate.setText(transDate);
        mBinding.lyDetailTransaction.tvTransTime.setText(transTime);
        mBinding.lyDetailTransaction.tvName.setText(transUserName);
        mBinding.lyDetailTransaction.tvTotalBalance.setText(mataUangHelper.formatRupiah(transBalance));


        detailTransactionAdapter = new DetailTransactionAdapter(mContext, detailTransactionModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.lyDetailTransaction.rvTransactionDetail.setLayoutManager(layoutManager);
        mBinding.lyDetailTransaction.rvTransactionDetail.setAdapter(detailTransactionAdapter);

        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        mBinding.lyDetailTransaction.lyDialogLayoutDetailTransaction.setVisibility(View.VISIBLE);


    }

    @Override
    public void showAddTransactionLayout() {

        detailTransactionModelAdd = new DetailTransactionModel();

        try {
            detailTransactionAddAdapter = new DetailTransactionAdapter(mContext, detailTransactionModelAdd);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            mBinding.lyAddTransaction.rvTransactionAdd.setLayoutManager(layoutManager);
            mBinding.lyAddTransaction.rvTransactionAdd.setAdapter(detailTransactionAddAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDialogListCategory(CategoryModel categoryModel) {
        categoryModelGlobal = categoryModel;
        if (categoryModelGlobal.getCategorySatuanList() != null) {
            sizeArray = categoryModelGlobal.getCategorySatuanList().size();
            if (sizeArray > 0) {

                selectedPositionCategory = findPosition(transCatAdd, 1);
                callCustomList(1, selectedPositionCategory, sizeArray);
            }
        }
    }

    @Override
    public void showDialogListProduct(ProductModel productModel) {
        productModelGlobal = productModel;
        if (productModelGlobal.getProductSatuanList() != null) {
            sizeArray = productModelGlobal.getProductSatuanList().size();
            if (sizeArray > 0) {

                selectedPositionProduct = findPosition(detailProductAdd, 3);
                callCustomList(3, selectedPositionProduct, sizeArray);
            }
        }
    }


    //end
}
