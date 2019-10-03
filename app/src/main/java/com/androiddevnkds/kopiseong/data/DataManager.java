package com.androiddevnkds.kopiseong.data;

import com.androiddevnkds.kopiseong.data.dataApps.AppsStorage;
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
    private static AppsStorage sAppsStorage = new AppsStorage();




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

    @Override
    public String getTanggal() {

        return sAppsStorage.getTanggal();
    }

    @Override
    public void setTanggal(String val) {
        sAppsStorage.setTanggal(val);
    }

    @Override
    public void removeTanggal() {
        sAppsStorage.removeTanggal();
    }
}