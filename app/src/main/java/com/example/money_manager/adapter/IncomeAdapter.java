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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private Context context;
    private ArrayList<Transaction> incomes;
    IncomeListClickListener listenerDelete;
    IncomeListClickListener listenerUpdate;

    public void setListenerDelete(IncomeListClickListener listenerDelete) {
        this.listenerDelete = listenerDelete;
    }

    public void setListenerUpdate(IncomeListClickListener listenerUpdate) {
        this.listenerUpdate = listenerUpdate;
    }

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
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-YYYY");
        holder.tvName.setText(t.getName());
        holder.tvAmount.setText(t.getAmount() + "");
        holder.tvDesc.setText(t.getDescription());
        holder.tvDate.setText(sf.format(t.getCreateAt()).toString());
    }

    public void removeItem(int pos) {
        incomes.remove(pos);
    }

    public Transaction getItem(int pos) {
        return incomes.get(pos);
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDesc;
        private TextView tvAmount;
        private TextView tvName;
        private TextView tvDate;
        private Button btnUpdate;
        private Button btnDelete;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.txtDes);
            tvAmount = itemView.findViewById(R.id.txtAmount);
            tvName = itemView.findViewById(R.id.txtName);
            tvDate = itemView.findViewById(R.id.txtDate);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
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
            });

        }
    }

    public interface IncomeListClickListener {
        void OnClick(View v, int position);
    }
}
