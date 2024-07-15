package com.example.money_manager.ui;

import static com.example.money_manager.utils.DateTimeUtils.getCurrentWeek;
import static com.example.money_manager.utils.DateTimeUtils.getNextWeek;
import static com.example.money_manager.utils.DateTimeUtils.getPreviousWeek;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeListByWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeListByWeekFragment extends Fragment implements IncomeContract.View {

    private ArrayList<Transaction> incomes = new ArrayList<>();
    private IncomeContract.Presenter presenter;
    private RecyclerView incomeRecyclerView;
    private IncomeAdapter incomeAdapter;
    private ProgressBar pbLoading;
    private TextView txtDate, txtNoIncome;
    private Button btnPrevious;
    private Button btnNext;
    private int incomePosition;

    public static IncomeListByWeekFragment newInstance(String param1, String param2) {
        IncomeListByWeekFragment fragment = new IncomeListByWeekFragment();
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
        View v = inflater.inflate(R.layout.fragment_income_list_by_week, container, false);
        presenter = new IncomePresenter(new IncomeModel(), this);
        pbLoading = v.findViewById(R.id.progressBar);
        incomeRecyclerView = v.findViewById(R.id.income_recycle_view_week);
        incomeAdapter = new IncomeAdapter(getContext(), incomes);
        registerForContextMenu(incomeRecyclerView);
        incomeAdapter.setOnLongItemClickListener(new IncomeAdapter.OnLongItemClickListener() {
            @Override
            public void itemLongClicked(View v, int position) {
                incomePosition = position;
            }
        });

        incomeRecyclerView.setAdapter(incomeAdapter);
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtDate = v.findViewById(R.id.txtSelectedWeek);
        txtDate.setText(getCurrentWeek());
        btnNext = v.findViewById(R.id.btnNextWeek);
        btnPrevious = v.findViewById(R.id.btnPreviousWeek);
        txtNoIncome = v.findViewById(R.id.txtNoExpense);
        txtNoIncome.setText("");


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeRecyclerView.setVisibility(View.GONE);
                txtNoIncome.setVisibility(View.GONE);
                String w = txtDate.getText().toString();
                pbLoading.setVisibility(View.VISIBLE);
                txtDate.setText(getNextWeek(w));
                presenter.onGetListIncomeByWeek(getNextWeek(w));

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeRecyclerView.setVisibility(View.GONE);
                txtNoIncome.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
                String w = txtDate.getText().toString();
                txtDate.setText(getPreviousWeek(w));
                presenter.onGetListIncomeByWeek(getPreviousWeek(w));

            }
        });
        presenter.onGetListIncomeByWeek(getCurrentWeek());
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
        if (incomes.size() == 0) {
            txtNoIncome.setVisibility(View.VISIBLE);
            txtNoIncome.setText("No data");
        } else {
            txtNoIncome.setVisibility(View.GONE);
            incomeRecyclerView.setVisibility(View.VISIBLE);
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