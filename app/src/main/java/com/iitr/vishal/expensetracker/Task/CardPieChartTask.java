package com.iitr.vishal.expensetracker.Task;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.iitr.vishal.expensetracker.CardExpenseActivity;
import com.iitr.vishal.expensetracker.Common.MoneyFormatter;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.Model.MonthlyTopModel;
import com.iitr.vishal.expensetracker.MonthlyexpenseActivity;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Divya on 26-12-2018.
 */

public class CardPieChartTask extends AsyncTask<String, Void, List<MonthlyExpenseModel>> {
    private WeakReference<Context> activityReference; // only retain a weak reference to the activity
    AppDatabase appDatabase;
    PieChart pieChart;

    public CardPieChartTask(Context context, AppDatabase appDatabase, PieChart pieChart) {
        activityReference = new WeakReference<>(context);
        this.appDatabase = appDatabase;
        this.pieChart = pieChart;
    }

    @Override
    protected List<MonthlyExpenseModel> doInBackground(String... params) {
        if (activityReference.get() != null) {
            List<MonthlyExpenseModel> monthlyExpenseModels = appDatabase.transactionDao().getMonthlyTransactionsSumByCard(params[0], params[1]);
            if (monthlyExpenseModels.size() > 5) {
                MonthlyExpenseModel tempMonthlyExpense = new MonthlyExpenseModel();
                tempMonthlyExpense.setMonth_year("Others");
                for (int i = 5; i < monthlyExpenseModels.size(); i++) {
                    tempMonthlyExpense.setExpenditure(tempMonthlyExpense.getExpenditure() + monthlyExpenseModels.get(i).getExpenditure());
                }

                List<MonthlyExpenseModel> finalList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    finalList.add(monthlyExpenseModels.get(i));
                }
                finalList.add(tempMonthlyExpense);
                return finalList;
            } else return monthlyExpenseModels;
        } else return null;
    }

    @Override
    protected void onPostExecute(List<MonthlyExpenseModel> notes) {
        CreateChart(notes);
    }

    private void CreateChart(List<MonthlyExpenseModel> monthlyExpenses) {

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //chart.setCenterTextTypeface(tfLight);
        //pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(R.color.horizontalChartBackGround);

        //pieChart.setTransparentCircleColor(Color.WHITE);
        //pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(10f);

        //pieChart.setDrawCenterText(true);

        //pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        //pieChart.setUnit(" ₹");
        //pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        //pieChart.setOnChartValueSelectedListener(this);

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(10);

        pieChart.animateY(1000, Easing.EasingOption.EaseOutBounce);
        //pieChart.spin(2000, 0, 360,Easing.EasingOption.EaseInOutBounce);

        /*Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
*/
        // entry label styling
        pieChart.getLegend().setEnabled(false);

        pieChart.setEntryLabelColor(Color.WHITE);
        //pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(12f);


        setData(monthlyExpenses);
    }

    private void setData(List<MonthlyExpenseModel> monthlyExpenses) {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        int j = 0;

        for (MonthlyExpenseModel item : monthlyExpenses) {
            yVals1.add(new PieEntry(item.getExpenditure(), item.getMonth_year()));
            j++;
        }

        PieDataSet set1 = new PieDataSet(yVals1, "");


        set1.setDrawIcons(false);

        set1.setSliceSpace(3f);
        set1.setIconsOffset(new MPPointF(0, 40));
        set1.setSelectionShift(5f);
        set1.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        set1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set1.setValueLinePart1OffsetPercentage(50f);
        set1.setValueLineVariableLength(true);
        set1.setValueLinePart1Length(0.7f);
        //set1.setValueLinePart2Length(.6f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

        //for (int c : ColorTemplate.MATERIAL_COLORS)
        //    colors.add(c);

        //colors.add(ColorTemplate.getHoloBlue());

        colors.add(ColorTemplate.rgb("#003f5c"));
        colors.add(ColorTemplate.rgb("#444e86"));
        colors.add(ColorTemplate.rgb("#955196"));
        colors.add(ColorTemplate.rgb("#dd5182"));
        colors.add(ColorTemplate.rgb("#ff6e54"));
        colors.add(ColorTemplate.rgb("#ffa600"));

        set1.setColors(colors);
        //dataSet.setSelectionShift(0f);
        set1.setValueFormatter(new MoneyFormatter());
        PieData data = new PieData(set1);
        //data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        pieChart.setData(data);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
}