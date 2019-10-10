package com.androiddevnkds.kopiseong.module.summary;

import android.util.Log;

import com.androiddevnkds.kopiseong.model.JurnalModel;
import com.androiddevnkds.kopiseong.utils.DateAndTime;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JurnalPresenter implements  JurnalContract.jurnalPresenter {

    private JurnalContract.jurnalView jurnalView;
    private List<String> dateListGlobal;
    private String dateGlobal = "";
    private int pos, sizeArray = 0;

    public JurnalPresenter(JurnalContract.jurnalView jurnalView){
        this.jurnalView = jurnalView;
    }
    @Override
    public void getAllJurnalPerMonth(String month) {

        jurnalView.showProgressBar();
        AndroidNetworking.post(K.URL_GET_ALL_JURNAL)
                .addBodyParameter("select", "select")
                .addBodyParameter("month", month)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(JurnalModel.class, new ParsedRequestListener<JurnalModel>() {
                    @Override
                    public void onResponse(JurnalModel jurnalModel) {
                        // do anything with response
                        Log.e("BASE",new Gson().toJson(jurnalModel));
                        if(jurnalModel.getErrorMessage()!=null){
                            onFailed(jurnalModel.getErrorMessage());
                        }
                        else {

                            jurnalView.hideProgressBar();
                            jurnalView.showAllJurnalPerMonth(jurnalModel);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        onFailed("ERROR");
                    }
                });
    }

    @Override
    public void getMonth() {

        DateAndTime dateAndTime = new DateAndTime();
        String date = dateAndTime.getCurrentDate(K.FORMAT_TANGGAL_STRING).substring(3, 10);
        List<String> dateList = new ArrayList<>();
        dateList.add(K.KEY_ALL_STRING);
        dateList.add(K.KEY_BULAN_JAN);
        dateList.add(K.KEY_BULAN_FEB);
        dateList.add(K.KEY_BULAN_MAR);
        dateList.add(K.KEY_BULAN_APR);
        dateList.add(K.KEY_BULAN_MAY);
        dateList.add(K.KEY_BULAN_JUN);
        dateList.add(K.KEY_BULAN_JUL);
        dateList.add(K.KEY_BULAN_AUG);
        dateList.add(K.KEY_BULAN_SEP);
        dateList.add(K.KEY_BULAN_OCT);
        dateList.add(K.KEY_BULAN_NOV);
        dateList.add(K.KEY_BULAN_DEC);

        jurnalView.showMonthFirstTime(date,dateList);

    }

    @Override
    public void getListDate(List<String> dateList, String date) {

        jurnalView.showProgressBar();
        dateListGlobal = dateList;
        dateGlobal = date;
        setCustomeList(1);

    }

    @Override
    public void convertDateFromStringToNumber(String dateString, String dateStrip) {
        String[] splited = dateStrip.trim().split("-");
        String dateResult = "";
        if(dateString.equalsIgnoreCase(K.KEY_BULAN_JAN)){
            dateResult = "01-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_FEB)){
            dateResult = "02-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_MAR)){
            dateResult = "03-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_APR)){

            dateResult = "04-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_MAY)){

            dateResult = "05-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_JUN)){
            dateResult = "06-"+splited[1];

        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_JUL)){
            dateResult = "07-"+splited[1];

        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_AUG)){

            dateResult = "08-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_SEP)){

            dateResult = "09-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_OCT)){

            dateResult = "10-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_NOV)){

            dateResult = "11-"+splited[1];
        }
        else if(dateString.equalsIgnoreCase(K.KEY_BULAN_DEC)){

            dateResult = "12-"+splited[1];
        }

        jurnalView.showResultConvert(dateResult);
    }

    @Override
    public void convertDateFromNumberToString(String dateNumber) {

        String[] splited = dateNumber.trim().split("-");
        String dateResult = "";

        if(splited[0].equalsIgnoreCase("01")){
            dateResult = K.KEY_BULAN_JAN;
        }
        else if(splited[0].equalsIgnoreCase("02")){
            dateResult = K.KEY_BULAN_FEB;
        }
        else if(splited[0].equalsIgnoreCase("03")){
            dateResult = K.KEY_BULAN_MAR;
        }
        else if(splited[0].equalsIgnoreCase("04")){
            dateResult = K.KEY_BULAN_APR;
        }
        else if(splited[0].equalsIgnoreCase("05")){
            dateResult = K.KEY_BULAN_MAY;
        }
        else if(splited[0].equalsIgnoreCase("06")){
            dateResult = K.KEY_BULAN_JUN;
        }
        else if(splited[0].equalsIgnoreCase("07")){
            dateResult = K.KEY_BULAN_JUL;
        }
        else if(splited[0].equalsIgnoreCase("08")){
            dateResult = K.KEY_BULAN_AUG;
        }
        else if(splited[0].equalsIgnoreCase("09")){
            dateResult = K.KEY_BULAN_SEP;
        }
        else if(splited[0].equalsIgnoreCase("10")){
            dateResult = K.KEY_BULAN_OCT;
        }
        else if(splited[0].equalsIgnoreCase("11")){
            dateResult = K.KEY_BULAN_NOV;
        }
        else if(splited[0].equalsIgnoreCase("12")){
            dateResult = K.KEY_BULAN_DEC;
        }

        jurnalView.showResultDateString(dateResult);
    }

    private void onFailed(String message){
        jurnalView.hideProgressBar();
        jurnalView.onFailed(message);
    }

    private void setCustomeList(int tipe){

        if(tipe==1) {
            if (dateListGlobal != null) {
                sizeArray = dateListGlobal.size();
                if (sizeArray > 0) {

                    pos = findPosition(4);
                    jurnalView.hideProgressBar();
                    jurnalView.showDateList(dateListGlobal, pos);
                }
            }
        }
    }

    private int findPosition(int tipe) {

        int tempPosition = -1;

        if(tipe==1){

            if (!dateGlobal.equalsIgnoreCase("")) {
                //category

                for (int i = 0; i < dateListGlobal.size(); i++) {
                    if (dateListGlobal.get(i).equalsIgnoreCase(dateGlobal)) {
                        tempPosition = i;
                        break;
                    }
                }
            }
        }

        return tempPosition;
    }


}
