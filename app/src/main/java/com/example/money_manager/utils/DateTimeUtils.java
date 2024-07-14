package com.example.money_manager.utils;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public final class DateTimeUtils {
    private DateTimeUtils() {}
    public static LocalDate getCurrentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            return date;
        }
        return null;
    }

    public static String getCurrentHour() {
        String result = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int hour = LocalDateTime.now().getHour();
            int minute = LocalDateTime.now().getMinute();
            result = hour + ":" + minute;
        }
        return result;
    }

    public static String convertTimestampToDate(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datePart = dateFormat.format(timestamp.toDate());
        return datePart;
    }
    public static String convertTimestampToTime(Timestamp timestamp) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timePart = timeFormat.format(timestamp.toDate());
        return timePart;
    }

    public  static  Date parseDate(String strDate, String pattern) {
        Date result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            result = dateFormat.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
