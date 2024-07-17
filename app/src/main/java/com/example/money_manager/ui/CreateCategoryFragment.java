package com.example.money_manager.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.money_manager.R;
import com.example.money_manager.adapter.CategoryAdapter;
import com.example.money_manager.contract.CreateCategoryContract;
import com.example.money_manager.contract.presenter.CreateCategoryPresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.utils.AccountState;

import java.util.ArrayList;

public class CreateCategoryFragment extends Fragment implements CreateCategoryContract.View {
    private Spinner sp;
    private EditText categoryNameEdt;
    private Category selectedCategory = new Category();
    private TextView categoryNameValidationMessage;
    private ArrayList<Category> categories= new ArrayList<>();


    private CreateCategoryPresenter presenter;
    private Button createButton;
    private int categoryType = 0;
    private GridView gridViewCate;
    private CategoryAdapter categoryAdapter;


    public CreateCategoryFragment() {
    }

    public static CreateCategoryFragment newInstance(String param1, String param2) {
        CreateCategoryFragment fragment = new CreateCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CreateCategoryPresenter(CreateCategoryFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categories.clear();
        View v = inflater.inflate(R.layout.fragment_create_category, container, false);
        gridViewCate = v.findViewById(R.id.gridViewCategories);
        categoryNameEdt = v.findViewById(R.id.edtCategoryTitle);
        categoryNameValidationMessage = v.findViewById(R.id.txtCategoryValidation);
        createButton = v.findViewById(R.id.btnCreateCategory);
        createButton.setAlpha(0.5f);
        createButton.setEnabled(false);

        String[] values = {"Income", "Expense"};
        sp = (Spinner) v.findViewById(R.id.selectCategoryType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);
        Category c = new Category();
        c.setImage("icon_transportation");
        c.setName("");
        c.setAutoID("");
        categories.add(c);
        Category c1 = new Category();
        c1.setImage("icon_health");
        c1.setName("");
        c1.setAutoID("");
        categories.add(c1);
        Category c2 = new Category();
        c2.setImage("icon_education");
        c2.setName("");
        c2.setAutoID("");
        categories.add(c2);
        Category c3 = new Category();
        c3.setImage("icon_gift");
        c3.setName("");
        c3.setAutoID("");
        categories.add(c3);



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleNameReminder();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        gridViewCate.setAdapter(categoryAdapter);
        int rows = (int) Math.ceil(categories.size() / 2.0);
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) {
            View listItem = categoryAdapter.getView(i, null, gridViewCate);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridViewCate.getLayoutParams();
        params.height = totalHeight + (gridViewCate.getVerticalSpacing() * (rows-1));
        gridViewCate.setLayoutParams(params);
        gridViewCate.requestLayout();
        selectedCategory = (Category) categoryAdapter.getItem(0);

        gridViewCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255,255,255)); // Set default background color
                }

                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);

                view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected
            }
        });
        gridViewCate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255,255,255)); // Set default background color
                }

                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);

                view.setBackgroundColor(Color.rgb(228, 255, 255));// E
                return true;// nsure setSelectedPosition method works as expected

            }
        });

        gridViewCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255,255,255)); // Set default background color
                }

                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);

                view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (parent.getCount() > 0) {
                    categoryType = 0;
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categoryNameEdt.getText().toString();
                String account = AccountState.getEmail(requireContext(), "email");
                presenter.createCategory(name, categoryType, account, selectedCategory.getImage());
            }
        });
    }

    private void handleNameReminder() {
        categoryNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    categoryNameValidationMessage.setText("Category name is required");
                    createButton.setAlpha(0.5f);
                    createButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    categoryNameValidationMessage.setText("");
                    createButton.setAlpha(1.0f);
                    createButton.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void createCategorySuccess() {
        loadFragment(new IncomeListFragment());
    }

    @Override
    public void createCategoryError() {
        Toast.makeText(requireContext(), "Failed to create category", Toast.LENGTH_SHORT).show();
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}