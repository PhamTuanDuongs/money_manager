package com.example.money_manager.ui;

import static com.example.money_manager.utils.DateTimeUtils.getCurrentMonth;
import static com.example.money_manager.utils.DateTimeUtils.getDateYearString;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money_manager.R;
import com.example.money_manager.adapter.IncomeAdapter;
import com.example.money_manager.contract.IncomeContract;
import com.example.money_manager.contract.model.IncomeModel;
import com.example.money_manager.contract.presenter.IncomePresenter;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;

import java.util.ArrayList;


public class IncomeListByYearFragment extends Fragment implements IncomeContract.View {

    private ArrayList<Transaction> incomes = new ArrayList<>();
    private IncomeContract.Presenter presenter;
    private RecyclerView incomeRecycleView;
    private IncomeAdapter incomeAdapter;
    private ProgressBar pbLoading;
    private TextView txtDate, txtNoExpense;
    private Button btnPrevious;
    private Button btnNext;
    private int incomePosition;

    public static IncomeListByYearFragment newInstance(String param1, String param2) {
        IncomeListByYearFragment fragment = new IncomeListByYearFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income_list_by_year, container, false);
        presenter = new IncomePresenter(new IncomeModel(), this);
        pbLoading = v.findViewById(R.id.progressBar);
        incomeRecycleView = v.findViewById(R.id.income_recycle_view_year);
        incomeAdapter = new IncomeAdapter(getContext(), incomes);
        registerForContextMenu(incomeRecycleView);
        incomeAdapter.setOnLongItemClickListener(new IncomeAdapter.OnLongItemClickListener() {
            @Override
            public void itemLongClicked(View v, int position) {
                incomePosition = position;
            }
        });
        incomeRecycleView.setAdapter(incomeAdapter);
        incomeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtDate = v.findViewById(R.id.txtSelectedYear);
        txtDate.setText(getCurrentMonth().substring(4,8));
        btnNext =v.findViewById(R.id.btnNextYear);
        btnPrevious =v.findViewById(R.id.btnPreviousYear);
        txtNoExpense =v.findViewById(R.id.txtNoExpense);
        txtNoExpense.setText("");





        /*expenseAdapter.setListenerDelete(new ExpenseAdapter.ExpenseListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                expensePosition = position;
                Transaction t = expenseAdapter.getItem(position);
                presenter.onDeleteButtonClick(t.getId());
            }
        });

        expenseAdapter.setListenerUpdate(new ExpenseAdapter.ExpenseListClickListener() {
            @Override
            public void OnClick(View v, int position) {
                expensePosition = position;

            }
        });*/

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                String w = txtDate.getText().toString();
                pbLoading.setVisibility(View.VISIBLE);
                txtDate.setText(Integer.parseInt(w)+1+"");
                presenter.onGetListIncomeByYear(getDateYearString(Integer.parseInt(w)+1+""));

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeRecycleView.setVisibility(View.GONE);
                txtNoExpense.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                String w = txtDate.getText().toString();
                txtDate.setText(Integer.parseInt(w)-1+"");
                presenter.onGetListIncomeByYear(getDateYearString(Integer.parseInt(w)-1+""));

            }
        });
        presenter.onGetListIncomeByYear(getDateYearString(getCurrentMonth().substring(4,8)));
    }
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.income_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            Transaction t = incomeAdapter.getItem(incomePosition);
            presenter.onDeleteButtonClick(t.getId());
        }

        if (id == R.id.update) {
            IncomeUpdateFragment fragment = new IncomeUpdateFragment(incomeAdapter.getItem(incomePosition).getId());
            FragmentManager manager = getParentFragment().getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.nav_host_fragment_content_main,fragment);

            transaction.addToBackStack(null);
            transaction.commit();
        }

        return true;
    }
    @Override
    public void showAddError(String title, String error) {

    }

    @Override
    public void navigateToIncomeActivity() {

    }

    @Override
    public void showAddSuccess(String message) {

    }

    @Override
    public void setListIncome(ArrayList<Transaction> transactions) {
        incomes.clear();

        for (Transaction trans : transactions
        ) {
            incomes.add(trans);
        }
        if(incomes.size()==0){
            txtNoExpense.setVisibility(View.VISIBLE);
            txtNoExpense.setText("No data");
        }else{
            txtNoExpense.setVisibility(View.GONE);
            incomeRecycleView.setVisibility(View.VISIBLE);
        }
        incomeAdapter.notifyDataSetChanged();
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void DeleteIncome(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage(message)
                .setIcon(R.drawable.check)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        incomeAdapter.removeItem(incomePosition);
        incomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateIncome(Transaction transaction) {

    }

    @Override
    public void updateIncomeOnSuccess(String message) {

    }

    @Override
    public void setBalance(String balance) {

    }

    @Override
    public void setCategory(ArrayList<Category> categories) {

    }
}