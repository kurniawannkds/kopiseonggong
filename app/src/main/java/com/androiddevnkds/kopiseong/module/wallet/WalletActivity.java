package com.androiddevnkds.kopiseong.module.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ActivityGeneralBinding;
import com.androiddevnkds.kopiseong.module.resep.ResepFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;

public class WalletActivity extends BaseActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityGeneralBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_general);


        initUI();
        initEvent();
    }

    @Override
    public void initUI() {

        Bundle bundle = new Bundle();
        bundle.putString(K.KEY_MAIN_FIRST_TIME, "Register");
        FragmentHelper.fragmentInitializer(R.id.fl_fragment_container, fm, new WalletFragment(), bundle);
    }

    @Override
    public void initEvent() {

    }



    //end
}
