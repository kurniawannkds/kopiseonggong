package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JurnalModel {

    @SerializedName("jurnal_list")
    private List<JurnalSatuan> jurnalSatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public JurnalModel() {
    }

    public JurnalModel(List<JurnalSatuan> jurnalSatuanList, String errorMessage) {
        this.jurnalSatuanList = jurnalSatuanList;
        this.errorMessage = errorMessage;
    }

    public List<JurnalSatuan> getJurnalSatuanList() {
        return jurnalSatuanList;
    }

    public void setJurnalSatuanList(List<JurnalSatuan> jurnalSatuanList) {
        this.jurnalSatuanList = jurnalSatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class JurnalSatuan{

        @SerializedName("jurnal_list_date")
        private List<JurnalDate> jurnalDateList;

        public JurnalSatuan() {
        }

        public JurnalSatuan(List<JurnalDate> jurnalDateList) {
            this.jurnalDateList = jurnalDateList;
        }

        public List<JurnalDate> getJurnalDateList() {
            return jurnalDateList;
        }

        public void setJurnalDateList(List<JurnalDate> jurnalDateList) {
            this.jurnalDateList = jurnalDateList;
        }

        public class JurnalDate{

            @SerializedName("trans_date")
            private String transDate;

            @SerializedName("trans_category")
            private String transCat;

            @SerializedName("trans_tipe_pembayaran")
            private String transTipeBayar;

            @SerializedName("total_trans_price")
            private long transPrice;

            @SerializedName("total_trans_hpp")
            private long transHpp;

            public JurnalDate() {
            }

            public JurnalDate(String transDate, String transCat, String transTipeBayar, long transPrice) {
                this.transDate = transDate;
                this.transCat = transCat;
                this.transTipeBayar = transTipeBayar;
                this.transPrice = transPrice;
            }

            public long getTransHpp() {
                return transHpp;
            }

            public void setTransHpp(long transHpp) {
                this.transHpp = transHpp;
            }

            public String getTransDate() {
                return transDate;
            }

            public void setTransDate(String transDate) {
                this.transDate = transDate;
            }

            public String getTransCat() {
                return transCat;
            }

            public void setTransCat(String transCat) {
                this.transCat = transCat;
            }

            public String getTransTipeBayar() {
                return transTipeBayar;
            }

            public void setTransTipeBayar(String transTipeBayar) {
                this.transTipeBayar = transTipeBayar;
            }

            public long getTransPrice() {
                return transPrice;
            }

            public void setTransPrice(long transPrice) {
                this.transPrice = transPrice;
            }
        }
    }
}
