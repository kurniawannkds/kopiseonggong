package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRoleModel {

    @SerializedName("role_list")
    private List<UserRoleSatuan> userRoleSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public UserRoleModel() {
    }

    public UserRoleModel(List<UserRoleSatuan> userRoleSatuanList, String errorMessage) {
        this.userRoleSatuanList = userRoleSatuanList;
        this.errorMessage = errorMessage;
    }

    public List<UserRoleSatuan> getUserRoleSatuanList() {
        return userRoleSatuanList;
    }

    public void setUserRoleSatuanList(List<UserRoleSatuan> userRoleSatuanList) {
        this.userRoleSatuanList = userRoleSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class UserRoleSatuan{

        @SerializedName("user_role")
        private String userRole;

        @SerializedName("user_role_duty")
        private String userDuty;

        public UserRoleSatuan() {
        }

        public UserRoleSatuan(String userRole, String userDuty) {
            this.userRole = userRole;
            this.userDuty = userDuty;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public String getUserDuty() {
            return userDuty;
        }

        public void setUserDuty(String userDuty) {
            this.userDuty = userDuty;
        }

        @Override
        public String toString() {
            return userRole;
        }
    }
}
