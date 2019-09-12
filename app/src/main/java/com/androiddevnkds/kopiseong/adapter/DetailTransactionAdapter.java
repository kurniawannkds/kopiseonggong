package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.DetailTransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class DetailTransactionAdapter extends RecyclerView.Adapter<DetailTransactionAdapter.TransactionViewHolder> {

    private DetailTransactionModel transactionModel;
    private Context context;

    public DetailTransactionAdapter(Context context, DetailTransactionModel transactionModel){
        this.context= context;
        this.transactionModel = transactionModel;
    }

    @NonNull
    @Override
    public DetailTransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DetailTransactionAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
//        String uang = mataUangHelper.formatRupiah(transactionModel.getDetailTransactionList().get(i).getPrice());
//        holder.textViewProduct.setText(transactionModel.getDetailTransactionList().get(i).getProductName());
//        holder.textViewTotal.setText(uang+ "/Unit");
//        holder.textViewQuantity.setText(transactionModel.getDetailTransactionList().get(i).getQuantity()+" Unit");
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

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewProduct, textViewTotal, textViewQuantity;

        public TransactionViewHolder(View view){
            super(view);

            textViewProduct = (TextView) view.findViewById(R.id.tv_product);
            textViewTotal = (TextView) view.findViewById(R.id.tv_nominal);
            textViewQuantity = (TextView) view.findViewById(R.id.tv_date);

        }

    }

    public void setNewItemList(DetailTransactionModel detailTransactionModel){
        this.transactionModel = detailTransactionModel;
    }


}
