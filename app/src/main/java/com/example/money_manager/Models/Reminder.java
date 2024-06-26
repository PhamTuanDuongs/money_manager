package com.example.money_manager.Models;

public class Reminder {
    private String title;
    private String description;

    public Reminder(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
