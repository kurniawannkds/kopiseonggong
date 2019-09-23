package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.StockModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.TransactionViewHolder> {

    private StockModel stockModel;
    private Context context;
    private static ClickListener clickListener;

    public StockAdapter(Context context, StockModel stockModel){
        this.context= context;
        this.stockModel = stockModel;
    }

    @NonNull
    @Override
    public StockAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String uang = mataUangHelper.formatRupiah(stockModel.getStockSatuanModelList().get(i).getStockPrice());
        holder.textViewProduct.setText(stockModel.getStockSatuanModelList().get(i).getStockName());
        holder.textViewTotal.setText(uang);
        holder.textViewDate.setText(stockModel.getStockSatuanModelList().get(i).getStockDate());
    }

    @Override
    public int getItemCount() {

        if(stockModel.getStockSatuanModelList().get(stockModel.getStockSatuanModelList().size()-1).getStockID().equalsIgnoreCase(K.ADD_NEW_STOCK)){
            Log.e("ADAP","MASUK SINI");
            return stockModel.getStockSatuanModelList().size()-1;
        }
        else {
            return stockModel.getStockSatuanModelList().size();
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
        StockAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resetStock(StockModel stockModel){
        this.stockModel = stockModel;
    }
}
