package com.example.money_manager.entity;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;

public class Reminder {
    private String id;
    private String name;
    private String frequency;
    private String date;
    private String time;
    private String comment;

    private String account;

    public  Reminder(){

    }

    public Reminder(String name, String frequency, String date, String time, String comment, String account) {
        this.name = name;
        this.frequency = frequency;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.account = account;
    }

    public Reminder(String id, String name, String frequency, String date, String time, String comment, String account) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.account = account;
    }

    public String getId(){return id;}

    public void setId(String id){this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
