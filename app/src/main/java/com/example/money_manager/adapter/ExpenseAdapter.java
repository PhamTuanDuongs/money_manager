package com.example.money_manager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.money_manager.R;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.google.firebase.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private ArrayList<Transaction> expenses;
    private ExpenseListClickListener listener;


    public ExpenseListClickListener listener() {
        return listener;
    }

    public void setListener(ExpenseListClickListener listener) {
        this.listener = listener;
    }



    public ExpenseAdapter(Context context, ArrayList<Transaction> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.expense_item, parent, false);
        ExpenseViewHolder viewHolder = new ExpenseViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transaction t = expenses.get(position);
        holder.tvName.setText("Title: "+t.getName());
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String amount = df.format(t.getAmount());
        holder.tvValue.setText("- " +amount + "  VND");
        holder.tvCate.setText("Category: "+ t.getCategory().getName());
        Date date = t.getCreateAt();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(date);
        holder.tvDate.setText(formattedDate);
        holder.tvDesc.setText("Description: "+t.getDescription());
        holder.btnMoreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.activity_expense_menu);

                popupMenu.setOnMenuItemClickListener(item -> {
                    String id = String.valueOf(item.getTitle());
                    switch (id) {
                        case "Delete":
                            Log.d("contextual menu", "click");
                            removeItem(position);

                            return true;
                        case "Update":
                            updateItem(getItem(position));

                            return true;
                        default:
                            return false;
                    }
                });

                popupMenu.show();
            }});

    }

    public void removeItem(int pos) {
        Log.d("Expense Adpater", "removeItem");
        listener.OnDelete(new View(context), pos);
        expenses.remove(pos);

    }
    public void updateItem(Transaction transaction) {
        listener.OnUpdate(new View(context), transaction);
    }

    public Transaction getItem(int pos) {
        return expenses.get(pos);
    }


    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvValue;
        private TextView tvCate;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvDesc;
        private ImageButton btnMoreAction;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.txtExpenseValue);
            btnMoreAction = itemView.findViewById(R.id.btn_more_action);
            tvName = itemView.findViewById(R.id.txtExpenseName);
            tvDate = itemView.findViewById(R.id.txtExpenseDateCreated);
            tvCate = itemView.findViewById(R.id.txtExpenseCate);
            tvDesc = itemView.findViewById(R.id.txtExpenseDesc);



        }
    }
    public interface ExpenseListClickListener {
        void OnDelete(View v, int position);
        void OnUpdate (View v, Transaction transaction);

    }




}

