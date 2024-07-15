package com.example.money_manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.money_manager.R;
import com.example.money_manager.entity.CategorySum;


import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private Context context;
    private ArrayList<CategorySum> categorySums;

    public ReportAdapter(Context context, ArrayList<CategorySum> categorySums) {
        this.context = context;
        this.categorySums = categorySums;
    }


    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.category_sum_item, parent, false);
        ReportAdapter.ReportViewHolder viewHolder = new ReportAdapter.ReportViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
        CategorySum category = categorySums.get(position);
        holder.tvCateName.setText(category.getName());
        holder.tvPercent.setText(category.getPercent());
        holder.imgCateIcon.setImageResource(context.getResources().getIdentifier(
                category.getIcon(), "drawable", context.getPackageName()));
    }


    @Override
    public int getItemCount() {
        return categorySums.size();
    }
    public class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCateName, tvPercent;
        private ImageView imgCateIcon;


        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCateName = itemView.findViewById(R.id.txtCategoryName);
            tvPercent = itemView.findViewById(R.id.txtPercent);
            imgCateIcon = itemView.findViewById(R.id.icon_cate_img);

        }


    }

}
