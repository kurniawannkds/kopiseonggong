package com.androiddevnkds.kopiseong.data.dataUser;

import com.androiddevnkds.kopiseong.data.CacheContract;
import com.androiddevnkds.kopiseong.utils.K;
import com.orhanobut.hawk.Hawk;

public class UserStorage implements CacheContract {

    @Override
    public boolean isCacheValid(String key) {
        return false;
    }


    //object user
    public void setUserInfoToStorage(String val) {
        Hawk.put(K.KEY_OBJECT_USER, val);
    }

    public String getUserInfoFromStorage() {
        return Hawk.get(K.KEY_OBJECT_USER);
    }

    public void removeUserInfoFromStorage() {
        Hawk.delete(K.KEY_OBJECT_USER);
    }

    //status login
    public void setUserStatusToStorage(boolean val) {
        Hawk.put(K.KEY_STATUS_USER_LOGIN, val);
    }

    public boolean getUserStatusFromStorage() {
        return Hawk.get(K.KEY_STATUS_USER_LOGIN,false);
    }

    public void removeUserStatusFromStorage() {
        Hawk.delete(K.KEY_STATUS_USER_LOGIN);
    }
}
