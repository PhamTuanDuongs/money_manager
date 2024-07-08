package com.example.money_manager.entity;

public class Category {
    private int id;
    private String categoryName;
    private byte type;
    private String account;
    private int iconImageId;

    public Category(int id, String categoryName, byte type, String account, int iconImageId) {
        this.id = id;
        this.categoryName = categoryName;
        this.type = type;
        this.account = account;
        this.iconImageId = iconImageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getIconImageId() {
        return iconImageId;
    }

    public void setIconImageId(int iconImageId) {
        this.iconImageId = iconImageId;
    }
}
