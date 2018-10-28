package com.iitr.vishal.expensetracker.Task;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.iitr.vishal.expensetracker.Common.BarChartCustomRenderer;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Common.MoneyFormatter;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.MonthlyexpenseActivity;
import com.iitr.vishal.expensetracker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Divya on 17-03-2018.
 */

public class ExpenseChartTask extends AsyncTask<Integer, Void, List<MonthlyExpenseModel>> {
    private WeakReference<MainActivity> activityReference; // only retain a weak reference to the activity
    private  List<MonthlyExpenseModel> monthlyExpenses;
    int range = 6;

    public ExpenseChartTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected List<MonthlyExpenseModel> doInBackground(Integer... params) {
        //List<TransactionEntity> transactionEntities =  activityReference.get().appDatabase.transactionDao().getAllTransactions();
        //ArrayList<Float> entries = new ArrayList<>();
        //for (TransactionEntity item : transactionEntities) {
        //    entries.add(item.getAmount());
        //}
        range = params[0];
        if (activityReference.get() != null)
            return activityReference.get().appDatabase.transactionDao().getMonthlyExpenditure(range);
        else return null;
    }

    @Override
    protected void onPostExecute(List<MonthlyExpenseModel> notes) {
        monthlyExpenses = notes;
        CreateChart();


    }

    private void CreateChart() {
        Collections.reverse(monthlyExpenses);
        final String[] labels = new String[monthlyExpenses.size()];
        int j = 0;
        for (MonthlyExpenseModel item : monthlyExpenses) {
            labels[j] = Formatter.monthFormatter(item.getMonth_year());
            j++;
        }

        BarChart mChart = (BarChart) activityReference.get().findViewById(R.id.barchart);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundResource(R.drawable.expense_background_gradient);
        //mChart.setBackgroundColor(Color.rgb(2, 128, 144));
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.animateY(1000);
        mChart.getLegend().setEnabled(false);
        mChart.setExtraTopOffset(80f);
        BarChartCustomRenderer customRenderer = new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        mChart.setRenderer(customRenderer);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                MonthlyExpenseModel lastMonth = monthlyExpenses.get((int) e.getX());
                SetTextViews(lastMonth);

                String value = labels[(int) e.getX()];
                Intent intent = new Intent(activityReference.get(), MonthlyexpenseActivity.class);
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("monthName", value);

                //Add the bundle to the intent
                intent.putExtras(bundle);
                activityReference.get().startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(range);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<range)
                    return labels[(int) value];
                else
                    return "";
            }
        });
        if(range>6)
        {
            xAxis.setLabelRotationAngle(-45);
            mChart.setExtraBottomOffset(15f);
        }
        else
        {
            xAxis.setLabelRotationAngle(0);
            mChart.setExtraBottomOffset(2f);
        }

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        setData(mChart);
        //mChart.highlightValue(monthlyExpenses.size()-1,0,false);
        MonthlyExpenseModel lastMonth = monthlyExpenses.get(monthlyExpenses.size()-1);
        SetTextViews(lastMonth);
    }

    private void setData(BarChart mChart) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int j = 0;

        for (MonthlyExpenseModel item : monthlyExpenses) {
            yVals1.add(new BarEntry(j, item.getExpenditure()));
            j++;
        }

        int[] barColors = new int[monthlyExpenses.size()];
        Arrays.fill(barColors,getColorWithAlpha(Color.WHITE,0.2f));
        barColors[monthlyExpenses.size()-1] = Color.WHITE;


        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setValueFormatter(new MoneyFormatter());
        set1.setDrawIcons(false);
        set1.setColors(barColors);
        set1.setValueTextColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setHighLightAlpha(255);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(12f);
        data.setBarWidth(0.33f);
        mChart.setData(data);

         //Float.toString(lastMonth.getExpenditure()));
    }

    private int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    private void SetTextViews(MonthlyExpenseModel monthlyExpenseModel)
    {
        TextView monthNameView = (TextView) activityReference.get().findViewById(R.id.monthName);
        monthNameView.setText(Formatter.monthFormatter(monthlyExpenseModel.getMonth_year()));
        TextView expenseView = (TextView) activityReference.get().findViewById(R.id.expenseAmount);
        expenseView.setText("₹" + String.format("%,.0f", (double)monthlyExpenseModel.getExpenditure()));
    }

}
