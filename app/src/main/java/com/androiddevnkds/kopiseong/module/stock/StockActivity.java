package com.androiddevnkds.kopiseong.module.stock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ActivityStockBinding;
import com.androiddevnkds.kopiseong.module.stock.stockWareHouse.StockWareHouseFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;

public class StockActivity extends BaseActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityStockBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock);


        initUI();
        initEvent();
    }


    @Override
    public void initUI() {


        if(getIntent().getStringExtra(K.KEY_STOCK).equalsIgnoreCase(K.VALUE_KEY_STOCK_WAREHOUSE)) {
            Bundle bundle = new Bundle();
            bundle.putString(K.KEY_MAIN_FIRST_TIME, "Stock");
            FragmentHelper.fragmentInitializer(R.id.fl_fragment_container, fm, new StockWareHouseFragment(), bundle);
        }

    }

    @Override
    public void initEvent() {

    }



    //end
}
