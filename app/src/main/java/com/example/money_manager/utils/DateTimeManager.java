package com.example.money_manager.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DateTimeManager {
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

}
