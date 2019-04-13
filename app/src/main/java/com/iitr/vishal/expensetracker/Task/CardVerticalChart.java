package com.iitr.vishal.expensetracker.Task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Divya on 26-12-2018.
 */

public class CardVerticalChart extends AsyncTask<String, Void, List<MonthlyExpenseModel>> {
    private WeakReference<Context> activityReference; // only retain a weak reference to the activity
    private List<MonthlyExpenseModel> monthlyExpenses;
    AppDatabase appDatabase;
    BarChart mChart;
    int range = 12;

    public CardVerticalChart(Context context, AppDatabase appDatabase, BarChart barChart) {
        activityReference = new WeakReference<>(context);
        this.appDatabase = appDatabase;
        this.mChart = barChart;
    }

    @Override
    protected List<MonthlyExpenseModel> doInBackground(String... params) {
        if (activityReference.get() != null)
            return appDatabase.transactionDao().getMonthlyExpenditureByCard(range, params[0]);
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

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        //mChart.setBackgroundResource(R.drawable.expense_background_gradient);
        mChart.setBackgroundResource(R.color.cardHorizontalChartBackGround);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.animateY(1000);
        mChart.getLegend().setEnabled(false);
        //mChart.setExtraTopOffset(80f);
        BarChartCustomRenderer customRenderer = new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        mChart.setRenderer(customRenderer);


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
                if (value < range)
                    return labels[(int) value];
                else
                    return "";
            }
        });

        xAxis.setLabelRotationAngle(0);
        mChart.setExtraBottomOffset(2f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        setData(mChart);
        mChart.setHighlightFullBarEnabled(false);
        //mChart.highlightValue(monthlyExpenses.size()-1,0,false);
    }

    private void setData(BarChart mChart) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int j = 0;

        for (MonthlyExpenseModel item : monthlyExpenses) {
            yVals1.add(new BarEntry(j, item.getExpenditure()));
            j++;
        }

        int[] barColors = new int[monthlyExpenses.size()];
        Arrays.fill(barColors, getColorWithAlpha(Color.WHITE, 0.2f));
        barColors[monthlyExpenses.size() - 1] = Color.WHITE;


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
}