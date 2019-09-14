package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentMethodeModel {

    @SerializedName("tipe_pembayaran_list")
    private List<PaymentMethodeSatuan> paymentMethodeSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public PaymentMethodeModel() {
    }

    public PaymentMethodeModel(List<PaymentMethodeSatuan> paymentMethodeSatuanList, String errorMessage) {
        this.paymentMethodeSatuanList = paymentMethodeSatuanList;
        this.errorMessage = errorMessage;
    }

    public List<PaymentMethodeSatuan> getPaymentMethodeSatuanList() {
        return paymentMethodeSatuanList;
    }

    public void setPaymentMethodeSatuanList(List<PaymentMethodeSatuan> paymentMethodeSatuanList) {
        this.paymentMethodeSatuanList = paymentMethodeSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public class PaymentMethodeSatuan{

        @SerializedName("tipe_pembayaran_id")
        private String paymentMethodeID;

        @SerializedName("tipe_pembayaraan")
        private String paymentMethode;

        public PaymentMethodeSatuan() {
        }

        public PaymentMethodeSatuan(String paymentMethodeID, String paymentMethode) {
            this.paymentMethodeID = paymentMethodeID;
            this.paymentMethode = paymentMethode;
        }

        public String getPaymentMethodeID() {
            return paymentMethodeID;
        }

        public void setPaymentMethodeID(String paymentMethodeID) {
            this.paymentMethodeID = paymentMethodeID;
        }

        public String getPaymentMethode() {
            return paymentMethode;
        }

        public void setPaymentMethode(String paymentMethode) {
            this.paymentMethode = paymentMethode;
        }
    }
}
