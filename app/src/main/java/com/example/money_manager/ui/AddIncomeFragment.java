package com.example.money_manager.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.adapter.CategoryAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;
import com.google.firebase.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddIncomeFragment extends Fragment implements IncomeContract.View {

    private Button btnDatePicker;
    private Category selectedCategory = new Category();
    private TextView tvDate;
    private String balance;
    private TextView tvBalance;
    private EditText edtAmount;
    private EditText edtDesc;
    private EditText edtTitle;
    private GridView gridViewCate;
    private ArrayList<Category> categories = new ArrayList<>();
    private com.example.money_manager.adapter.CategoryAdapter categoryAdapter;
    private Button btnAdd;
    private ImageButton btnBack;
    private IncomeModel incomeModel = new IncomeModel();

    private Date choosenDate = new Date();
    private IncomeContract.Presenter presenter;

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    int[][] states = new int[][]{

            new int[]{android.R.attr.state_focused},
            new int[]{-android.R.attr.state_focused}
    };

    int[] colorsError = new int[]{

            Color.RED,
            Color.GRAY,
    };
    int[] colors = new int[]{

            Color.rgb(1, 87, 86),
            Color.GRAY,
    };


    ColorStateList colorStateList = new ColorStateList(states, colors);
    ColorStateList colorStateLisError = new ColorStateList(states, colorsError);


    public static AddIncomeFragment newInstance(String param1, String param2) {
        AddIncomeFragment fragment = new AddIncomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_income, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new IncomePresenter(new IncomeModel(), this);
        btnDatePicker = view.findViewById(R.id.btnSelectDate);
        tvDate = view.findViewById(R.id.tvExpenseDate);
        tvBalance = view.findViewById(R.id.txtBalance);
        btnAdd = view.findViewById(R.id.btnAddExpense);
        edtAmount = view.findViewById(R.id.edtExpenseValue);
        edtDesc = view.findViewById(R.id.edtExpenseDesc);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        btnBack = view.findViewById(R.id.btnGoBack);
        gridViewCate = view.findViewById(R.id.gridViewCategories);
        String email = AccountState.getEmail(getContext(), "email");
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(d);
        tvDate.setText(formattedDate);
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidateTitle()) {
                    edtTitle.setBackgroundTintList(colorStateLisError);
                } else {
                    edtTitle.setBackgroundTintList(colorStateList);
                }

            }
        });
        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidateAmount()) {
                    edtAmount.setBackgroundTintList(colorStateLisError);
                } else {
                    edtAmount.setBackgroundTintList(colorStateList);
                }

            }
        });
        edtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidateDesc()) {
                    edtDesc.setBackgroundTintList(colorStateLisError);
                } else {
                    edtDesc.setBackgroundTintList(colorStateList);
                }
            }
        });


        presenter.onGetCategoryListByEmailAndType(email, 0);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new IncomeListFragment());
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvDate.setText(day + "/" + (month + 1) + "/" + year);
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, day);
                                choosenDate = cal.getTime();
                            }
                        }, year, month, day
                );
                dialog.show();
            }
        });

        presenter.onGetBalance(email);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidateTitle()) {
                    validateTitle();

                } else if (!isValidateAmount()) {
                    validateAmount();

                } else if (!isValidateDesc()) {
                    validateDesc();

                } else {
                    presenter.onAddButtonClick(getTransaction());
                }

            }
        });


    }

    @Override
    public void showAddError(String title, String error) {
        new AlertDialog.Builder(getContext())
                .setTitle("Fail")
                .setMessage(error)
                .setIcon(R.drawable.error)
                .setPositiveButton("OK", null)
                .show();

    }

    @Override
    public void navigateToIncomeActivity() {

    }


    @Override
    public void showAddSuccess(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage(message)
                .setIcon(R.drawable.check)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadFragment(new IncomeListFragment());
                    }
                })
                .show();
    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {

    }


    @Override
    public void DeleteIncome(String message) {

    }

    @Override
    public void updateIncome(Transaction transaction) {

    }

    @Override
    public void updateIncomeOnSuccess(String message) {

    }

    @Override
    public void setBalance(String balance) {
        tvBalance.setText("Balance: " + balance + " VND");
        tvBalance.setText(balance);
    }

    @Override
    public void setCategory(ArrayList<Category> object) {
        categories.clear();

        for (Category c :  object) {
            categories.add(c);
        }
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
        params.height = totalHeight + (gridViewCate.getVerticalSpacing() * (rows - 1));
        gridViewCate.setLayoutParams(params);
        gridViewCate.requestLayout();
        selectedCategory = (Category) categoryAdapter.getItem(0);

        gridViewCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255, 255, 255)); // Set default background color
                }
                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);
                Toast.makeText(getContext(), selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
                Log.d("CATEGORY", selectedCategory.getAutoID());

                view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected
            }
        });
        gridViewCate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255, 255, 255)); // Set default background color
                }

                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);
                Toast.makeText(getContext(), selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.rgb(228, 255, 255));// E
                return true;// nsure setSelectedPosition method works as expected

            }
        });

        gridViewCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.rgb(255, 255, 255)); // Set default background color
                }

                categoryAdapter.setSelectedPosition(position);
                selectedCategory = (Category) categoryAdapter.getItem(position);
                Toast.makeText(getContext(), selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Transaction getTransaction() {
        Transaction t = new Transaction();
        t.setAmount(Double.parseDouble(edtAmount.getText().toString()));
        t.setDescription(edtDesc.getText().toString());
        t.setCreateAt(choosenDate);
        Category category = new Category();
        category.setAutoID(selectedCategory.getAutoID());
        t.setCategory(category);
        t.setType(0);
        t.setName(edtTitle.getText().toString());
        return t;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void validateTitle() {
        if (edtTitle.getText().toString().trim().isEmpty() || edtTitle.getText().toString().trim().length() > 100) {
            edtTitle.setBackgroundTintList(colorStateLisError);

            edtTitle.requestFocus();

        }

    }

    private void validateAmount() {
        if (edtAmount.getText().toString().trim().isEmpty()) {
            edtAmount.setBackgroundTintList(colorStateLisError);
            edtAmount.requestFocus();
        }
    }

    private void validateDesc() {
        if (edtDesc.getText().toString().trim().isEmpty() || edtDesc.getText().toString().trim().length() > 500) {
            edtDesc.setBackgroundTintList(colorStateLisError);

            edtDesc.requestFocus();

        }
    }

    private boolean isValidateTitle() {
        if (edtTitle.getText().toString().trim().isEmpty() || edtTitle.getText().toString().trim().length() > 100) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidateAmount() {
        if (edtAmount.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidateDesc() {
        if (edtDesc.getText().toString().trim().isEmpty() || edtDesc.getText().toString().trim().length() > 500) {
            return false;
        } else {
            return true;
        }
    }
}