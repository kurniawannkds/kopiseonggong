package com.androiddevnkds.kopiseong.module.register;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.databinding.ActivityGeneralBinding;
import com.androiddevnkds.kopiseong.model.ListUserRoleModel;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.register.RegisterFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity {

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

        FragmentHelper.fragmentInitializer(R.id.fl_fragment_container, fm, new RegisterFragment(), null);
    }

    @Override
    public void initEvent() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //end
}
