package com.androiddevnkds.kopiseong.module.jurnal;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.ActivityJurnalBinding;
import com.androiddevnkds.kopiseong.databinding.ActivityMainBinding;
import com.androiddevnkds.kopiseong.model.JurnalModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.login.LoginFragment;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JurnalActivity extends BaseActivity implements JurnalContract.jurnalView {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityJurnalBinding mBinding;
    private JurnalPresenter jurnalPresenter;
    private String mDateMonth = "";
    private DateAndTime dateAndTime;
    private MataUangHelper mataUangHelper;

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


        mDateMonth = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING).substring(3, 10);
        Log.e("date", mDateMonth);
        mBinding.tableLayoutTb.setStretchAllColumns(true);
        jurnalPresenter.getAllJurnalPerMonth(mDateMonth);


    }

    @Override
    public void initEvent() {

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

        mBinding.tableLayoutTb.removeAllViews();
        for (int i = -1; i < rows; i++) {

            JurnalModel.JurnalSatuan row = null;
            if (i > -1)
            row = jurnalModel.getJurnalSatuanList().get(i);
            else {
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }

            //date
            final TextView tv2 = new TextView(this);
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv2.setGravity(Gravity.LEFT);
            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setText("Date");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            }else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(row.getJurnalDateList().get(0).getTransDate());
            }

            // data columns
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding(5, 15, 0, 15);

            //income
            if (i == -1) {
                tv.setText("INCOME");
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                long price = 0;
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                for(int j = 0; j<row.getJurnalDateList().size();i++){
                    if(row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("INCOME")){
                        price = row.getJurnalDateList().get(j).getTransPrice();
                        break;
                    }
                }
                tv.setText(String.valueOf(mataUangHelper.formatRupiah(price)));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            //expense
            final LinearLayout layCustomer = new LinearLayout(this);
            layCustomer.setOrientation(LinearLayout.VERTICAL);
            layCustomer.setPadding(0, 10, 0, 10);
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            tv3.setGravity(Gravity.TOP);
            if (i == -1) {
                tv3.setText("EXPENSE");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                long price = 0;
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                for(int j = 0; j<row.getJurnalDateList().size();i++){
                    if(row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("EXPENSE")){
                        price = row.getJurnalDateList().get(j).getTransPrice();
                        break;
                    }
                }
                tv3.setText(mataUangHelper.formatRupiah(price));
            }
            layCustomer.addView(tv3);

            if (i > -1) {
                final TextView tv3b = new TextView(this);
                tv3b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv3b.setGravity(Gravity.RIGHT);
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3b.setPadding(5, 1, 0, 5);
                tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3b.setText("CASH");
                layCustomer.addView(tv3b);
            }
            final LinearLayout layAmounts = new LinearLayout(this);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 0, 10);
            layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));


            //CASH OUT
            final TextView tv4 = new TextView(this);
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setPadding(5, 5, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setPadding(5, 0, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            tv4.setGravity(Gravity.RIGHT);
            if (i == -1) {
                tv4.setText("CASH OUT");
                tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
                long price = 0;
                for(int j = 0; j<row.getJurnalDateList().size();i++){
                    if(row.getJurnalDateList().get(j).getTransCat().equalsIgnoreCase("BUY STOCKWH") &&
                            row.getJurnalDateList().get(j).getTransTipeBayar().equalsIgnoreCase("CASH")){
                        price = row.getJurnalDateList().get(j).getTransPrice();
                    }
                }
                tv4.setText(mataUangHelper.formatRupiah(price));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
            layAmounts.addView(tv4);


            if (i > -1) {
                final TextView tv4b = new TextView(this);
                tv4b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4b.setGravity(Gravity.RIGHT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4b.setText("BUY STOCK, DLL");
                layAmounts.addView(tv4b);
            }


            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);
            tr.addView(tv);
            tr.addView(tv2);

            tr.addView(layCustomer);
            tr.addView(layAmounts);
            if (i > -1) {
                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;

                        //do whatever action is needed

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
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                mBinding.tableLayoutTb.addView(trSep, trParamsSep);
            }


        }

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
