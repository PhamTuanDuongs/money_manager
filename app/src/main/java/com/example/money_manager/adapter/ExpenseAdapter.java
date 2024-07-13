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
import androidx.recyclerview.widget.RecyclerView;

import com.example.money_manager.R;
import com.example.money_manager.entity.Category;
import com.example.money_manager.entity.Transaction;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private ArrayList<Transaction> expenses;

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
        holder.tvValue.setText("- " +t.getAmount() + "  VND");
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
                            //removeItem(position);
                            Log.d("delete","delete"+ position);
                            Toast.makeText(context,"delete"+ position, Toast.LENGTH_SHORT);
                            // Perform delete action

                            return true;
                        case "Update":
                            Log.d("upadte","delete"+ position);
                            Toast.makeText(context,"update"+ position, Toast.LENGTH_SHORT);
                            /*Transaction t= getItem(position);
                            updateItem(t);*/

                            return true;
                        default:
                            return false;
                    }
                });

                popupMenu.show();
            }});

    }

    public void removeItem(int pos) {expenses.remove(pos);
    }
    public void updateItem(Transaction transaction) {;
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


}

