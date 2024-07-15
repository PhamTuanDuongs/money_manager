package com.example.money_manager.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.money_manager.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private PieChart pieChart;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();

        return view;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.getDescription().setEnabled(false);

    }

    private void loadPieChartData() {
        List<PieEntry> entries = new ArrayList<>();

        // Thêm dữ liệu vào biểu đồ
        entries.add(new PieEntry(90f, "Quà Tặng"));
        entries.add(new PieEntry(75f, "Interest"));
        entries.add(new PieEntry(80f, "Gift"));
        entries.add(new PieEntry(65f, "Paycheck"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FFBB86FC"));
        colors.add(Color.parseColor("#FF6200EE"));
        colors.add(Color.parseColor("#FF03DAC5"));
        colors.add(Color.parseColor("#FF018786"));

        PieDataSet dataSet = new PieDataSet(entries, "Income Categories");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);


        pieChart.setCenterText("Income Categories");
        pieChart.setData(data);
        pieChart.invalidate();
    }
}