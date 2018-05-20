package com.iitr.vishal.expensetracker.Task;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
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

public class ExpenseChartTask extends AsyncTask<Integer, Void, List<MonthlyExpenseModel>> {
    private WeakReference<MainActivity> activityReference; // only retain a weak reference to the activity

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
            barChart.setPinchZoom(false);
            barChart.setDoubleTapToZoomEnabled(false);
            barChart.getLegend().setEnabled(false);
            barChart.setBackgroundColor(Color.rgb(2, 128, 144)); //set whatever color you prefer
            barChart.setDrawGridBackground(false);// this is a must

            //barChart.setVisibleXRangeMaximum(range);
            //barChart.moveViewToX(labels.size() - range);

            //barChart.centerViewTo(labels.size(), notes.get(labels.size() - 6).getExpenditure(), YAxis.AxisDependency.LEFT);
            //barChart.zoom(labels.size()/labels.size(),500000/notes.get(labels.size() - 6).getExpenditure(),1,1);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setTextColor(Color.WHITE);
            xAxis.setTextSize(12);
            xAxis.setLabelsToSkip(0);
            if(range>6)
            {
                xAxis.setLabelRotationAngle(-45);
                barChart.setExtraBottomOffset(28f);
            }
            else
            {
                xAxis.setLabelRotationAngle(0);
                barChart.setExtraBottomOffset(-18f);
            }

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    List<String> values = ((BarChart) activityReference.get().findViewById(R.id.barchart)).getXAxis().getValues();
                    String value = values.get(e.getXIndex());
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

        }
    }
}
