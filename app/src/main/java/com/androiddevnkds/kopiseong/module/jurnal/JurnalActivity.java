package com.androiddevnkds.kopiseong.module.jurnal;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.androiddevnkds.kopiseong.BaseActivity;
import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.ActivityMainBinding;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.module.login.LoginFragment;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;

public class JurnalActivity extends BaseActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        initUI();
        initEvent();
    }


    @Override
    public void initUI() {

        if(DataManager.can().getUserStatusFromStorage()){

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString(K.KEY_MAIN_FIRST_TIME, "Register");
            FragmentHelper.fragmentInitializer(R.id.fl_fragment_container, fm, new LoginFragment(), bundle);
        }

    }

    @Override
    public void initEvent() {

    }



    //end
}
