package com.example.money_manager.ui;

import static com.example.money_manager.utils.DateTimeUtils.getCurrentMonth;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.money_manager.R;
import com.example.money_manager.adapter.ExpenseAdapter;
import com.example.money_manager.adapter.ReportAdapter;
import com.example.money_manager.contract.ReportContract;
import com.example.money_manager.contract.model.ReportModel;
import com.example.money_manager.contract.presenter.ReportPresenter;
import com.example.money_manager.entity.CategorySum;
import com.example.money_manager.entity.Transaction;
import com.example.money_manager.utils.AccountState;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Fragment implements ReportContract.View {


    private ReportContract.Presenter presenter;
    private Double[] expenses = new Double[]{0.0,2.0,6.0,7.0,2.0,8.0,1.0,9.0,2.0,5.0,5.0,9.0,2.0};
    private Double[] incomes = new Double[]{9.0,6.0,2.0,5.0,10.0,4.0,6.0,9.0,9.0,2.0,5.0,0.0,5.0};
    private RecyclerView expenseRecyclerView;
    private RecyclerView incomeRecyclerView;
    private ReportAdapter expenseAdapter;
    private ReportAdapter incomeAdapter;
    private ArrayList<CategorySum> categorySumByExpense = new ArrayList<>();
    private ArrayList<CategorySum> categorySumByIncome = new ArrayList<>();
    private ProgressBar pbLoading;
    private TextView txtDate;
    private Button btnPrevious;
    private Button btnNext;
    private CombinedChart mChart;


    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ReportPresenter(new ReportModel(), ReportFragment.this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        pbLoading = v.findViewById(R.id.progressBar);
        incomeRecyclerView = v.findViewById(R.id.report_income_recycle_view_year);
        expenseRecyclerView = v.findViewById(R.id.report_expense_recycle_view_year);
        String email = AccountState.getEmail(getContext(), "email");
        presenter.onGetExpenseSumReport(2024,email);
        presenter.onGetIncomeSumReport(2024,email);
        presenter.onGetCategoryByExpenseReport(2024,email);
        presenter.onGetCategoryByIncomeReport(2024,email);
        mChart = v.findViewById(R.id.combinedChart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        txtDate = v.findViewById(R.id.txtSelectedYear);
        txtDate.setText(getCurrentMonth().substring(4, 8));
        btnNext = v.findViewById(R.id.btnNextYear);
        btnPrevious = v.findViewById(R.id.btnPreviousYear);
        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String email = AccountState.getEmail(getContext(), "email");

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }
            @Override
            public void onNothingSelected() {

            }
        });




        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String w = txtDate.getText().toString();
                pbLoading.setVisibility(View.VISIBLE);
                txtDate.setText(Integer.parseInt(w)+1+"");
                presenter.onGetExpenseSumReport(Integer.parseInt(w)+1,email);
                presenter.onGetIncomeSumReport(Integer.parseInt(w)+1,email);
                presenter.onGetCategoryByExpenseReport(Integer.parseInt(w)+1,email);
                presenter.onGetCategoryByIncomeReport(Integer.parseInt(w)+1,email);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLoading.setVisibility(View.VISIBLE);
                String w = txtDate.getText().toString();
                txtDate.setText(Integer.parseInt(w) - 1 + "");
                presenter.onGetExpenseSumReport(Integer.parseInt(w)-1,email);
                presenter.onGetIncomeSumReport(Integer.parseInt(w)-1,email);
                presenter.onGetCategoryByExpenseReport(Integer.parseInt(w)-1,email);
                presenter.onGetCategoryByIncomeReport(Integer.parseInt(w)-1,email);

            }
        });

    }


    @Override
    public void setListExpenseSum(Map<Integer, Double> monthlyTotals) {
        Collection<Double> values = monthlyTotals.values();
        expenses = values.toArray(new Double[values.size()]);
        pbLoading.setVisibility(View.GONE);
        for(Double d: expenses){
            Log.d("setView", d+"");
        }
        fillDataToChart();
    }

    @Override
    public void setListIncomeSum(Map<Integer, Double> monthlyTotals) {
        Collection<Double> values = monthlyTotals.values();
        incomes = values.toArray(new Double[values.size()]);
        pbLoading.setVisibility(View.GONE);
        fillDataToChart();
    }

    @Override
    public void setListCategorySumByIncome(ArrayList<CategorySum> cates) {
        categorySumByIncome.clear();
        ArrayList<CategorySum> categories = new ArrayList<>();
        float total =0.0f;


        for (CategorySum cate : cates)
        {
            if (!isContained(categories,cate)) {
                categories.add(cate);
                total+=cate.getTotalAmount();
            }
        }
        for (CategorySum cate:categories){
            cate.setPercent(Math.round(cate.getTotalAmount()/total*100.0f)+"%");
            Log.d("category setView expense","name: "+cate.getName()+"  amount:  "+ cate.getPercent());
        }
        incomeAdapter = new ReportAdapter(getContext(), categories);
        incomeRecyclerView.setAdapter(incomeAdapter);
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void setListCategorySumByExpense(ArrayList<CategorySum> cates) {
        categorySumByExpense.clear();
        ArrayList<CategorySum> categories = new ArrayList<>();
        float total =0.0f;
        for (CategorySum cate : cates)
        {

            if (!isContained(categories,cate)) {
                categories.add(cate);
                total+=cate.getTotalAmount();
            }
        }
        for (CategorySum cate:categories){
            cate.setPercent(Math.round(cate.getTotalAmount()/total*100.0f)+"%");
            Log.d("category setView expense","name: "+cate.getName()+"  amount:  "+ cate.getTotalAmount());
        }
        expenseAdapter = new ReportAdapter(getContext(), categories);
        expenseRecyclerView.setAdapter(expenseAdapter);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public boolean isContained (ArrayList<CategorySum> cates, CategorySum c){
        for (CategorySum cate : cates)
        {

            if (cate.getIcon().equals(c.getIcon())&&cate.getName().equals(c.getName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onError(String message) {


    }

    private static DataSet dataChartExpense(Double[] amount) {
        Map<String, Integer> map = new HashMap<>();


        LineData d = new LineData();
       // int[] data = new int[] { 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9 };

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 12; index++) {
            entries.add(new Entry(index, amount[index].floatValue()));
        }

        LineDataSet set = new LineDataSet(entries, "Expense");
        set.setColor(Color.RED);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.RED);
        set.setCircleRadius(3f);
        set.setFillColor(Color.RED);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.RED);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return set;
    }
    private static DataSet dataChartIncome(Double[] amount) {
        Map<String, Integer> map = new HashMap<>();


        LineData d = new LineData();
        // int[] data = new int[] { 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9 };

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < 12; index++) {
            entries.add(new Entry(index, amount[index].floatValue()));
        }

        LineDataSet set = new LineDataSet(entries, "Income");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(3f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.GREEN);

        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        d.addDataSet(set);

        return set;
    }

    public void fillDataToChart() {
        final List<String> xLabel = new ArrayList<>();
        xLabel.add("Jan");
        xLabel.add("Feb");
        xLabel.add("Mar");
        xLabel.add("Apr");
        xLabel.add("May");
        xLabel.add("Jun");
        xLabel.add("Jul");
        xLabel.add("Aug");
        xLabel.add("Sep");
        xLabel.add("Oct");
        xLabel.add("Nov");
        xLabel.add("Dec");
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value % xLabel.size());
            }
        });


        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChartExpense(expenses));
        lineDatas.addDataSet((ILineDataSet) dataChartIncome(incomes));
        data.setData(lineDatas);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        mChart.setData(data);
        mChart.invalidate();

    }
}
