package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

public class GeneralAddResponseModel {

    @SerializedName("error_msg")
    private String errorMessage;

    @SerializedName("success_msg")
    private String successMessage;

    public GeneralAddResponseModel() {
    }

    public GeneralAddResponseModel(String errorMessage, String successMessage) {
        this.errorMessage = errorMessage;
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
