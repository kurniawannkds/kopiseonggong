package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class HPPModel {

    @SerializedName("hpp")
    private long hpp;

    @SerializedName("error_msg")
    private String errorMessage;

    public HPPModel() {
    }

    public HPPModel(long hpp, String errorMessage) {
        this.hpp = hpp;
        this.errorMessage = errorMessage;
    }

    public long getHpp() {
        return hpp;
    }

    public void setHpp(long hpp) {
        this.hpp = hpp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
