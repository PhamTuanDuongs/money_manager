package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.money_manager.R;
import com.example.money_manager.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesFragment extends Fragment {

    private List<Category> defaultCategories;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpensesFragment newInstance(String param1, String param2) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        List<Category> defaultCategories = new ArrayList<>();
        defaultCategories.add(new Category(1, "Health", (byte) 1, "", R.drawable.icon_health));
        defaultCategories.add(new Category(2, "Leisure", (byte) 1, "", R.drawable.icon_leisure));
        defaultCategories.add(new Category(3, "Home", (byte) 1, "", R.drawable.icon_home));
        defaultCategories.add(new Category(4, "Food", (byte) 1, "", R.drawable.icon_food));
        defaultCategories.add(new Category(5, "Education", (byte) 1, "", R.drawable.icon_education));
        defaultCategories.add(new Category(6, "Gifts", (byte) 1, "", R.drawable.icon_gift));
        defaultCategories.add(new Category(7, "Groceries", (byte) 1, "", R.drawable.icon_groceries));
        defaultCategories.add(new Category(8, "Family", (byte) 1, "", R.drawable.icon_family));
        defaultCategories.add(new Category(9, "Workout", (byte) 1, "", R.drawable.icon_workout));
        defaultCategories.add(new Category(10, "Transportation", (byte) 1, "", R.drawable.icon_transportation));
        defaultCategories.add(new Category(11, "Other", (byte) 1, "", R.drawable.icon_other));
        defaultCategories.add(new Category(12, "Create", (byte) 1, "", R.drawable.icon_create));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Category> accountCategories = getCategoriesForCurrentAccount();
        List<Category> expenseCategories = new ArrayList<>(defaultCategories);
        expenseCategories.addAll(accountCategories);

        CategoryAdapter adapter = new CategoryAdapter(expenseCategories);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Category> getCategoriesForCurrentAccount() {
        List<Category> accountCategories = new ArrayList<>();

        return accountCategories;
    }
}