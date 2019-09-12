package com.androiddevnkds.kopiseong.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.model.ListModel;

import java.util.List;

public class PilihProdukPemrekAdapter extends PagerAdapter {

    Context mContext;
    List<ListModel> listProduk;
    private OnClick onClick, onClickInfo;
    private ImageView imageView1, imageView2, imageView3;

    public PilihProdukPemrekAdapter(Context mContext, List<ListModel> listProduk) {
        Log.e("PilihAdapterIMG", listProduk + "");
        this.mContext = mContext;
        this.listProduk = listProduk;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getCount() {
        return listProduk.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        Log.e("PilihAdapterIMG", position + "");

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_piliih_produk, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_jenis_produk);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(position);
            }
        });
        if (position == 0) {

            imageView1 = imageView;
        }
        else if(position==1){
            imageView2 = imageView;
        }

        else if(position==3){
            imageView3 = imageView;
        }

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView price = (TextView) view.findViewById(R.id.tv_price);

        imageView.setImageResource(listProduk.get(position).getImage());
        title.setText(listProduk.get(position).getTitle());
        price.setText(listProduk.get(position).getPrice());

        container.addView(view);

        return view;
    }

    public interface OnClick {
        void onClick(int position);
    }

    public void setImageView1(boolean action) {
        imageView1.setEnabled(action);
    }

    public void setImageView2(boolean action) {
        imageView2.setEnabled(action);
    }

    public void setImageView3(boolean action) {
        imageView3.setEnabled(action);
    }
}