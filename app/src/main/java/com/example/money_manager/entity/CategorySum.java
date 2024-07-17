package com.example.money_manager.entity;

public class CategorySum {
    private String name;
    private String icon;
    private String percent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPercent() {
        return percent;
    }

    public float totalAmount;
    public void setPercent(String percent) {
        this.percent = percent;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CategorySum() {
    }

    public CategorySum(String name, String icon, String percent) {
        this.name = name;
        this.icon = icon;
        this.percent = percent;
    }
}
