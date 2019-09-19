package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.ResepItemModel;
import com.androiddevnkds.kopiseong.model.ResepModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

import java.util.List;

public class ResepItemAdapter extends RecyclerView.Adapter<ResepItemAdapter.ResepItemViewHolder> {

    private List<ResepItemModel> resepItemModelList;
    private Context context;
    private static ClickListener clickListener;

    public ResepItemAdapter(Context context, List<ResepItemModel> resepItemModelList){
        this.context= context;
        this.resepItemModelList = resepItemModelList;
    }

    @NonNull
    @Override
    public ResepItemAdapter.ResepItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.resep_item_satuan, viewGroup, false);

        return new ResepItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ResepItemAdapter.ResepItemViewHolder holder, int i) {

        holder.textViewItem.setText(resepItemModelList.get(i).getItemName());
        holder.textViewJumlah.setText(resepItemModelList.get(i).getItemJumlah());
    }

    @Override
    public int getItemCount() {
        return resepItemModelList.size();
    }

    public class ResepItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView textViewItem,textViewJumlah;

        public ResepItemViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            textViewItem = (TextView) view.findViewById(R.id.tv_item_satuan);
            textViewJumlah = (TextView) view.findViewById(R.id.tv_item_jumlah_satuan);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            Log.e("adapter", "ke click ga");
        }
    }

    public void setOnItemClickListener(ResepItemAdapter.ClickListener clickListener) {
        ResepItemAdapter.clickListener = clickListener;
        Log.e("adapter", "ke click ga");

    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resetList(List<ResepItemModel> resepItemModelList){
        this.resepItemModelList = resepItemModelList;
    }
}
