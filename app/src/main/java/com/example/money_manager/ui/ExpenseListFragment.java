package com.example.money_manager.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.money_manager.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ExpenseListFragment extends Fragment {
    TabLayout tabLayout;
    TabItem week,month,year;
    ViewPager viewPager;
    FragmentManage fragmentmanager;
    ImageButton btnAddExpense;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense, container, false);
        tabLayout = v.findViewById(R.id.ctablayout);
        btnAddExpense = v.findViewById((R.id.btnAddExpense));
        week =v.findViewById((R.id.ctab1));
        month =v.findViewById((R.id.ctab2));
        year=v.findViewById((R.id.ctab3));
        viewPager=(ViewPager) v.findViewById(R.id.pageholder);
        fragmentmanager=new FragmentManage(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(fragmentmanager);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 loadFragment(new AddExpenseFragment());
                                             }
                                         });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       /* presenter.onGetListExpense();*/
//        setList();
        /*btnCreateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

    }



    /*@Override
    public void navigateToExpenseActivity() {

    }

    @Override
    public void showAddSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setListExpense(ArrayList<Transaction> transactions) {

        for (Transaction trans : transactions
        ) {
            expenses.add(trans);
        }
        expenseAdapter.notifyDataSetChanged();
        pbLoading.setVisibility(View.GONE);

    }

    @Override
    public void DeleteExpense(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
        expenseAdapter.removeItem(expensePosition);
        expenseAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddError(String title, String error) {

    }
*/

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
