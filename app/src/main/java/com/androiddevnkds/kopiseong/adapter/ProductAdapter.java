package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.ProductModel;
import com.androiddevnkds.kopiseong.model.TransactionModel;
import com.androiddevnkds.kopiseong.utils.MataUangHelper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.TransactionViewHolder> {

    private ProductModel productModel;
    private Context context;
    private static ClickListener clickListener;

    public ProductAdapter(Context context, ProductModel productModel){
        this.context= context;
        this.productModel = productModel;
    }

    @NonNull
    @Override
    public ProductAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_satuan, viewGroup, false);

        return new TransactionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.TransactionViewHolder holder, int i) {

        MataUangHelper mataUangHelper = new MataUangHelper();
        String uang = mataUangHelper.formatRupiah(productModel.getProductSatuanList().get(i).getProductPrice());
        holder.textViewProduct.setText(productModel.getProductSatuanList().get(i).getProductName());
        holder.textViewTotal.setText(uang);
        holder.textViewDate.setText(productModel.getProductSatuanList().get(i).getProductGeneralCategory());
    }

    @Override
    public int getItemCount() {
        if(productModel!=null && productModel.getProductSatuanList()!=null
                && productModel.getProductSatuanList().size()>0){
            return productModel.getProductSatuanList().size();
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
        ProductAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void resetListProduct(ProductModel productModel){
        this.productModel = productModel;
    }
}
