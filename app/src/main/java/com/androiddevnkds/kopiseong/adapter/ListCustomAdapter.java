package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ItemListCustomBinding;
import com.androiddevnkds.kopiseong.model.CategoryModel;
import com.androiddevnkds.kopiseong.model.ListUserModel;
import com.androiddevnkds.kopiseong.model.PaymentMethodeModel;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.androiddevnkds.kopiseong.utils.listener.OnItemClickListener;

import java.util.List;

public class ListCustomAdapter extends RecyclerView.Adapter<ListCustomAdapter.ViewHolder> {

    private ItemListCustomBinding mBinding;
    private Context mContext;
    private int tipe = 0;
    private CategoryModel categoryModel;
    private ProductModel productModel;
    private PaymentMethodeModel paymentMethodeModel;
    private ListUserModel userInfoModelList;
    private StockModel stockModel;
    private long hargaPerGram = 0;

    public int selectedPosition = -1;
    private OnItemClickListener mItemClickListener;

    public ListCustomAdapter(Context mContext, CategoryModel categoryModel, int tipe) {
        this.mContext = mContext;
        this.categoryModel = categoryModel;
        this.tipe = tipe;
    }

    public ListCustomAdapter(Context mContext, ProductModel productModel, int tipe) {
        this.mContext = mContext;
        this.productModel = productModel;
        this.tipe = tipe;
    }

    public ListCustomAdapter(Context mContext, PaymentMethodeModel paymentMethodeModel, int tipe) {
        this.mContext = mContext;
        this.paymentMethodeModel = paymentMethodeModel;
        this.tipe = tipe;
    }

    public ListCustomAdapter(Context mContext, StockModel stockModel, int tipe) {
        this.mContext = mContext;
        this.stockModel = stockModel;
        this.tipe = tipe;
    }

    public ListCustomAdapter(Context mContext, ListUserModel userInfoModelList, int tipe) {
        this.mContext = mContext;
        this.userInfoModelList = userInfoModelList;
        this.tipe = tipe;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_list_custom, parent, false);
        return new ViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String itemShow = "";
        //category
        if(tipe == 1){
            itemShow = categoryModel.getCategorySatuanList().get(position).getCategoryName();
        }
        //user
        else if(tipe==2){
            itemShow = userInfoModelList.getUserInfoModelList().get(position).getNameUser();
        }
        //produk
        else if(tipe==3){
            itemShow = productModel.getProductSatuanList().get(position).getProductName();
        }
        //payment methode
        else if(tipe==4){
            itemShow = paymentMethodeModel.getPaymentMethodeSatuanList().get(position).getPaymentMethode();
        }
        //stock name
        else if(tipe==5){
            itemShow = stockModel.getStockSatuanModelList().get(position).getStockName();

        }
        else if(tipe==6){
            itemShow = stockModel.getStockSatuanModelList().get(position).getStockName();
            hargaPerGram = stockModel.getStockSatuanModelList().get(position).getStockPricePerGram();

        }

        if(tipe==6){
            MataUangHelper mataUangHelper = new MataUangHelper();
            holder.mBinding.tvItem.setText(itemShow + " ("+mataUangHelper.formatRupiah(hargaPerGram)+"/gr)");
        }
        else {
            holder.mBinding.tvItem.setText(itemShow);
        }


        if (selectedPosition != -1 && selectedPosition == position) {
            holder.mBinding.itemList.setSelected(true);
            holder.mBinding.itemList.setBackgroundColor(holder.view.getResources().getColor(R.color.textColorSelected));
        } else {
            holder.mBinding.itemList.setSelected(false);
            holder.mBinding.itemList.setBackgroundColor(holder.view.getResources().getColor(android.R.color.transparent));
        }

        holder.mBinding.itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);

                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
//
                selectedPosition = holder.getAdapterPosition();
//                notifyItemChanged(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(tipe==1) {
            return categoryModel.getCategorySatuanList().size();
        }
        else if(tipe==2){
            //dummy
            return userInfoModelList.getUserInfoModelList().size();
        }

        else if(tipe==3){
            //dummy
            return productModel.getProductSatuanList().size();
        }
        else if(tipe==4){
            return paymentMethodeModel.getPaymentMethodeSatuanList().size();
        }
        else {
            return stockModel.getStockSatuanModelList().size();
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemListCustomBinding mBinding;
        View view;

        public ViewHolder(View itemView, ItemListCustomBinding mBinding) {
            super(itemView);
            view = itemView;
            this.mBinding = mBinding;
        }
    }
}
