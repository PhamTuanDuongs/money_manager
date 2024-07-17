package com.example.money_manager.adapter;

import android.content.Context;
import android.util.Log;
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
import java.util.Date;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private Context context;
    private ArrayList<Transaction> incomes;
    IncomeListClickListener listenerDelete;
    IncomeListClickListener listenerUpdate;
    OnLongItemClickListener mOnLongItemClickListener;

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        mOnLongItemClickListener = onLongItemClickListener;
    }
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
        int pos = holder.getLayoutPosition();
        Transaction t = incomes.get(position);
        Date date = t.getCreateAt();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = formatter.format(date);

        holder.tvName.setText("Name:  "+ t.getName());
        holder.tvAmount.setText("+" +t.getAmount() + " VND");
        holder.tvDesc.setText("Description:  "+t.getDescription());
        holder.tvDate.setText(formattedDate);
        holder.tvCate.setText("Category:  "+t.getCategory().getName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnLongItemClickListener != null) {
                    mOnLongItemClickListener.itemLongClicked(view, pos);
                    Log.d("INCOME:" , "Hello");
                }
                return false;
            }
        });
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
        private TextView tvDecs;
        private Button btnUpdate;
        private Button btnDelete;

        private TextView tvCate;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.txtIncomeValue);
            tvName = itemView.findViewById(R.id.txtIncomeName);
            tvDate = itemView.findViewById(R.id.txtIncomeDateCreated);
            tvCate = itemView.findViewById(R.id.txtIncomeCate);
            tvDesc = itemView.findViewById(R.id.txtIncomeDesc);

        }
    }

    public interface IncomeListClickListener {
        void OnClick(View v, int position);

    }

    public interface OnLongItemClickListener {
        void itemLongClicked(View v, int position);
    }
}
