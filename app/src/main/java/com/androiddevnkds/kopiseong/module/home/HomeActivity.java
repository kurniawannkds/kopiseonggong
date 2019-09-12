package com.androiddevnkds.kopiseong.module.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ActivityHomeBinding;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.BaseActivity;

public class HomeActivity extends BaseActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);


        initUI();
        initEvent();
    }

    @Override
    public void initUI() {

        Bundle bundle = new Bundle();
        bundle.putString(K.KEY_MAIN_FIRST_TIME, "Register");
        FragmentHelper.fragmentInitializer(R.id.fl_fragment_container, fm, new WelcomeFragment(), bundle);
    }

    @Override
    public void initEvent() {

    }



    //end
}
