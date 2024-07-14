package com.example.money_manager.entity;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;

public class Reminder {
    private String id;
    private String name;
    private String frequency;
    private Timestamp datetime;
    private String comment;

    public Reminder(String name, String frequency, String date, String time, String comment, String account) {

    }
    private Account account;

    public Reminder() {}
    public Reminder(String name, String frequency, Timestamp datetime, String comment, Account account) {
        this.name = name;
        this.frequency = frequency;
        this.datetime = datetime;
        this.comment = comment;
        this.account = account;
    }

    public Reminder(String id, String name, String frequency, Timestamp datetime, String comment, Account account) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.datetime = datetime;
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

    public Timestamp getDateTime() {
        return datetime;
    }

    public void setDateTime(Timestamp date) {
        this.datetime = date;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
