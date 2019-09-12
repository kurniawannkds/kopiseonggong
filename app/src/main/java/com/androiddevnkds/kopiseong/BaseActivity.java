package com.androiddevnkds.kopiseong;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.androiddevnkds.kopiseong.MyApplication;

public abstract class BaseActivity extends AppCompatActivity {

    private static FragmentManager mFragMgr;
    public MyApplication app = MyApplication.getInstance();

    /**
     * method used for first UI initialization & manipulation
     */
    public abstract void initUI();

    /**
     * method used for first event initialization & manipulation
     */
    public abstract void initEvent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragMgr = getSupportFragmentManager();
    }

}
