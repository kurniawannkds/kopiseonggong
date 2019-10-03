package com.androiddevnkds.kopiseong.data;

import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;

public interface DataManagerType {

    RegisterInteractor getUserInfoFromStorage();
    void setUserInfoToStorage(RegisterInteractor model);
    void removeUserInfoFromStorage();

    boolean getUserStatusFromStorage();
    void setUserStatusToStorage(boolean val);
    void removeUserStatusFromStorage();

    String getTanggal();
    void setTanggal(String val);
    void removeTanggal();
}
