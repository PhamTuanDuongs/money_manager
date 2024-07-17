package com.example.money_manager.common;

public class Constants {
    public enum CategoryType {
        Income(0),
        Expense(1);

        public final int value;

        private CategoryType(int value) {
            this.value = value;
        }
    }
}