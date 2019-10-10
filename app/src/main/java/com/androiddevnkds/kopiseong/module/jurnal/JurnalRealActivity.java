package com.androiddevnkds.kopiseong.module.jurnal;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ActivityJurnalBinding;
import com.androiddevnkds.kopiseong.model.JurnalModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.summary.JurnalActivity;
import com.androiddevnkds.kopiseong.module.summary.JurnalContract;
import com.androiddevnkds.kopiseong.module.summary.JurnalPresenter;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JurnalRealActivity extends BaseActivity implements JurnalContract.jurnalView {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityJurnalBinding mBinding;
    private JurnalPresenter jurnalPresenter;
    private String mDateMonth = "";
    private DateAndTime dateAndTime;
    private MataUangHelper mataUangHelper;
    private long cashIn = 0, cashOut= 0, accIn = 0, accOut = 0, laba = 0, totalLaba =0, totalIncome =0, totalExpense = 0, totalHpp = 0, hpp = 0, totalCashBalance = 0 , totalAccBal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_jurnal);
        jurnalPresenter = new JurnalPresenter(this);
        dateAndTime = new DateAndTime();
        mataUangHelper  = new MataUangHelper();
        initUI();
        initEvent();
    }


    @Override
    public void initUI() {

        mBinding.lyBottomNav.navigation.setSelectedItemId(R.id.jurnal_menu);
        mDateMonth = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING).substring(3, 10);
        Log.e("date", mDateMonth);
        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelContentText("Jurnal");
        HeaderHelper.setRelativeContentVisible(true);
        HeaderHelper.setLabelContentVisible(true);
        HeaderHelper.setLinearReportVisible(true);
        HeaderHelper.setLinearContentVisible(false);
        mBinding.tableLayoutTb.setStretchAllColumns(true);
        jurnalPresenter.getAllJurnalPerMonth(mDateMonth);



    }

    @Override
    public void initEvent() {


        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent intent = new Intent(JurnalRealActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.summary_menu:
                        Intent intentMenu = new Intent(JurnalRealActivity.this, JurnalActivity.class);
                        startActivity(intentMenu);
                        finish();
                        return true;

                    case R.id.jurnal_menu:
                        Intent intentJurnal = new Intent(JurnalRealActivity.this, JurnalRealActivity.class);
                        startActivity(intentJurnal);
                        finish();
                        return true;
                }

                return false;
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
    public void onFailed(String message) {

        showError(message);
    }

    @Override
    public void showAllJurnalPerMonth(JurnalModel jurnalModel) {

//        hideProgressBar();
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        int rows = jurnalModel.getJurnalSatuanList().size();
        TextView textSpacer = null;
        Log.e("jurnal",jurnalModel.getJurnalSatuanList().size()+"");

        mBinding.tableLayoutTb.removeAllViews();
        for (int i = -1; i < rows; i++) {

            Log.e("jurnal",i+"");

            JurnalModel.JurnalSatuan row = null;
            if (i > -1) {
                row = jurnalModel.getJurnalSatuanList().get(i);
            }
            else {
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }
            Log.e("jurnal",new Gson().toJson(row));

            //---------------------------date-------------------------
            final TextView tv = new TextView(this);
            if (i == -1) {
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);
            } else {
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv.setGravity(Gravity.LEFT);
            tv.setPadding(7, 15, 7, 15);
            if (i == -1) {
                tv.setText("Date");
                tv.setBackgroundColor(Color.parseColor("#f7f7f7"));
            }else {
                tv.setBackgroundColor(Color.parseColor("#ffffff"));
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(row.getJurnalDateList().get(0).getTransDate());
                Log.e("jurnal",row.getJurnalDateList().get(0).getTransDate());
            }

            //--------------------------income cash--------------------------------------
            final LinearLayout layAmounts = new LinearLayout(this);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 15, 10);

            layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv4 = new TextView(this);
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setPadding(5, 5, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setPadding(5, 0, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv4.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv4.setText("Income Cash");
                tv4.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv4.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for (int j = 0; j < row.getJurnalDateList().size(); j++) {
                    if (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH")||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL STOCKWH") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL ASSET") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))) {
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                totalIncome = totalIncome + price;
                tv4.setLayoutParams(params);
                tv4.setText(mataUangHelper.formatRupiah(price));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts.addView(tv4);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                String due = "Income with cash";

                tv4b.setText(due);
                layAmounts.addView(tv4b);

            }

            //--------------------------income Acc-------------------------------------
            final LinearLayout layAmounts2 = new LinearLayout(this);
            layAmounts2.setOrientation(LinearLayout.VERTICAL);
            layAmounts2.setGravity(Gravity.RIGHT);
            layAmounts2.setPadding(0, 10, 15, 10);
            layAmounts2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv2 = new TextView(this);
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setPadding(5, 5, 1, 5);
                layAmounts2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setPadding(5, 0, 1, 5);
                layAmounts2.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv2.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv2.setText("Income Account");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for (int j = 0; j < row.getJurnalDateList().size(); j++) {
                    if (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH")||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL STOCKWH") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL ASSET") &&
                                    !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))) {
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                totalIncome = totalIncome + price;
                tv2.setLayoutParams(params);
                tv2.setText(mataUangHelper.formatRupiah(price));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts2.addView(tv2);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                String due = "Income with OVO, DANA, TRANSFER, stc";

                tv4b.setText(due);
                layAmounts2.addView(tv4b);

            }

            //--------------------------EXPENSE cash--------------------------------------
            final LinearLayout layAmounts3 = new LinearLayout(this);
            layAmounts3.setOrientation(LinearLayout.VERTICAL);
            layAmounts3.setGravity(Gravity.RIGHT);
            layAmounts3.setPadding(0, 10, 15, 10);
            layAmounts3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 1, 5);
                layAmounts3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv3.setPadding(5, 0, 1, 5);
                layAmounts3.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv3.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv3.setText("Expense Cash");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if(row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("EXPENSE")&&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH")){
                        price = row.getJurnalDateList().get(j).getTransPrice();
                        break;
                    }
                }
                totalExpense = totalExpense + price;
                tv3.setLayoutParams(params);
                tv3.setText(mataUangHelper.formatRupiah(price));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts3.addView(tv3);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                String due = "Expense with cash";

                tv4b.setText(due);
                layAmounts3.addView(tv4b);

            }

            //--------------------------expense Acc-------------------------------------
            final LinearLayout layAmounts4 = new LinearLayout(this);
            layAmounts4.setOrientation(LinearLayout.VERTICAL);
            layAmounts4.setGravity(Gravity.RIGHT);
            layAmounts4.setPadding(0, 10, 15, 10);
            layAmounts4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv5 = new TextView(this);
            if (i == -1) {
                tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv5.setPadding(5, 5, 1, 5);
                layAmounts4.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv5.setPadding(5, 0, 1, 5);
                layAmounts4.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv5.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv5.setText("Expense Account");
                tv5.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv5.setBackgroundColor(Color.parseColor("#ffffff"));
                tv5.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for (int j = 0; j < row.getJurnalDateList().size(); j++) {
                    if (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("EXPENSE") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH")) {
                        price = row.getJurnalDateList().get(j).getTransPrice();
                        break;
                    }
                }
                totalExpense = totalExpense + price;
                tv5.setLayoutParams(params);
                tv5.setText(mataUangHelper.formatRupiah(price));
                tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts4.addView(tv5);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                String due = "Expense with OVO, DANA, TRANSFER, stc";

                tv4b.setText(due);
                layAmounts4.addView(tv4b);

            }

            //--------------------------CASH IN--------------------------------------
            final LinearLayout layAmounts5 = new LinearLayout(this);
            layAmounts5.setOrientation(LinearLayout.VERTICAL);
            layAmounts5.setGravity(Gravity.RIGHT);
            layAmounts5.setPadding(0, 10, 15, 10);
            layAmounts5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv6 = new TextView(this);
            if (i == -1) {
                tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv6.setPadding(5, 5, 1, 5);
                layAmounts5.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv6.setPadding(5, 0, 1, 5);
                layAmounts5.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv6.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv6.setText("Cash In");
                tv6.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv6.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv6.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if((row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL STOCKWH") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL ASSET") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("ADD CASH") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))){
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                cashIn = cashIn + price;
                tv6.setLayoutParams(params);
                tv6.setText(mataUangHelper.formatRupiah(price));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts5.addView(tv6);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                String due = "Sell asset, stock, stc with CASH";

                tv4b.setText(due);
                layAmounts5.addView(tv4b);

            }

            //------------------------- Acc  OUT------------------------------------
            final LinearLayout layAmounts6 = new LinearLayout(this);
            layAmounts6.setOrientation(LinearLayout.VERTICAL);
            layAmounts6.setGravity(Gravity.RIGHT);
            layAmounts6.setPadding(0, 10, 15, 10);
            layAmounts6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv7 = new TextView(this);
            if (i == -1) {
                tv7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv7.setPadding(5, 5, 1, 5);
                layAmounts6.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv7.setPadding(5, 0, 1, 5);
                layAmounts6.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv5.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv7.setText("Account In");
                tv7.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv7.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv7.setBackgroundColor(Color.parseColor("#ffffff"));
                tv7.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if((row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL STOCKWH") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL ASSET") &&
                                    !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("ADD ACC") &&
                                    !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))){
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                accIn = accIn + price;
                tv7.setLayoutParams(params);
                tv7.setText(mataUangHelper.formatRupiah(price));
                tv7.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts6.addView(tv7);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                String due = "Sell asset, stock, stc with OVO, Transfer, stc";

                tv4b.setText(due);
                layAmounts6.addView(tv4b);

            }

            //--------------------------CASH OUT--------------------------------------
            final LinearLayout layAmounts7 = new LinearLayout(this);
            layAmounts7.setOrientation(LinearLayout.VERTICAL);
            layAmounts7.setGravity(Gravity.RIGHT);
            layAmounts7.setPadding(0, 10, 15, 10);
            layAmounts7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv8 = new TextView(this);
            if (i == -1) {
                tv8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv8.setPadding(5, 5, 1, 5);
                layAmounts7.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv8.setPadding(5, 0, 1, 5);
                layAmounts7.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv6.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv8.setText("Cash Out");
                tv8.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv8.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv8.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv8.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if((row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("EXPENSE") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("BUY STOCKWH") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("BUY ASSET") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("MIN CASH") &&
                                    row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))){
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                cashOut = cashOut + price;
                tv8.setLayoutParams(params);
                tv8.setText(mataUangHelper.formatRupiah(price));
                tv8.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts7.addView(tv8);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                String due = "Buy asset, stock, stc with CASH";

                tv4b.setText(due);
                layAmounts7.addView(tv4b);

            }


            //------------------------- Acc Out-------------------------------------
            final LinearLayout layAmounts8 = new LinearLayout(this);
            layAmounts8.setOrientation(LinearLayout.VERTICAL);
            layAmounts8.setGravity(Gravity.RIGHT);
            layAmounts8.setPadding(0, 10, 15, 10);
            layAmounts8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            final TextView tv9 = new TextView(this);
            if (i == -1) {
                tv9.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv9.setPadding(5, 5, 1, 5);
                layAmounts8.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv9.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv9.setPadding(5, 0, 1, 5);
                layAmounts8.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv5.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv9.setText("Account Out");
                tv9.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv9.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv9.setBackgroundColor(Color.parseColor("#ffffff"));
                tv9.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if((row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("EXPENSE") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("BUY STOCKWH") &&
                            !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("BUY ASSET") &&
                                    !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))||
                            (row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("MIN ACC") &&
                                    !row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH"))){
                        price = price + row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                accOut = accOut + price;
                tv9.setLayoutParams(params);
                tv9.setText(mataUangHelper.formatRupiah(price));
                tv9.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts8.addView(tv9);

            if (i > -1) {
                final TextView tv4b = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv4b.setLayoutParams(params);
                tv4b.setGravity(Gravity.LEFT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                String due = "Buy asset, stock, stc with OVO, Transfer, stc";

                tv4b.setText(due);
                layAmounts8.addView(tv4b);

            }

            //--------------------------HPP--------------------------------------
            final LinearLayout layAmounts9 = new LinearLayout(this);
            layAmounts9.setOrientation(LinearLayout.VERTICAL);
            layAmounts9.setGravity(Gravity.RIGHT);
            layAmounts9.setPadding(0, 10, 15, 10);
            layAmounts9.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv10 = new TextView(this);
            if (i == -1) {
                tv10.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv10.setPadding(5, 5, 1, 5);
                layAmounts9.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv10.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv10.setPadding(5, 0, 1, 5);
                layAmounts9.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv6.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv10.setText("HPP");
                tv10.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv10.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv10.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv10.setTextColor(Color.parseColor("#000000"));
                long price =0;
                for(int j = 0; j<row.getJurnalDateList().size();j++){
                    if(row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME")||
                            row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL STOCKWH")||
                            row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("SELL ASSET")){
                        price = price + row.getJurnalDateList().get(j).getTransHpp();
                    }
                }
                hpp = hpp + price;
                tv10.setLayoutParams(params);
                tv10.setText(mataUangHelper.formatRupiah(price));
                tv10.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }
            layAmounts9.addView(tv10);

            laba = totalIncome - totalExpense - hpp;
            totalLaba = totalLaba + laba;

            //------------------------- laba-------------------------------------
            final LinearLayout layAmounts10 = new LinearLayout(this);
            layAmounts10.setOrientation(LinearLayout.VERTICAL);
            layAmounts10.setGravity(Gravity.RIGHT);
            layAmounts10.setPadding(0, 10, 15, 10);
            layAmounts10.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            final TextView tv11 = new TextView(this);
            if (i == -1) {
                tv11.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv11.setPadding(5, 5, 1, 5);
                layAmounts10.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv11.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv11.setPadding(5, 0, 1, 5);
                layAmounts10.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv5.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv11.setText("Laba/Rugi");
                tv11.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv11.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv11.setBackgroundColor(Color.parseColor("#ffffff"));
                tv11.setTextColor(Color.parseColor("#000000"));

                tv11.setLayoutParams(params);
                tv11.setText(mataUangHelper.formatRupiah(laba));
                laba = 0;
                totalIncome = 0;
                totalExpense = 0;
                tv11.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts10.addView(tv11);

            long cashBalance = cashIn - cashOut;
            totalCashBalance = totalCashBalance + cashBalance ;

            //--------------------------CASH BALANCE--------------------------------------
            final LinearLayout layAmounts11 = new LinearLayout(this);
            layAmounts11.setOrientation(LinearLayout.VERTICAL);
            layAmounts11.setGravity(Gravity.RIGHT);
            layAmounts11.setPadding(0, 10, 15, 10);
            layAmounts11.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            final TextView tv12 = new TextView(this);
            if (i == -1) {
                tv12.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv12.setPadding(5, 5, 1, 5);
                layAmounts11.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv12.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv12.setPadding(5, 0, 1, 5);
                layAmounts11.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }
            tv6.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv12.setText("CASH BALANCE");
                tv12.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv12.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv12.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv12.setTextColor(Color.parseColor("#000000"));

                tv12.setLayoutParams(params);
                tv12.setText(mataUangHelper.formatRupiah(cashBalance));
                cashIn = 0;
                cashOut =0;
                tv12.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }
            layAmounts11.addView(tv12);


            long accBalance = accIn - accOut;
            totalAccBal = totalAccBal + accBalance;
            //------------------------- Acc BAlanace-------------------------------------
            final LinearLayout layAmounts12 = new LinearLayout(this);
            layAmounts12.setOrientation(LinearLayout.VERTICAL);
            layAmounts12.setGravity(Gravity.RIGHT);
            layAmounts12.setPadding(0, 10, 15, 10);
            layAmounts12.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            final TextView tv13 = new TextView(this);
            if (i == -1) {
                tv13.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv13.setPadding(5, 5, 1, 5);
                layAmounts12.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv13.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv13.setPadding(5, 0, 1, 5);
                layAmounts12.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv5.setGravity(Gravity.LEFT);

            if (i == -1) {
                tv13.setText("Account Balancce");
                tv13.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv13.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize);

            } else {
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(7);
                    params.setMarginEnd(15);
                }
                tv13.setBackgroundColor(Color.parseColor("#ffffff"));
                tv13.setTextColor(Color.parseColor("#000000"));

                tv13.setLayoutParams(params);
                tv13.setText(mataUangHelper.formatRupiah(accBalance));

                accIn = 0;
                accOut = 0;
                tv11.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            }

            layAmounts12.addView(tv13);

            //row

            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);
            tr.addView(tv);
            tr.addView(layAmounts);
            tr.addView(layAmounts2);
            tr.addView(layAmounts3);
            tr.addView(layAmounts4);
            tr.addView(layAmounts5);
            tr.addView(layAmounts6);
            tr.addView(layAmounts7);
            tr.addView(layAmounts8);
            tr.addView(layAmounts9);
            tr.addView(layAmounts10);
            tr.addView(layAmounts11);
            tr.addView(layAmounts12);

            if (i > -1) {
                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;

//                        Toast.makeText(getApplicationContext(),"tes",Toast.LENGTH_SHORT).show();

                    }
                });
            }
            mBinding.tableLayoutTb.addView(tr, trParams);
            if (i > -1) {
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 13;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                mBinding.tableLayoutTb.addView(trSep, trParamsSep);
            }



        }

        mBinding.linearSum.setVisibility(View.VISIBLE);
        mBinding.tvAccount.setText(mataUangHelper.formatRupiah(totalAccBal));
        mBinding.tvCash.setText(mataUangHelper.formatRupiah(totalCashBalance));
        mBinding.tvLaba.setText(mataUangHelper.formatRupiah(totalLaba));

    }

    @Override
    public void showMonthFirstTime(String date, List<String> listDate) {

    }

    @Override
    public void showDateList(List<String> dateList, int pos) {

    }

    @Override
    public void showResultConvert(String date) {

    }

    @Override
    public void showResultDateString(String date) {

    }


    private void showError(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
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
