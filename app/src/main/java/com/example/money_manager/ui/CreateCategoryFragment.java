package com.example.money_manager.ui;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.money_manager.R;
import com.example.money_manager.contract.CreateCategoryContract;
import com.example.money_manager.contract.presenter.CreateCategoryPresenter;
import com.example.money_manager.utils.AccountState;

public class CreateCategoryFragment extends Fragment implements CreateCategoryContract.View {
    private Spinner sp;
    private EditText categoryNameEdt;
    private TextView categoryNameValidationMessage;

    private CreateCategoryPresenter presenter;
    private Button createButton;
    private int categoryType = 0;

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
        View v = inflater.inflate(R.layout.fragment_create_category, container, false);

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

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleNameReminder();

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
                presenter.createCategory(name, categoryType, account);
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
        Toast.makeText(requireContext(), "Category created successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createCategoryError() {
        Toast.makeText(requireContext(), "Failed to create category", Toast.LENGTH_SHORT).show();
    }
}