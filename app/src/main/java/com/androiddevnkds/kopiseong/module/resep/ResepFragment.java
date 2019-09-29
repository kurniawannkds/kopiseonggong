package com.androiddevnkds.kopiseong.module.resep;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.ResepAdapter;
import com.androiddevnkds.kopiseong.adapter.ResepItemAdapter;
import com.androiddevnkds.kopiseong.adapter.TransactionAdapter;
import com.androiddevnkds.kopiseong.databinding.FragmentLoginBinding;
import com.androiddevnkds.kopiseong.databinding.FragmentResepBinding;
import com.androiddevnkds.kopiseong.model.ResepItemModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.login.model.LoginCredential;
import com.androiddevnkds.kopiseong.module.register.RegisterFragment;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResepFragment extends BaseFragment implements ResepContract.resepView {

    private FragmentResepBinding mBinding;
    private Context mContext;
    private boolean isEditResep = false;
    private ResepModel resepModelGlobal;
    private ResepAdapter resepAdapter;
    private ResepPresenter resepPresenter;
    private ResepItemAdapter resepItemAdapter;
    private ResepModel.ResepModelSatuan resepModelSatuanGlobal;
    private List<ResepItemModel> resepItemModelList;
    private int positionEdited = -1, positionClicked = -1;
    private double updateHpp = 0;
    private ListCustomAdapter listCustomAdapter;

    //add resep
    private boolean isResepID = false, isResepItem = false, isResepJumlah, isResepHpp = false, isDialogKeypad = false, isAddResep = false;
    private String resepID = "", resepItem = "", resepJumlah = "", resepItemName = "";
    private long resepHpp = 0;

    public ResepFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_resep, container, false);
        resepPresenter = new ResepPresenter(this);
        initUI();
        initEvent();


        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelText("Resep");
        HeaderHelper.setLabelVisible(true);

        resepPresenter.getAllResep();
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

        mBinding.lyBlack.lyBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isEditResep) {
                    mBinding.lyDialogEditTextResep.lyDialogLayoutEditResep.setVisibility(View.GONE);
                    mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.VISIBLE);
                    isEditResep = false;
                } else if (isAddResep) {
                    if (isDialogKeypad) {
                        mBinding.lyDoneEditText.etNumber.setText("");
                        mBinding.lyDoneEditText.etKarakter.setText("");
                        mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.VISIBLE);
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
                        isDialogKeypad = false;
                    } else if (isResepItem) {
                        mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.VISIBLE);
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.GONE);
                        isResepItem = false;
                    } else {
                        mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.GONE);
                        mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                        isAddResep = false;
                    }
                } else {
                    mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                    mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.GONE);
                }
            }
        });

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAddResep = true;
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.VISIBLE);
                resepID = "";
                resepItem = "";
                resepJumlah = "";
                resepHpp = 0;

                mBinding.lyDialogAddResep.tvResepId.setText("");
                mBinding.lyDialogAddResep.tvResepItem.setText("");
                mBinding.lyDialogAddResep.tvJumlahItem.setText("");
                mBinding.lyDialogAddResep.tvResepHpp.setText("");
            }
        });

        //add resep id
        mBinding.lyDialogAddResep.layoutRelatifResepID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input ResepID");
                mBinding.lyDoneEditText.etKarakter.setHint("ex : RDAEJEONCOFFEE");
                mBinding.lyDoneEditText.etKarakter.setText(resepID);
                isDialogKeypad = true;
                isResepID = true;
                mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.GONE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.GONE);
            }
        });

        //add resep item
        mBinding.lyDialogAddResep.layoutRelatifItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isDialogKeypad = true;
                isResepItem = true;

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Input Resep Item");
                resepPresenter.getAllStock(resepItem);
            }
        });

        //add jumlah item
        mBinding.lyDialogAddResep.layoutRelatifTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isDialogKeypad = true;
                isResepJumlah = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input Jumlah Gram Item");
                mBinding.lyDoneEditText.etKarakter.setHint("ex : 20,40  (without space!)");
                mBinding.lyDoneEditText.etKarakter.setText(resepJumlah);
                mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.GONE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.GONE);
            }
        });

//        //add resep hpp
//        mBinding.lyDialogAddResep.layoutRelatifItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                isDialogKeypad = true;
//                isResepHpp = true;
//                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input HPP");
//                mBinding.lyDoneEditText.etKarakter.setHint("ex : 100000  (decimal only!)");
//                mBinding.lyDoneEditText.etKarakter.setVisibility(View.GONE);
//                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
//                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
//                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.GONE);
//            }
//        });

        //edit text submit
        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resepid
                if (isResepID) {
                    resepID = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    mBinding.lyDialogAddResep.tvResepId.setText(resepID);
                }

                //jumlah
                else if (isResepJumlah) {
                    resepJumlah = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                    mBinding.lyDialogAddResep.tvJumlahItem.setText(resepJumlah);
                    if (!resepJumlah.equalsIgnoreCase("")) {
                        mBinding.lyDialogAddResep.linearClearJumlah.setVisibility(View.VISIBLE);
                    }
                    if (!resepItem.equalsIgnoreCase("")) {
                        checkHpp();
                    }
                }

                //hpp
                else {
                    String resepTemp = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",", "");
                    try {
                        resepHpp = Integer.parseInt(resepTemp);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    MataUangHelper mataUangHelper = new MataUangHelper();
                    resepTemp = mataUangHelper.formatRupiah(resepHpp);
                    mBinding.lyDialogAddResep.tvResepItem.setText(resepTemp);

                }

                isResepID = false;
                isResepItem = false;
                isResepJumlah = false;
                isResepHpp = false;
                isDialogKeypad = false;


                mBinding.lyDoneEditText.etKarakter.setText("");
                mBinding.lyDoneEditText.etNumber.setText("");
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.GONE);
            }
        });

        //submit add
        mBinding.lyDialogAddResep.btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResepModel.ResepModelSatuan resepModelSatuan = new ResepModel().new ResepModelSatuan();
                resepModelSatuan.setResepID(resepID);
                resepModelSatuan.setResepItem(resepItem);
                resepModelSatuan.setResepJumlahItem(resepJumlah);
                resepModelSatuan.setResepTotalPrice(resepHpp);

                resepPresenter.addResep(resepModelSatuan);
            }
        });

        //clear all item
        mBinding.lyDialogAddResep.btnClearAllItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resepItem = "";
                mBinding.lyDialogAddResep.tvResepItem.setText("");
                mBinding.lyDialogAddResep.linearClearItem.setVisibility(GONE);
            }
        });

        //clear last item
        mBinding.lyDialogAddResep.btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resepItem.contains(",")) {
                    String[] splited = resepItem.trim().split(",");
                    resepItem = "";
                    for (int i = 0; i < splited.length - 1; i++) {
                        if (i == splited.length - 2) {
                            resepItem = resepItem + splited[i];
                        } else {
                            resepItem = resepItem + splited[i] + ",";
                        }
                    }
                    mBinding.lyDialogAddResep.tvResepItem.setText(resepItem);
                } else {
                    resepItem = "";
                    mBinding.lyDialogAddResep.tvResepItem.setText(resepItem);
                    mBinding.lyDialogAddResep.linearClearItem.setVisibility(GONE);
                }
            }
        });


        //clear all jumlah
        mBinding.lyDialogAddResep.btnClearAllJumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resepJumlah = "";
                mBinding.lyDialogAddResep.tvJumlahItem.setText("");
                mBinding.lyDialogAddResep.linearClearJumlah.setVisibility(GONE);
            }
        });

        //clear last jumlah
        mBinding.lyDialogAddResep.btnClearJumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resepJumlah.contains(",")) {
                    String[] splited = resepJumlah.trim().split(",");
                    resepJumlah = "";
                    for (int i = 0; i < splited.length - 1; i++) {
                        if (i == splited.length - 2) {
                            resepJumlah = resepJumlah + splited[i];
                        } else {
                            resepJumlah = resepJumlah + splited[i] + ",";
                        }
                    }
                    mBinding.lyDialogAddResep.tvJumlahItem.setText(resepJumlah);
                } else {
                    resepJumlah = "";
                    mBinding.lyDialogAddResep.tvJumlahItem.setText(resepJumlah);
                    mBinding.lyDialogAddResep.linearClearJumlah.setVisibility(GONE);
                }
            }
        });

    }

    @Override
    public void setHPP(long hpp, int tipe, String hasilItem, String hasilJumlahItem) {

        if (tipe == 1) {
            resepHpp = hpp;
            MataUangHelper mataUangHelper = new MataUangHelper();
            String uang = mataUangHelper.formatRupiah(hpp);
            mBinding.lyDialogAddResep.tvResepHpp.setText(uang);
        } else if(tipe==2){
            updateHpp = hpp;
            ResepModel.ResepModelSatuan resepModelSatuan = new ResepModel().new ResepModelSatuan();
            resepModelSatuan.setResepID(resepModelSatuanGlobal.getResepID());
            resepModelSatuan.setResepTotalPrice(updateHpp);
            resepModelSatuan.setResepItem(hasilItem);
            resepModelSatuan.setResepJumlahItem(hasilJumlahItem);
            resepPresenter.updateResep(resepModelSatuan);

        }
        else {
            MataUangHelper mataUangHelper = new MataUangHelper();
            String uang = mataUangHelper.formatRupiah(hpp);
            mBinding.lyDialogDetailResep.tvResepHpp.setText(uang);
        }
    }

    @Override
    public void showAllStock(StockModel stockModel, int selectedPos) {

        setCustomeList(stockModel);

        if (selectedPos != -1) {
            listCustomAdapter.selectedPosition = selectedPos;
            listCustomAdapter.notifyDataSetChanged();

            if (stockModel.getStockSatuanModelList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(selectedPos);
            }
        }
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);

    }

    //delete resep
    @Override
    public void succesDeleteResep(String message, final int position) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Delete Success");
        pDialog.setConfirmText("Yes");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.showCancelButton(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                resepModelGlobal.getResepModelSatuanList().remove(position);
                resepAdapter.resepResepAll(resepModelGlobal);
                resepAdapter.notifyDataSetChanged();
                mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void successAddResep(String message, ResepModel.ResepModelSatuan resepModelSatuan) {

        resepModelGlobal.getResepModelSatuanList().add(resepModelSatuan);
        resepAdapter.resepResepAll(resepModelGlobal);
        resepAdapter.notifyDataSetChanged();

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.showCancelButton(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                isAddResep = false;
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
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

    //validasi2
    @Override
    public void onFailed(final int tipe, String message) {


        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
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
    public void showAllResep(final ResepModel resepModel) {

        resepModelGlobal = resepModel;

        resepAdapter = new ResepAdapter(mContext, resepModelGlobal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvResep.setLayoutManager(layoutManager);
        mBinding.rvResep.setAdapter(resepAdapter);

        resepAdapter.setOnItemClickListener(new ResepAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                positionClicked = position;
                resepPresenter.setOnClickResep(resepModel, position);
            }
        });
    }

    @Override
    public void showSuccessUpdate(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.GONE);
                mBinding.lyBlack.lyBlack.setVisibility(View.GONE);
                pDialog.dismiss();
                resepPresenter.getAllResep();
            }
        });
    }

    @Override
    public void updateResepItem(final ResepItemModel resepItemModel, int pos) {

        resepItemModelList.remove(pos);
        resepItemModelList.add(pos, resepItemModel);
        resepItemAdapter.resetList(resepItemModelList);
        resepItemAdapter.notifyDataSetChanged();
        mBinding.lyDialogEditTextResep.lyDialogLayoutEditResep.setVisibility(View.GONE);
        mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.VISIBLE);
        isEditResep = false;

        mBinding.lyDialogDetailResep.layoutOke.setVisibility(View.VISIBLE);
        mBinding.lyDialogDetailResep.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hasilItem = "";
                String hasilJumlahItem = "";
                for (int i = 0; i < resepItemModelList.size(); i++) {

                    if (i == resepItemModelList.size() - 1) {
                        hasilItem = hasilItem + resepItemModelList.get(i).getItemName();
                        hasilJumlahItem = hasilJumlahItem + resepItemModelList.get(i).getItemJumlah();
                    } else {
                        hasilItem = hasilItem + resepItemModelList.get(i).getItemName() + ",";
                        hasilJumlahItem = hasilJumlahItem + resepItemModelList.get(i).getItemJumlah() + ",";
                    }
                }
                if (hasilJumlahItem.length() == 0) {
                    mBinding.lyDialogEditTextResep.lyDialogLayoutEditResep.setVisibility(View.GONE);
                    mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.VISIBLE);
                } else {

                    resepPresenter.getCountHPP(hasilItem, hasilJumlahItem, 2);
                }
            }
        });
    }

    @Override
    public void showEditDialog(final ResepItemModel resepItemModel) {

        isEditResep = true;
        mBinding.lyDialogEditTextResep.tvResepItem.setText(resepItemModel.getItemName());
        mBinding.lyDialogEditTextResep.etResepItemJumlah.setText(resepItemModel.getItemJumlah());
        final ResepItemModel newResepItemModel = new ResepItemModel();

        mBinding.lyDialogEditTextResep.etResepItemJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String newJumlah = mBinding.lyDialogEditTextResep.etResepItemJumlah.getText().toString().trim();
                newResepItemModel.setItemName(resepItemModel.getItemName());
                newResepItemModel.setItemJumlah(newJumlah);
            }
        });

        mBinding.lyDialogEditTextResep.btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resepPresenter.getDataAfterEdit(newResepItemModel, positionEdited);
            }
        });
        Log.e("adapter", "show layout");
        mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.GONE);
        mBinding.lyDialogEditTextResep.lyDialogLayoutEditResep.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResepSatuan(final ResepModel.ResepModelSatuan resepModelSatuan, final List<ResepItemModel> resepItemModel) {

        Log.e("adapter", "kepanggil di fungsi ga 1");
        resepModelSatuanGlobal = resepModelSatuan;

        mBinding.lyDialogDetailResep.tvResepId.setText(resepModelSatuan.getResepID());
        mBinding.lyDialogDetailResep.tvResepItem.setText(resepModelSatuan.getResepItem());
        mBinding.lyDialogDetailResep.tvResepItemJumlah.setText(resepModelSatuan.getResepJumlahItem());
        resepPresenter.getCountHPP(resepModelSatuan.getResepItem(),resepModelSatuan.getResepJumlahItem(),3);

        resepItemModelList = resepItemModel;
        resepItemAdapter = new ResepItemAdapter(mContext, resepItemModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.lyDialogDetailResep.rvItem.setLayoutManager(layoutManager);
        mBinding.lyDialogDetailResep.rvItem.setAdapter(resepItemAdapter);

        resepItemAdapter.setOnItemClickListener(new ResepItemAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                positionEdited = position;
                Log.e("adapter", "kepanggil di fungsi ga");
                resepPresenter.setOnClickEditResep(resepItemModel, position);
            }
        });

        mBinding.lyDialogDetailResep.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Delete Warning");
                pDialog.setContentText("Are you sure you want to delete " + resepModelSatuan.getResepID() + " ?");
                pDialog.setCancelText("No");
                pDialog.setConfirmText("Yes");
                pDialog.showCancelButton(true);
                pDialog.show();
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("Cancelled!")
                                .setContentText("Your resep is safe :)")
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

                        resepPresenter.deleteResep(resepModelSatuan.getResepID(), positionClicked);
                        pDialog.dismiss();
                    }
                });
            }
        });

        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        mBinding.lyDialogDetailResep.lyDialogLayoutDetailResep.setVisibility(View.VISIBLE);
    }


    //end
    private void checkHpp() {

        if (!resepItem.equalsIgnoreCase("") && !resepJumlah.equalsIgnoreCase("")) {

            resepPresenter.getCountHPP(resepItem, resepJumlah, 1);
        }
    }

    private void setCustomeList(final StockModel stockModel) {

        listCustomAdapter = new ListCustomAdapter(mContext, stockModel, 5);
        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (resepItem.equalsIgnoreCase("")) {
                    resepItem = stockModel.getStockSatuanModelList().get(position).getStockID();
                } else {
                    resepItem = resepItem + "," + stockModel.getStockSatuanModelList().get(position).getStockID();
                }

                if (!resepJumlah.equalsIgnoreCase("")) {
                    checkHpp();
                }

                mBinding.lyDialogAddResep.tvResepItem.setText(resepItem);
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                mBinding.lyDialogAddResep.lyDialogLayoutAddResep.setVisibility(View.VISIBLE);
                mBinding.lyDialogAddResep.linearClearItem.setVisibility(View.VISIBLE);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }
}
