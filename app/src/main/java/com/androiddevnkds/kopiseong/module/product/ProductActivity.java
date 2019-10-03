package com.androiddevnkds.kopiseong.module.product;

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
import com.androiddevnkds.kopiseong.adapter.DetailTransactionAdapter;
import com.androiddevnkds.kopiseong.adapter.ListCustomAdapter;
import com.androiddevnkds.kopiseong.adapter.ProductAdapter;
import com.androiddevnkds.kopiseong.databinding.ActivityGeneralBinding;
import com.androiddevnkds.kopiseong.databinding.ActivityProductBinding;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.resep.ResepFragment;
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

public class ProductActivity extends BaseActivity implements ProductContract.productView {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityProductBinding mBinding;
    private String mCategoryFilter = "ALL";
    private ProductPresenter productPresenter;
    private ResepModel resepForList;
    private List<String> catForList, catForList2;
    private ListCustomAdapter listCustomAdapter;
    private ProductAdapter productAdapter;
    private boolean isDialogCustome = false, isEditProd = false, isAddProd = false, isDialogText = false, isID = false, isName = false, isPrice = false;
    private ProductModel productModelGlobal;
    private ProductModel.ProductSatuan productSatuanGlobal;
    private int posDetail;

    //edit
    private String mEProdID ="", mEProdName = "", mEProdResep = "", mEProdCat ="";
    private long mEProdPrice = 0;

    //add
    private String mAProdID ="", mAProdName = "", mAProdResep = "", mAProdCat ="";
    private long mAProdPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        productPresenter = new ProductPresenter(this);

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

        mBinding.lyHeaderData.tvDate.setText(mCategoryFilter);

        catForList = new ArrayList<>();
        catForList2 = new ArrayList<>();
        catForList.add("ALL");
        catForList.add(K.GENERAL_CATEGORY_PROD_KOPI);
        catForList.add(K.GENERAL_CATEGORY_PROD_NON_KOPI);

        catForList2.add(K.GENERAL_CATEGORY_PROD_KOPI);
        catForList2.add(K.GENERAL_CATEGORY_PROD_NON_KOPI);

        productPresenter.getAllProduct(mCategoryFilter);

    }

    @Override
    public void initEvent() {

        mBinding.lyHeaderData.tvIconFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditProd = false;
                isAddProd = false;
                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Choose General Category");
                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                productPresenter.getCategoryGeneral(catForList,mCategoryFilter);
            }
        });

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.transaction_menu:
                        Intent intentTrans = new Intent(ProductActivity.this, TransactionActivity.class);
                        intentTrans.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
                        finish();

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(ProductActivity.this, StockActivity.class);
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

                if(isEditProd || isAddProd){
                    if (isDialogCustome){
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);
                        isDialogCustome = false;
                    }
                    else if(isDialogText){
                        mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                        mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);
                        isDialogText = false;
                    }
                    else {
                        mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
                        mBinding.lyBlack.lyBlack.setVisibility(GONE);
                        isEditProd = false;
                        isAddProd = false;
                    }
                }
                else {
                    if (isDialogCustome){
                        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
                        isDialogCustome = false;
                    }
                    mBinding.lyBlack.lyBlack.setVisibility(GONE);
                }
            }
        });

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isEditProd = false;
                isAddProd = true;

                mBinding.lyDetailProduct.tvTitle.setText("Add Product");

                mEProdID = "";
                mEProdName = "";
                mEProdCat = "";
                mEProdResep = "";
                mEProdPrice = 0;


                mBinding.lyDetailProduct.tvProdId.setText(mEProdID);
                mBinding.lyDetailProduct.tvProdName.setText(mEProdName);
                mBinding.lyDetailProduct.tvProdCategory.setText(mEProdCat);
                mBinding.lyDetailProduct.tvProdResep.setText(mEProdResep);
                mBinding.lyDetailProduct.tvProdPrice.setText("");
                mBinding.lyDetailProduct.tvProdHpp.setText("");

                mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);
            }
        });

        //-----------------------------------------edit-------------------------------------------

        mBinding.lyDetailProduct.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Delete Warning");
                pDialog.setContentText("Are you sure you want to delete this product ?");
                pDialog.setCancelText("No");
                pDialog.setConfirmText("Yes");
                pDialog.showCancelButton(true);
                pDialog.show();
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        pDialog.dismiss();
                        productPresenter.deleteProduct(productSatuanGlobal.getProductID(),posDetail);

                    }
                });
            }
        });

        //id
        mBinding.lyDetailProduct.layoutRelatifProdID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isID = true;
                isDialogText = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input product ID max 40 characters");
                if(isEditProd){
                    if(!mEProdID.equalsIgnoreCase("")){
                        mBinding.lyDoneEditText.etKarakter.setText(mEProdID);
                    }
                    else {
                        mBinding.lyDoneEditText.etKarakter.setText( "");
                    }
                }
                else if(isAddProd){
                    if(!mAProdID.equalsIgnoreCase("")){
                        mBinding.lyDoneEditText.etKarakter.setText(mAProdID);
                    }
                    else {
                        mBinding.lyDoneEditText.etKarakter.setText( "");
                    }
                }

                mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
            }
        });

        //name
        mBinding.lyDetailProduct.layoutRelatifProdName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isName = true;
                isDialogText = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input product name max 100 characters");
                if(isEditProd){
                    if(!mEProdID.equalsIgnoreCase("")){
                        mBinding.lyDoneEditText.etKarakter.setText(mEProdName);
                    }
                    else {
                        mBinding.lyDoneEditText.etKarakter.setText( "");
                    }
                }
                else if(isAddProd){
                    if(!mAProdID.equalsIgnoreCase("")){
                        mBinding.lyDoneEditText.etKarakter.setText(mAProdName);
                    }
                    else {
                        mBinding.lyDoneEditText.etKarakter.setText( "");
                    }
                }

                mBinding.lyDoneEditText.etKarakter.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.etNumber.setVisibility(GONE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
            }
        });

        //resep
        mBinding.lyDetailProduct.layoutRelatifProdResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Choose Recipe");
                if(isEditProd) {
                    productPresenter.getResepList(resepForList,mEProdResep );
                }
                else {
                    productPresenter.getResepList(resepForList,mAProdResep );
                }
            }
        });

        //price
        mBinding.lyDetailProduct.layoutRelatifProdPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPrice = true;
                isDialogText = true;
                mBinding.lyDoneEditText.tvEditTextLabel.setText("Input product price with number only");
                if(isEditProd){
                    if(mEProdPrice!=0){
                        mBinding.lyDoneEditText.etNumber.setText(mEProdPrice+"");
                    }
                    else {
                        mBinding.lyDoneEditText.etNumber.setText( "");
                    }
                }
                else if(isAddProd){
                    if(mAProdPrice!=0){
                        mBinding.lyDoneEditText.etNumber.setText(mAProdPrice+"");
                    }
                    else {
                        mBinding.lyDoneEditText.etNumber.setText( "");
                    }
                }

                mBinding.lyDoneEditText.etKarakter.setVisibility(GONE);
                mBinding.lyDoneEditText.etNumber.setVisibility(View.VISIBLE);
                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(View.VISIBLE);
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
            }
        });

        //category
        mBinding.lyDetailProduct.layoutRelatifProdCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mBinding.lyDialogCustomeList.tvDialogListLabel.setText("Choose General Category");
                if(isEditProd) {
                    productPresenter.getCategoryGeneral(catForList2,mEProdCat );
                }
                else {
                    productPresenter.getCategoryGeneral(catForList2,mAProdCat);
                }
            }
        });

        //oke detail
        mBinding.lyDetailProduct.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEditProd) {
                    if (mEProdID.equalsIgnoreCase(productSatuanGlobal.getProductID()) &&
                            mEProdCat.equalsIgnoreCase(productSatuanGlobal.getProductGeneralCategory()) &&
                            mEProdName.equalsIgnoreCase(productSatuanGlobal.getProductName()) &&
                            mEProdResep.equalsIgnoreCase(productSatuanGlobal.getProductResepID()) &&
                            mEProdPrice == productSatuanGlobal.getProductPrice()) {

                        isEditProd = false;
                        mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
                        mBinding.lyBlack.lyBlack.setVisibility(GONE);
                    } else {

                        ProductModel.ProductSatuan productSatuan = new ProductModel().new ProductSatuan();
                        productSatuan.setProductID(mEProdID);
                        productSatuan.setProductGeneralCategory(mEProdCat);
                        productSatuan.setProductName(mEProdName);
                        productSatuan.setProductPrice(mEProdPrice);
                        productSatuan.setProductResepID(mEProdResep);

                        productPresenter.editProduct(productSatuan, posDetail);
                    }
                }
                else {

                    int size = productModelGlobal.getProductSatuanList().size();
                    ProductModel.ProductSatuan productSatuan = new ProductModel().new ProductSatuan();
                    productSatuan.setProductID(mAProdID);
                    productSatuan.setProductGeneralCategory(mAProdCat);
                    productSatuan.setProductName(mAProdName);
                    productSatuan.setProductPrice(mAProdPrice);
                    productSatuan.setProductResepID(mAProdResep);

                    productPresenter.addProduct(productSatuan, size);
                }
            }
        });

        //------------------------------------edit finish----------------------------------------------------

        //btn edit text
        mBinding.lyDoneEditText.btnDoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tempKarakter = mBinding.lyDoneEditText.etKarakter.getText().toString().trim();
                String tempNumber = mBinding.lyDoneEditText.etNumber.getText().toString().trim().replace(",","");
                if(isID){

                    if(isEditProd){
                        mEProdID = tempKarakter;
                        mBinding.lyDetailProduct.tvProdId.setText(mEProdID);
                    }
                    else {
                        mAProdID = tempKarakter;
                        mBinding.lyDetailProduct.tvProdId.setText(mAProdID);
                    }
                }

                else if(isName){

                    if(isEditProd){
                        mEProdName = tempKarakter;
                        mBinding.lyDetailProduct.tvProdName.setText(mEProdName);
                    }
                    else {
                        mAProdName = tempKarakter;
                        mBinding.lyDetailProduct.tvProdName.setText(mAProdName);
                    }
                }

                else if(isPrice){

                    MataUangHelper mataUangHelper = new MataUangHelper();
                    if(isEditProd){
                        try {
                            mEProdPrice = Integer.parseInt(tempNumber);
                            mBinding.lyDetailProduct.tvProdPrice.setText(mataUangHelper.formatRupiah(mEProdPrice));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            mAProdPrice = Integer.parseInt(tempNumber);
                            mBinding.lyDetailProduct.tvProdPrice.setText(mataUangHelper.formatRupiah(mAProdPrice));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                }

                try {
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        imm = (InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE));
                    }
                    assert imm != null;
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mBinding.lyDoneEditText.lyDialogEditText.setVisibility(GONE);
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);
                isDialogText = false;
                isID = false;
                isName =false;
                isPrice = false;
            }
        });
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
    public void showAllProduct(final ProductModel productModel) {

        productModelGlobal = productModel;
        productAdapter = new ProductAdapter(ProductActivity.this, productModelGlobal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductActivity.this);
        mBinding.rvProduct.setLayoutManager(layoutManager);
        mBinding.rvProduct.setAdapter(productAdapter);
        mBinding.lyBlack.lyBlack.setVisibility(GONE);

        productAdapter.setOnItemClickListener(new ProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int pos, View v) {

                productPresenter.setOnClickProduct(productModel, pos);
            }
        });
    }

    @Override
    public void showDetailProduct(ProductModel.ProductSatuan productSatuan, int pos) {

        isEditProd = true;
        isAddProd = false;
        posDetail = pos;
        productSatuanGlobal = productSatuan;
        mEProdID = productSatuan.getProductID();
        mEProdName = productSatuan.getProductName();
        mEProdCat = productSatuan.getProductGeneralCategory();
        mEProdResep = productSatuan.getProductResepID();
        productPresenter.countHPP(mEProdResep);

        mBinding.lyDetailProduct.tvTitle.setText("Detail Product");
        MataUangHelper mataUangHelper = new MataUangHelper();
        mEProdPrice = productSatuan.getProductPrice();

        mBinding.lyDetailProduct.tvProdId.setText(mEProdID);
        mBinding.lyDetailProduct.tvProdName.setText(mEProdName);
        mBinding.lyDetailProduct.tvProdCategory.setText(mEProdCat);
        mBinding.lyDetailProduct.tvProdResep.setText(mEProdResep);
        mBinding.lyDetailProduct.tvProdPrice.setText(mataUangHelper.formatRupiah(mEProdPrice));

        mBinding.lyBlack.lyBlack.setVisibility(View.VISIBLE);
        mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);

    }

    @Override
    public void showSuccessEdit(String message, final ProductModel.ProductSatuan productSatuan, final int pos) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                productModelGlobal.getProductSatuanList().remove(pos);
                productModelGlobal.getProductSatuanList().add(pos,productSatuan);
                productAdapter.resetListProduct(productModelGlobal);
                productAdapter.notifyDataSetChanged();
                isEditProd = false;
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void showSuccessDelete(String message, final int pos) {

        isEditProd = false;
        isAddProd = false;

        final SweetAlertDialog pDialog = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                productModelGlobal.getProductSatuanList().remove(pos);
                productAdapter.resetListProduct(productModelGlobal);
                productAdapter.notifyDataSetChanged();

                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });


    }

    @Override
    public void showResepList(ResepModel resepModel, int pos) {

        isDialogCustome = true;

        resepForList = resepModel;
        setCustomeList(1);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (resepModel.getResepModelSatuanList().size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        if(isEditProd || isAddProd){
            mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
        }
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHpp(long hpp) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        mBinding.lyDetailProduct.tvProdHpp.setText(mataUangHelper.formatRupiah(hpp));

    }

    @Override
    public void showSuccessAdd(String message, final ProductModel.ProductSatuan productSatuan, final int sizeArray) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("Yes");
        pDialog.showCancelButton(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                mAProdID = "";
                mAProdName = "";
                mAProdCat = "";
                mAProdResep = "";
                mAProdPrice = 0;
                productModelGlobal.getProductSatuanList().add(sizeArray,productSatuan);
                productAdapter.resetListProduct(productModelGlobal);
                productAdapter.notifyDataSetChanged();
                isEditProd = false;
                mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
                mBinding.lyBlack.lyBlack.setVisibility(GONE);
                pDialog.dismiss();
            }
        });
    }

    @Override
    public void onFailed(String message) {

        showError(message);
    }

    @Override
    public void showCatList(List<String> generalCat, int pos) {

        isDialogCustome = true;

        catForList = generalCat;
        setCustomeList(2);

        if (pos != -1) {
            listCustomAdapter.selectedPosition = pos;
            listCustomAdapter.notifyDataSetChanged();

            if (generalCat.size() > 7) {
                mBinding.lyDialogCustomeList.rvCustomList.scrollToPosition(pos);
            }
        }

        if(isEditProd || isAddProd){
            mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(GONE);
        }

        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    private void setCustomeList(final int tipe) {

        if (tipe == 1) {
            listCustomAdapter = new ListCustomAdapter(ProductActivity.this, resepForList, 8);
        } else if (tipe == 2) {
            listCustomAdapter = new ListCustomAdapter(ProductActivity.this, catForList, 7);
        }
        listCustomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (tipe == 1) {

                    if(isEditProd){

                        mEProdResep = resepForList.getResepModelSatuanList().get(position).getResepID();
                        mBinding.lyDetailProduct.tvProdResep.setText(mEProdResep);
                        productPresenter.countHPP(mEProdResep);
                    }
                    else {

                        mAProdResep = resepForList.getResepModelSatuanList().get(position).getResepID();
                        mBinding.lyDetailProduct.tvProdResep.setText(mAProdResep);
                        productPresenter.countHPP(mAProdResep);
                    }

                } else if (tipe == 2) {
                    if(isEditProd){

                        mEProdCat = catForList.get(position);
                        mBinding.lyDetailProduct.tvProdCategory.setText(mEProdCat);
                    }
                    else if(isAddProd){

                        mAProdCat = catForList.get(position);
                        mBinding.lyDetailProduct.tvProdCategory.setText(mAProdCat);
                    }
                    else {
                        if (!mCategoryFilter.equalsIgnoreCase(catForList.get(position))) {
                            mCategoryFilter = catForList.get(position);
                            mBinding.lyHeaderData.tvDate.setText(mCategoryFilter);
                            productPresenter.getAllProduct(mCategoryFilter);
                        }
                    }
                }

                isDialogCustome = false;
                if(isEditProd || isAddProd){

                    mBinding.lyDetailProduct.lyDialogDetailProduct.setVisibility(View.VISIBLE);
                }
                mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(GONE);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProductActivity.this);
        mBinding.lyDialogCustomeList.rvCustomList.setLayoutManager(mLayoutManager);
        mBinding.lyDialogCustomeList.rvCustomList.setItemAnimator(new DefaultItemAnimator());
        mBinding.lyDialogCustomeList.rvCustomList.setAdapter(listCustomAdapter);
        mBinding.lyDialogCustomeList.lyDialogLayout.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.ERROR_TYPE);
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
