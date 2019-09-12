package com.androiddevnkds.kopiseong.module.login.model;

public class LoginCredential {

    private String userName;
    private String userPassword;

    public LoginCredential(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public LoginCredential() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
