package com.example.money_manager.entity;

public class Category {

    private int id;
    private String name;
    private int type;
    private String account;
    private String image;

    public Category(){

    }

    public Category(int id, String name, int type, String account, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.account = account;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
