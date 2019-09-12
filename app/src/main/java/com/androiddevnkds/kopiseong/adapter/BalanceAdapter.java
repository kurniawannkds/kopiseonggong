package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.TotalBalanceModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.TransactionViewHolder> {

    private TotalBalanceModel totalBalanceModel;
    private Context context;
    private static ClickListener clickListener;

    public BalanceAdapter(Context context, TotalBalanceModel totalBalanceModel){
        this.context= context;
        this.totalBalanceModel = totalBalanceModel;
    }

    @NonNull
    @Override
    public BalanceAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BalanceAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String uang = mataUangHelper.formatRupiah(totalBalanceModel.getTotalBalanceSatuanList().get(i).getTotalBalancePemasukan());
        holder.textViewProduct.setText("Total Income : "+uang);
        uang = mataUangHelper.formatRupiah(totalBalanceModel.getTotalBalanceSatuanList().get(i).getTotalBalancePengeluaran());
        holder.textViewTotal.setText("Total Expense : "+uang);
        String date = totalBalanceModel.getTotalBalanceSatuanList().get(i).getTotalBalanceID()+"";
        date = date.substring(5,6)+"-"+date.substring(0,4);

        holder.textViewDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return totalBalanceModel.getTotalBalanceSatuanList().size();
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
        BalanceAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
