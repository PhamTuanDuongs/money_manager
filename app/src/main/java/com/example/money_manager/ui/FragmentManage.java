package com.example.money_manager.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentManage extends FragmentPagerAdapter {
    private int tabno;

    public FragmentManage(@NonNull androidx.fragment.app.FragmentManager fm, int behavior, int tabno) {
        super(fm, behavior);
        this.tabno = tabno;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:return new ExpenseListByWeekFragment();
            case 1:return new ExpenseListByMonthFragment();
            case 2:return new ExpenseListByYearFragment();
            default:return  null;
        }
    }

    @Override
    public int getCount() {
        return tabno;
    }
}