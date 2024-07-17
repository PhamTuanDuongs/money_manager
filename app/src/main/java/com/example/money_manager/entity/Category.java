package com.example.money_manager.entity;

public class Category {
    private String id;
    private String name;
    private int type;
    private String account;
    private String image;
    private String autoID;

    public Category() {

    }

    public Category(String id, String name, int type, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
    }


    public Category(String id, String name, int type, String account, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.account = account;
        this.image = image;
    }

    public Category(String name, int type, String account) {
        this.name = name;
        this.type = type;
        this.account = account;
    }
    public Category(String name, int type, String account, String img) {
        this.name = name;
        this.type = type;
        this.account = account;
        this.image =img;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAutoID() {
        return autoID;
    }

    public void setAutoID(String autoID) {
        this.autoID = autoID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImage() {
        return image;
    }

    public void setIconImageId(String image) {
        this.image = image;
    }
}
