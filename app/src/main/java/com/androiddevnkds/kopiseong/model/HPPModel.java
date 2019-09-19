package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class HPPModel {

    @SerializedName("hpp")
    private double hpp;

    @SerializedName("error_msg")
    private String errorMessage;

    public HPPModel() {
    }

    public HPPModel(double hpp, String errorMessage) {
        this.hpp = hpp;
        this.errorMessage = errorMessage;
    }

    public double getHpp() {
        return hpp;
    }

    public void setHpp(double hpp) {
        this.hpp = hpp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
