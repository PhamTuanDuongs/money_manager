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
import com.example.money_manager.contract.model.ExpenseModel;
import com.example.money_manager.adapter.CategoryAdapter;
import com.example.money_manager.contract.ExpenseContract;
import com.example.money_manager.contract.presenter.ExpensePresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class UpdateExpenseFragment extends Fragment implements ExpenseContract.View {

    private Button btnDatePicker;
    private Transaction transaction;
    private Category selectedCategory = new Category();
    private TextView tvDate;
    private String balance;
    private EditText edtAmount;
    private EditText edtDesc;
    private EditText edtTitle;
    private GridView gridViewCate;
    private ArrayList<Category> categories= new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private Button btnAdd;
    private ImageButton btnBack;
    private ExpenseModel expenseModel = new ExpenseModel();

    private Date choosenDate = new Date();
    private ExpenseContract.Presenter presenter;


    public UpdateExpenseFragment() {
        // Required empty public constructor
    }

    public UpdateExpenseFragment(Transaction transaction) {
        this.transaction = transaction;
    }

    int[][] states = new int[][] {

            new int[] { android.R.attr.state_focused},
            new int[] { -android.R.attr.state_focused}
    };

    int[] colorsError = new int[] {

            Color.RED,
            Color.GRAY,
    };
    int[] colors = new int[] {

            Color.rgb(1,87,86),
            Color.GRAY,
    };


    ColorStateList colorStateList = new ColorStateList(states, colors);
    ColorStateList colorStateLisError = new ColorStateList(states, colorsError);


    public static UpdateExpenseFragment newInstance(String param1, String param2) {
        UpdateExpenseFragment fragment = new UpdateExpenseFragment();
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
        View v = inflater.inflate(R.layout.fragment_update_expense, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        categories.clear();
        super.onViewCreated(view, savedInstanceState);
        presenter = new ExpensePresenter(new ExpenseModel(), UpdateExpenseFragment.this);
        btnDatePicker = view.findViewById(R.id.btnSelectDate);
        tvDate = view.findViewById(R.id.tvExpenseDate);

        btnAdd = view.findViewById(R.id.btnAddExpense);
        edtAmount = view.findViewById(R.id.edtExpenseValue);
        edtDesc = view.findViewById(R.id.edtExpenseDesc);
        edtTitle = view.findViewById(R.id.edtExpenseTitle);
        btnBack = view.findViewById(R.id.btnGoBack);
        gridViewCate = view.findViewById(R.id.gridViewCategories);
        String email = AccountState.getEmail(getContext(), "email");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(transaction.getCreateAt());
        tvDate.setText(formattedDate);
        edtAmount.setText(transaction.getAmount()+"");
        edtTitle.setText(transaction.getName());
        edtDesc.setText(transaction.getDescription());
        selectedCategory = transaction.getCategory();
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isValidateTitle()){
                    edtTitle.setBackgroundTintList(colorStateLisError);
                }else{
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
                if(!isValidateAmount()){
                    edtAmount.setBackgroundTintList(colorStateLisError);
                }else{
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
                if(!isValidateDesc()){
                    edtDesc.setBackgroundTintList(colorStateLisError);
                }else{
                    edtDesc.setBackgroundTintList(colorStateList);
                }
            }
        });



        expenseModel.getCategoryListByEmailAndType(email,1, new ExpenseContract.Model.onTransactionListener() {
            @Override
            public void onSuccess(Object object) {

                categories.clear();

                for (Category c : (ArrayList<Category>)object) {
                    categories.add(c);
                }
                categoryAdapter = new CategoryAdapter(getContext(), categories);
                for(int i=0;i<categories.size();i++){
                   if( categories.get(i).getAutoID().equals(selectedCategory.getAutoID()))
                   {
                       categoryAdapter.setSelectedPosition(i);
                   }
                }
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


                gridViewCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            View listItem = parent.getChildAt(i);
                            listItem.setBackgroundColor(Color.rgb(255,255,255)); // Set default background color
                        }

                        categoryAdapter.setSelectedPosition(position);
                        selectedCategory = (Category) categoryAdapter.getItem(position);
                        Toast.makeText(getContext(),selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(),selectedCategory.getAutoID(), Toast.LENGTH_SHORT).show();
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
                new AlertDialog.Builder(getContext())
                        .setMessage("Your changes will not be saved!")
                        .setTitle("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadFragment(new ExpenseListFragment());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        choosenDate = transaction.getCreateAt();


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String [] num = formattedDate.split("/");
                int year = Integer.parseInt(num[0]);
                int month = Integer.parseInt(num[1]);
                int day = Integer.parseInt(num[2]);*/
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(transaction.getCreateAt());
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

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



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidateTitle()){
                    validateTitle();

                }else if (!isValidateAmount()){
                    validateAmount();

                } else if(!isValidateDesc()){
                    validateDesc();

                }else{
                    presenter.onUpdateButtonClick(getTransaction());

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
    public void updateExpense(Transaction transaction) {

    }




    @Override
    public void showAddSuccess(String message) {
        loadFragment(new DetailExpenseFragment(getTransaction()));
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
        t.setAutoID(transaction.getAutoID());
        category.setAutoID(selectedCategory.getAutoID());
        category.setName(selectedCategory.getName());
        t.setCategory(category);
        t.setType(1);
        t.setName(edtTitle.getText().toString());
        return t;
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void validateTitle() {
        if(edtTitle.getText().toString().trim().isEmpty()||edtTitle.getText().toString().trim().length()>100){
            edtTitle.setBackgroundTintList(colorStateLisError);

            edtTitle.requestFocus();

        }

    }
    private void validateAmount() {
        if(edtAmount.getText().toString().trim().isEmpty()){
            edtAmount.setBackgroundTintList(colorStateLisError);
            edtAmount.requestFocus();
        }
    }
    private void validateDesc() {
        if(edtDesc.getText().toString().trim().isEmpty()||edtDesc.getText().toString().trim().length()>500){
            edtDesc.setBackgroundTintList(colorStateLisError);

            edtDesc.requestFocus();

        }
    }
    private boolean isValidateTitle() {
        if(edtTitle.getText().toString().trim().isEmpty()||edtTitle.getText().toString().trim().length()>100){
            return false;
        } else{
            return true;
        }
    }
    private boolean isValidateAmount() {
        if(edtAmount.getText().toString().trim().isEmpty()){
            return false;
        } else{
            return true;
        }
    }
    private boolean isValidateDesc() {
        if(edtDesc.getText().toString().trim().isEmpty()||edtDesc.getText().toString().trim().length()>500){
            return false;
        } else{
            return true;
        }
    }
}