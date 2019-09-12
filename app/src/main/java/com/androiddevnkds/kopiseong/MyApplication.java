package com.androiddevnkds.kopiseong;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

public class MyApplication extends Application {

    private static MyApplication sApp;

    public static MyApplication getInstance() {
        if (sApp == null) {
            sApp = new MyApplication();
        }

        return sApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;
        Hawk.init(getApplicationContext()).build();
    }
}
