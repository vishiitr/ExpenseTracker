package com.iitr.vishal.expensetracker.Task;

import android.graphics.Color;
import android.os.AsyncTask;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Common.MoneyFormatter;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
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

public class MonthlyExpenseTask extends AsyncTask<Void, Void, List<MonthlyExpenseModel>> {
    private WeakReference<MainActivity> activityReference; // only retain a weak reference to the activity

    public MonthlyExpenseTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected List<MonthlyExpenseModel> doInBackground(Void... voids) {
        List<TransactionEntity> transactionEntities =  activityReference.get().appDatabase.transactionDao().getAllTransactions();
        ArrayList<Float> entries = new ArrayList<>();
        for (TransactionEntity item : transactionEntities) {
            entries.add(item.getAmount());
        }

        if (activityReference.get() != null)
            return activityReference.get().appDatabase.transactionDao().getMonthlyExpenditure();
        else return null;
    }

    @Override
    protected void onPostExecute(List<MonthlyExpenseModel> notes) {
        if (notes != null && notes.size() > 0) {
            int j = 0;
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<BarEntry> entries = new ArrayList<>();
            Collections.reverse(notes);
            for (MonthlyExpenseModel item : notes) {
                entries.add(new BarEntry(item.getExpenditure(), j));
                labels.add(Formatter.monthFormatter(item.getMonth_year()));
                j++;
            }

            BarChart barChart = (BarChart) activityReference.get().findViewById(R.id.barchart);
            BarDataSet bardataset = new BarDataSet(entries, "");
            bardataset.setBarSpacePercent(65f);
            bardataset.setColor(Color.WHITE);
            bardataset.setValueTextColor(Color.WHITE);
            BarData data = new BarData(labels, bardataset);
            data.setValueFormatter(new MoneyFormatter());
            barChart.setData(data); // set the data and list of lables into chart
            barChart.setDescription("");
            barChart.animateY(1000);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisLeft().setEnabled(false);

            barChart.getXAxis().setDrawGridLines(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.setPinchZoom(false);
            barChart.setDoubleTapToZoomEnabled(false);
            barChart.getLegend().setEnabled(false);
            barChart.setBackgroundColor(Color.rgb(0, 128, 128)); //set whatever color you prefer
            barChart.setDrawGridBackground(false);// this is a must

            barChart.setVisibleXRangeMaximum(6);
            barChart.moveViewToX(labels.size() - 6);
            barChart.getXAxis().setTextColor(Color.WHITE);
            barChart.setOnChartValueSelectedListener(activityReference.get());
        }
    }
}
