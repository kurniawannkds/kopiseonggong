package com.androiddevnkds.kopiseong.module.summary;

import com.androiddevnkds.kopiseong.model.JurnalModel;

import java.util.List;

public interface JurnalContract {

    interface jurnalView{

        void showProgressBar();

        void hideProgressBar();

        void onFailed(String message);

        void showAllJurnalPerMonth(JurnalModel jurnalModel);

        void showMonthFirstTime(String date, List<String> listDate);

        void showDateList(List<String> dateList, int pos);

        void showResultConvert(String date);

        void showResultDateString(String date);
    }

    interface jurnalPresenter{

        void getAllJurnalPerMonth(String month);

        void getMonth();

        void getListDate(List<String> dateList, String date);

        void convertDateFromStringToNumber(String dateString, String dateStrip);

        void convertDateFromNumberToString(String dateNumber);
    }
}
