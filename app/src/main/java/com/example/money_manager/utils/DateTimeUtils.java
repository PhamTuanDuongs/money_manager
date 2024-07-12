package com.example.money_manager.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

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

    public static String getCurrentWeek() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                break;
            case Calendar.MONDAY:

                break;
            case Calendar.TUESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -2);
                break;
            case Calendar.THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                break;
            case Calendar.FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -4);
                break;
            case Calendar.SATURDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -5);
                break;
        }
        Date first = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        Date last = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");

        // Format the date to a string
        String formattedDate = formatter.format(first) + " - " + formatter.format(last);
        return formattedDate;

    }

    public static String getNextWeek(String week) {
        String[] dates = week.split(" - ");
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        String nextWeek = "";

        for (String d : dates) {
            try {
                // Parse the date string to a Date object
                Date date = formatter.parse(d);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                date = calendar.getTime();
                nextWeek += formatter.format(date) + " - ";


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        return nextWeek.substring(0, nextWeek.length() - 3);
    }

    public static String getPreviousWeek(String week) {
        String[] dates = week.split(" - ");
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        String nextWeek = "";

        for (String d : dates) {
            try {
                // Parse the date string to a Date object
                Date date = formatter.parse(d);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                date = calendar.getTime();
                nextWeek += formatter.format(date) + " - ";


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        return nextWeek.substring(0, nextWeek.length() - 3);
    }

    public static String getCurrentMonth() {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");


        String formattedDate = formatter.format(currentDate);
        return formattedDate;

    }

    public static String getNextMonth(String month) {
        SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(month);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
     } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return format.format(calendar.getTime());
    }
    public static String getPreviousMonth(String month) {
        SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(month);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return format.format(calendar.getTime());
    }
    public static boolean isLeapYear(int year) {
        // Check if the year is divisible by 4 and not divisible by 100,
        // or if it is divisible by 400
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    public static String getDateMonthString(String month) {

        String date = "1 "+month+ " - ";
        if (month.substring(0,3).equals("Jan")||month.substring(0,3).equals("Mar")||month.substring(0,3).equals("May")
        ||month.substring(0,3).equals("Jul")||month.substring(0,3).equals("Aug")||month.substring(0,3).equals("Oct")||month.substring(0,3).equals("Dec")){
            date= date+ "31 "+month;
        }
        if (isLeapYear(Integer.parseInt(month.substring(4,8)))&&month.substring(0,3).equals("Feb")){
            date=date+ "29 "+month;
        }
        if ((!isLeapYear(Integer.parseInt(month.substring(4,8))))&&month.substring(0,3).equals("Feb")){
            date=date+ "28 "+ month;
        }
        if (month.substring(0,3).equals("Apr")||month.substring(0,3).equals("Jun")||month.substring(0,3).equals("Sep")
                ||month.substring(0,3).equals("Nov")){
            date= date+ "30 "+ month;
        }
        return date;
    }
    public static String getDateYearString(String year) {
        String date = "1 Jan "+year+ " - 31 Dec "+year;
        return date;
    }

}
