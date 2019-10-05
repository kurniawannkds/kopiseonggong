package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.AssetModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.TransactionViewHolder> {

    private AssetModel assetModel;
    private Context context;
    private static ClickListener clickListener;

    public AssetAdapter(Context context, AssetModel assetModel){
        this.context= context;
        this.assetModel = assetModel;
    }

    @NonNull
    @Override
    public AssetAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AssetAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String uang = mataUangHelper.formatRupiah(assetModel.getAssetModelSatuanList().get(i).getAssetPrice());
        holder.textViewProduct.setText(assetModel.getAssetModelSatuanList().get(i).getAssetName());
        holder.textViewTotal.setText(uang);
        holder.textViewDate.setText(assetModel.getAssetModelSatuanList().get(i).getAssetDate());
    }

    @Override
    public int getItemCount() {
        if(assetModel!=null && assetModel.getAssetModelSatuanList()!=null
                && assetModel.getAssetModelSatuanList().size()>0){
            return assetModel.getAssetModelSatuanList().size();
        }
        else {
            return 0;
        }

    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textViewProduct, textViewTotal, textViewDate;

        public TransactionViewHolder(View view){
            super(view);
            itemView.setOnClickListener(this);
            textViewProduct = (TextView) view.findViewById(R.id.tv_product);
            textViewTotal = (TextView) view.findViewById(R.id.tv_nominal);
            textViewDate = (TextView) view.findViewById(R.id.tv_date);

        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AssetAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resetListTransaction(AssetModel assetModel){
        this.assetModel = assetModel;
    }
}
