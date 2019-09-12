package com.androiddevnkds.kopiseong.module.register.model;

import com.google.gson.annotations.SerializedName;

public class RegisterInteractor {

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("user_pass")
    private String userPass;

    private String userPassConfirmation;

    @SerializedName("name_user")
    private String nameUser;

    @SerializedName("user_branch")
    private String userBranch;

    @SerializedName("user_role")
    private String userRole;

    @SerializedName("user_membership")
    private String userMembership;

    @SerializedName("error_msg")
    private String errorMessage;

    public RegisterInteractor(String userEmail, String userPass, String userPassConfirmation, String nameUser, String userBranch, String userRole, String userMembership, String errorMessage) {
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userPassConfirmation = userPassConfirmation;
        this.nameUser = nameUser;
        this.userBranch = userBranch;
        this.userRole = userRole;
        this.userMembership = userMembership;
        this.errorMessage = errorMessage;
    }

    public String getUserPassConfirmation() {
        return userPassConfirmation;
    }

    public void setUserPassConfirmation(String userPassConfirmation) {
        this.userPassConfirmation = userPassConfirmation;
    }

    public String getUserBranch() {
        return userBranch;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }

    public String getUserMembership() {
        return userMembership;
    }

    public void setUserMembership(String userMembership) {
        this.userMembership = userMembership;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RegisterInteractor() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
