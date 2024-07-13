package com.example.money_manager.ui;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.money_manager.activity.authentication.model.ExpenseModel;
import com.example.money_manager.adapter.CategoryAdapter;
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.contract.presenter.ExpensePresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddExpenseFragment extends Fragment implements ExpenseContract.View {

    private Button btnDatePicker;
    private Category selectedCategory = new Category();
    private TextView tvDate;
    private String balance;
    private TextView tvBalance;
    private EditText edtAmount;
    private EditText edtDesc;
    private EditText edtTitle;
    private GridView gridViewCate;
    private ArrayList<Category> categories= new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private Button btnAdd;
    private ImageButton btnBack;
    private ExpenseModel expenseModel = new ExpenseModel();

    private Date choosenDate;
    private ExpenseContract.Presenter presenter;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
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
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        categories.clear();
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExpensePresenter(new ExpenseModel(), AddExpenseFragment.this);
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
        expenseModel.getCategoryListByEmailAndType(email,1, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {
                ArrayList<Category>quac = (ArrayList<Category>)object;

                categories.clear();

                for (Category c : (ArrayList<Category>)object) {
                    categories.add(c);
                }
                categoryAdapter = new CategoryAdapter(getContext(), categories);
                gridViewCate.setAdapter(categoryAdapter);

               /* gridViewCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedCategory = categories.get(position);
                        categoryAdapter.setSelectedPosition(position);
                        view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected
                        Toast.makeText(getContext(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                    }
                });*/

                gridViewCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            View listItem = parent.getChildAt(i);
                            listItem.setBackgroundColor(Color.rgb(255,255,255)); // Set default background color
                        }

                        categoryAdapter.setSelectedPosition(position);
                        selectedCategory = (Category) categoryAdapter.getItem(position);
                        Toast.makeText(getContext(),selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(Color.rgb(228, 255, 255));// Ensure setSelectedPosition method works as expected

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();

            }
        });






        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ExpenseListFragment());
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
                                tvDate.setText(day + "-" + (month + 1) + "-" + year);
                           Calendar cal = Calendar.getInstance();
                               cal.set(year, month, day);
                          choosenDate = cal.getTime();
                            }
                        }, year, month, day
                );
                dialog.show();
            }
        });
        balance = AccountState.getAccountBalance(getContext(), "balance")+ " VND";
        tvBalance.setText("Balance: " + balance);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddButtonClick(getTransaction());
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
    public void navigateToExpenseActivity() {

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
                        loadFragment(new ExpenseListFragment());
                    }
                })
                .show();
    }

    @Override
    public void setListExpense(ArrayList<Transaction> transactions) {

    }



    @Override
    public void DeleteExpense(String message) {

    }

    public Transaction getTransaction() {
        Transaction t = new Transaction();
        t.setAmount(Double.parseDouble(edtAmount.getText().toString()));
        t.setDescription(edtDesc.getText().toString());
        t.setCreateAt(choosenDate);
        Category category = new Category();
        category.setAutoID(selectedCategory.getAutoID());
        t.setCategory(category);
        t.setType(1);
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

}