package com.example.money_manager.entity;

import com.google.firebase.Timestamp;

public class Reminder {
    private String name;
    private String frequency;
    private Timestamp datetime;
    private String comment;

    private Account account;

    private boolean isActive;

    public Reminder() {}
    public Reminder(String name, String frequency, Timestamp datetime, String comment, Account account, boolean isActive) {
        this.name = name;
        this.frequency = frequency;
        this.datetime = datetime;
        this.comment = comment;
        this.account = account;
        this.isActive = isActive;
    }

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

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
