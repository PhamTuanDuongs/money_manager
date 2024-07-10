package com.example.money_manager.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.money_manager.R;
import com.example.money_manager.entity.Transaction;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private ArrayList<Transaction> expenses;
    ExpenseListClickListener listenerDelete;
    ExpenseListClickListener listenerUpdate;

    public void setListenerDelete(ExpenseListClickListener listenerDelete) {
        this.listenerDelete = listenerDelete;
    }

    public void setListenerUpdate(ExpenseListClickListener listenerUpdate) {
        this.listenerUpdate = listenerUpdate;
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
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Transaction t = expenses.get(position);
        holder.tvName.setText(t.getName());
        holder.tvValue.setText("- " +t.getAmount() + "  VND");
        Timestamp timestamp = t.getCreateAt();
        Date date = timestamp.toDate();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        // Format the date to a string
        String formattedDate = formatter.format(date);


        // Create a SimpleDateFormat object with the desired format



        holder.tvDate.setText(formattedDate);
    }

    public void removeItem(int pos) {expenses.remove(pos);
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
        private TextView tvName;
        private TextView tvDate;
        private Button btnUpdate;
        private Button btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.txtExpenseValue);
            tvName = itemView.findViewById(R.id.txtExpenseName);
            tvDate = itemView.findViewById(R.id.txtExpenseDateCreated);
            /*btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);*/
            /*btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerUpdate.OnClick(view, getLayoutPosition());
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerDelete.OnClick(view, getLayoutPosition());
                }
            });*/

        }
    }

    public interface ExpenseListClickListener {
        void OnClick(View v, int position);
    }
}

