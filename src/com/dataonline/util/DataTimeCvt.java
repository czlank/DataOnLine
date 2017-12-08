package com.dataonline.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTimeCvt {
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);

        return str;
    }

    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = format.parse(str);
        } catch (ParseException e) {

        }

        return date;
    }
    
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);

        return str;
    }
}
