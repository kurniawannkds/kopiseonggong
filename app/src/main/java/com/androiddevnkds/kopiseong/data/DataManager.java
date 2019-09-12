package com.androiddevnkds.kopiseong.data;

import com.androiddevnkds.kopiseong.data.dataUser.UserStorage;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.google.gson.Gson;

public class DataManager implements DataManagerType {
    private static DataManager dm;

    public static DataManager can() // or use, or call (?)
    {
        if (dm == null) {
            dm = new DataManager();
        }
        return dm;
    }

    private static UserStorage sUserStorage = new UserStorage();




    @Override
    public RegisterInteractor getUserInfoFromStorage() {
        String json = sUserStorage.getUserInfoFromStorage();
        return new Gson().fromJson(json, RegisterInteractor.class);
    }

    @Override
    public void setUserInfoToStorage(RegisterInteractor model) {
        String json = new Gson().toJson(model);
        sUserStorage.setUserInfoToStorage(json);
    }

    @Override
    public void removeUserInfoFromStorage() {
        sUserStorage.removeUserInfoFromStorage();
    }


    @Override
    public boolean getUserStatusFromStorage() {

        return sUserStorage.getUserStatusFromStorage();
    }

    @Override
    public void setUserStatusToStorage(boolean val) {
        sUserStorage.setUserStatusToStorage(val);
    }

    @Override
    public void removeUserStatusFromStorage() {
        sUserStorage.removeUserStatusFromStorage();
    }
}