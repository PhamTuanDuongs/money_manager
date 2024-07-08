package com.example.money_manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.money_manager.R;
import com.example.money_manager.entity.Transaction;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private Context context;
    private ArrayList<Transaction> incomes;

    public IncomeAdapter(Context context, ArrayList<Transaction> incomes) {
        this.context = context;
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.income_item, parent, false);
        IncomeViewHolder viewHolder = new IncomeViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Transaction t = incomes.get(position);
        holder.tvName.setText(t.getName());
        holder.tvAmount.setText(t.getAmount() + "");
        holder.tvDesc.setText(t.getDescription());
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc;
        private TextView tvAmount;
        private TextView tvName;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.txtDes);
            tvAmount = itemView.findViewById(R.id.txtAmount);
            tvName = itemView.findViewById(R.id.txtName);

        }
    }
}
