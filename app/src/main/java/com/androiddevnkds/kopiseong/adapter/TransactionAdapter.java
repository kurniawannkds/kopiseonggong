package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private TransactionModel transactionModel;
    private Context context;
    private static ClickListener clickListener;

    public TransactionAdapter(Context context, TransactionModel transactionModel){
        this.context= context;
        this.transactionModel = transactionModel;
    }

    @NonNull
    @Override
    public TransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String uang = mataUangHelper.formatRupiah(transactionModel.getTransactionModelLists().get(i).getTransactionBalance());
        holder.textViewProduct.setText(transactionModel.getTransactionModelLists().get(i).getTransactionCategory());
        holder.textViewTotal.setText(uang);
        holder.textViewDate.setText(transactionModel.getTransactionModelLists().get(i).getTransactionDate());
    }

    @Override
    public int getItemCount() {
        if(transactionModel!=null && transactionModel.getTransactionModelLists()!=null
                && transactionModel.getTransactionModelLists().size()>0){
            return transactionModel.getTransactionModelLists().size();
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
        TransactionAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resetListTransaction(TransactionModel transactionModel){
        this.transactionModel = transactionModel;
    }
}
