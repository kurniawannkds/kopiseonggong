package com.androiddevnkds.kopiseong.data.dataApps;

import com.androiddevnkds.kopiseong.data.CacheContract;
import com.androiddevnkds.kopiseong.utils.K;
import com.orhanobut.hawk.Hawk;

public class AppsStorage implements CacheContract {

    @Override
    public boolean isCacheValid(String key) {
        return false;
    }


    //object user
    public void setTanggal(String val) {
        Hawk.put(K.KEY_DATE, val);
    }

    public String getTanggal() {
        return Hawk.get(K.KEY_DATE);
    }

    public void removeTanggal() {
        Hawk.delete(K.KEY_DATE);
    }
}
