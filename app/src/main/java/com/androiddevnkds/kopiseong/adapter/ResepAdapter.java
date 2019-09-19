package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepviewHolder> {

    private ResepModel resepModel;
    private Context context;
    private static ClickListener clickListener;

    public ResepAdapter(Context context, ResepModel resepModel){
        this.context= context;
        this.resepModel = resepModel;
    }

    @NonNull
    @Override
    public ResepAdapter.ResepviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new ResepviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ResepAdapter.ResepviewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String[] splitJumlah = resepModel.getResepModelSatuanList().get(i).getResepJumlahItem().trim().split(",");
        String[] splitItem = resepModel.getResepModelSatuanList().get(i).getResepItem().trim().split(",");
        String hasilResep = "";
        for(int j = 0 ;j<splitJumlah.length;j++){

            splitItem[j] = splitItem[j]+"("+splitJumlah[j]+")";
            if(j==splitJumlah.length-1){
                hasilResep = hasilResep + splitItem[j];
            }
            else {
                hasilResep = hasilResep + splitItem[j] + ", ";
            }
        }
        String uang = mataUangHelper.formatRupiah(resepModel.getResepModelSatuanList().get(i).getResepTotalPrice());
        holder.textViewProduct.setText(resepModel.getResepModelSatuanList().get(i).getResepID());
        holder.textViewTotal.setText(uang);
        holder.textViewDate.setText(hasilResep);
    }

    @Override
    public int getItemCount() {
        return resepModel.getResepModelSatuanList().size();
    }

    public class ResepviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textViewProduct, textViewTotal, textViewDate;

        public ResepviewHolder(View view){
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
        ResepAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resepResepAll(ResepModel resepModel){
        this.resepModel = resepModel;
    }
}
