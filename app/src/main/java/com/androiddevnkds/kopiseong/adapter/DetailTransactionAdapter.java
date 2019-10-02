package com.androiddevnkds.kopiseong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;
import com.google.gson.Gson;

public class DetailTransactionAdapter extends RecyclerView.Adapter<DetailTransactionAdapter.TransactionViewHolder> {

    private DetailTransactionModel transactionModel;
    private Context context;
    private int tipe = 0;
    private static DetailTransactionAdapter.ClickListener clickListener;

    public DetailTransactionAdapter(Context context, DetailTransactionModel transactionModel, int tipe){
        this.context= context;
        this.transactionModel = transactionModel;

        this.tipe = tipe;
    }

    @NonNull
    @Override
    public DetailTransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DetailTransactionAdapter.TransactionViewHolder holder, int i) {


        Log.e("Adapter", tipe+"");

        if(tipe==0){
            holder.textViewProduct.setText(transactionModel.getDetailTransactionList().get(i).getDetailProductID());
        }
        else {
            holder.textViewProduct.setText(transactionModel.getDetailTransactionList().get(i).getProductName());
        }
        holder.textViewQuantity.setText(transactionModel.getDetailTransactionList().get(i).getDetailJumlah()+" Unit");
    }

    @Override
    public int getItemCount() {
        if(transactionModel.getDetailTransactionList()!=null) {
            return transactionModel.getDetailTransactionList().size();
        }
        else {
            return 0;
        }
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textViewProduct, textViewTotal, textViewQuantity;

        public TransactionViewHolder(View view){
            super(view);
            itemView.setOnClickListener(this);
            textViewProduct = (TextView) view.findViewById(R.id.tv_product);
            textViewTotal = (TextView) view.findViewById(R.id.tv_nominal);
            textViewQuantity = (TextView) view.findViewById(R.id.tv_date);

        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DetailTransactionAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setNewItemList(DetailTransactionModel detailTransactionModel){
        this.transactionModel = detailTransactionModel;
        Log.e("Adapter",new Gson().toJson(this.transactionModel));
    }


}
