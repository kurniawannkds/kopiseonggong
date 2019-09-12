package com.androiddevnkds.kopiseong.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAndTime {

    private String date;
    private String time;
    private SimpleDateFormat sdf;
    private Calendar todayDate;

    public DateAndTime() {

    }

    public String getCurrentDate(String formatTanggal) {
        todayDate = Calendar.getInstance();
        //dapatin tanggal hari ini
        sdf = new SimpleDateFormat(formatTanggal);
        date = sdf.format(todayDate.getTime());

        return date;
    }

    public String getCurrentTime(String formatWaktu) {
        todayDate = Calendar.getInstance();
        //dapatin jam
        sdf = new SimpleDateFormat(formatWaktu);
        time = sdf.format(todayDate.getTime());

        return time;
    }
}
