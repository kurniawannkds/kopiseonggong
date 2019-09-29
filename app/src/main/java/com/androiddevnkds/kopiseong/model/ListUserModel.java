package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserModel {

    @SerializedName("user_list")
    private List<UserInfoModel> userInfoModelList;

    @SerializedName("error_msg")
    private String errorMessage;

    public ListUserModel() {
    }

    public ListUserModel(List<UserInfoModel> userInfoModelList, String errorMessage) {
        this.userInfoModelList = userInfoModelList;
        this.errorMessage = errorMessage;
    }

    public List<UserInfoModel> getUserInfoModelList() {
        return userInfoModelList;
    }

    public void setUserInfoModelList(List<UserInfoModel> userInfoModelList) {
        this.userInfoModelList = userInfoModelList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
