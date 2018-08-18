package com.iitr.vishal.expensetracker.Task;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Common.MoneyFormatter;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.Model.MonthlyTopModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.MonthlyexpenseActivity;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        if (notes != null && notes.size() > 0) {
            int j = 0;
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<BarEntry> entries = new ArrayList<>();
            Collections.reverse(notes);
            for (MonthlyTopModel item : notes) {
                entries.add(new BarEntry(item.spentAmount, j));
                labels.add(item.spentAt);
                j++;
            }

            HorizontalBarChart barChart = (HorizontalBarChart) activityReference.get().findViewById(R.id.horchart);
            BarDataSet bardataset = new BarDataSet(entries, "");
            bardataset.setBarSpacePercent(30f);
            bardataset.setColor(Color.WHITE);
            bardataset.setValueTextColor(Color.WHITE);
            bardataset.setValueTextSize(12);
            BarData data = new BarData(labels, bardataset);
            data.setValueFormatter(new MoneyFormatter());
            barChart.setData(data); // set the data and list of lables into chart
            barChart.setDescription("");
            barChart.animateY(1000);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisLeft().setEnabled(false);

            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setAxisLineColor(Color.rgb(230,230,250));
            barChart.getXAxis().setAxisLineWidth(2f);

            barChart.setExtraLeftOffset(20);
            barChart.setExtraRightOffset(30);

            barChart.setPinchZoom(false);
            barChart.setDoubleTapToZoomEnabled(false);
            barChart.getLegend().setEnabled(false);
            barChart.setBackgroundColor(Color.rgb(170, 32, 108)); //set whatever color you prefer
            barChart.setDrawGridBackground(false);// this is a must


            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setTextSize(12);
            xAxis.setLabelsToSkip(0);
        }
    }
}
