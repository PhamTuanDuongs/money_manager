package com.example.money_manager.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.money_manager.R;
import com.example.money_manager.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories = new ArrayList<>();
    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }
    public ImageView categoryIcon;
    public TextView categoryName;
    private int selectedPosition=0;

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        categoryIcon = convertView.findViewById(R.id.category_icon);
        categoryName = convertView.findViewById(R.id.category_name);
        Category category  = categories.get(position);
        categoryIcon.setImageResource(convertView.getContext().getResources().getIdentifier(
                category.getImage(), "drawable", convertView.getContext().getPackageName()));
        categoryName.setText(category.getName());
        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.rgb(228, 255, 255));
        } else {
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        return convertView;
    }
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }


    @Override
    public boolean isEmpty() {
        return false;
    }



}
