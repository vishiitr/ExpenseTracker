package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.Task.CardPieChartTask;
import com.iitr.vishal.expensetracker.Task.CardVerticalChart;
import com.iitr.vishal.expensetracker.db.AppDatabase;

/**
 * Created by Divya on 26-12-2018.
 */

public class MonthlyCardAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    public AppDatabase appDatabase;
    String monthName;
    String bank_id;
    //private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

    public MonthlyCardAdapter(Context context, AppDatabase appDatabase, String monthName, String bank_id) {
        this.context = context;
        this.appDatabase = appDatabase;
        this.monthName = monthName;
        this.bank_id = bank_id;
    }

    @Override
    public int getCount() {
        return 2;//images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_monthly_card, null);
        LinearLayout cardChartPieNHor = (LinearLayout) view.findViewById(R.id.cardChartPieNHor);
        if (position == 1) {
            PieChart pieChart = new PieChart(context);
            int pieId = View.generateViewId();
            pieChart.setId(pieId);
            pieChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            cardChartPieNHor.addView(pieChart);
            new CardPieChartTask(context, appDatabase, pieChart).execute(monthName, bank_id);

        } else {
            BarChart barChart = new BarChart(context);
            int barId = View.generateViewId();
            barChart.setId(barId);
            barChart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            cardChartPieNHor.addView(barChart);
            new CardVerticalChart(context, appDatabase, barChart).execute(bank_id);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
