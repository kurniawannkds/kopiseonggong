package com.androiddevnkds.kopiseong.model;

import android.support.annotation.NonNull;

public class ListUserRoleModel {

    private String userRole;

    public ListUserRoleModel(String userRole) {
        this.userRole = userRole;
    }

    public ListUserRoleModel() {
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public String toString() {
        return userRole;
    }
}
