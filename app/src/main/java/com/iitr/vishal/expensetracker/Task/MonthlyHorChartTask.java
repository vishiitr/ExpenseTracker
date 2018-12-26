package com.iitr.vishal.expensetracker.Task;

import android.graphics.Color;
import android.os.AsyncTask;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.iitr.vishal.expensetracker.Common.MoneyFormatter;
import com.iitr.vishal.expensetracker.Model.MonthlyTopModel;
import com.iitr.vishal.expensetracker.MonthlyexpenseActivity;
import com.iitr.vishal.expensetracker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Divya on 17-03-2018.
 */

public class MonthlyHorChartTask extends AsyncTask<String, Void, List<MonthlyTopModel>> {
    private WeakReference<MonthlyexpenseActivity> activityReference; // only retain a weak reference to the activity

    public MonthlyHorChartTask(MonthlyexpenseActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected List<MonthlyTopModel> doInBackground(String ... params) {
        if (activityReference.get() != null)
            return activityReference.get().appDatabase.transactionDao().getMonthlyTopExpenditure(params[0]);
        else return null;
    }

    @Override
    protected void onPostExecute(List<MonthlyTopModel> notes) {
        CreateChart(notes);
    }

    private void CreateChart(List<MonthlyTopModel> monthlyExpenses) {
        Collections.reverse(monthlyExpenses);
        final String[] labels = new String[monthlyExpenses.size()];
        int j = 0;
        for (MonthlyTopModel item : monthlyExpenses) {
            labels[j] = item.spentAt;
            j++;
        }

        HorizontalBarChart mChart = (HorizontalBarChart) activityReference.get().findViewById(R.id.horchart);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(activityReference.get().getResources().getColor(R.color.horizontalChartBackGround));
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.animateY(1000);
        mChart.getLegend().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.rgb(230,230,250));
        xAxis.setAxisLineWidth(2f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int) value];
            }
        });


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);
        rightAxis.setSpaceTop(15f);

        //mChart.highlightValue(monthlyExpenses.size()-1,0,false);
        setData(mChart, monthlyExpenses);
    }

    private void setData(BarChart mChart, List<MonthlyTopModel> monthlyExpenses) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int j = 0;

        for (MonthlyTopModel item : monthlyExpenses) {
            yVals1.add(new BarEntry(j, item.spentAmount));
            j++;
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setValueFormatter(new MoneyFormatter());
        set1.setDrawIcons(false);
        set1.setColor(Color.WHITE);
        set1.setValueTextColor(Color.WHITE);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(12f);
        data.setBarWidth(0.4f);
        mChart.setData(data);
    }


}
