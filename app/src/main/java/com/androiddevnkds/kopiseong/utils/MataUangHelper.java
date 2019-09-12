package com.androiddevnkds.kopiseong.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class MataUangHelper {

    private NumberFormat formatRupiah;
    private Locale localeID = new Locale("en", "ID");

    public String formatRupiah(int nominal){
        String format = "";
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        format = formatRupiah.format(nominal).replace("IDR","IDR. ");
        return format;
    }

    public String formatRupiah(long nominal){
        String format = "";
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        format = formatRupiah.format(nominal).replace("IDR","IDR. ");
        return format;
    }
}
