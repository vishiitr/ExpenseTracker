package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.stetho.Stetho;
import com.github.mikephil.charting.charts.BarChart;
import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Task.BalanceCardTask;
import com.iitr.vishal.expensetracker.Task.ExpenseChartTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;


public class MainActivity extends Activity {

    public AppDatabase appDatabase;
    private FloatingActionButton monthFab, yearFab;
    LinearLayout monthLayout, yearLayout;
    private boolean is6Month = true;
    public RecyclerView balanceCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        displayList();

        monthFab = findViewById(R.id.monthFab);
        monthLayout = findViewById(R.id.monthSelection);
        yearLayout = findViewById(R.id.yearSelection);
        yearLayout.setVisibility(View.INVISIBLE);
        monthFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReDrawChart();
            }
        });

        balanceCardView = (RecyclerView)findViewById(R.id.balance_recycler_view);
        new BalanceCardTask(this).execute();
    }

    private void displayList() { // initialize database instance
        appDatabase = AppDatabase.getAppDatabase(MainActivity.this);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.recent_expenses);
        Bundle bundle = fragment.getArguments();
        bundle.putString("monthName", "");

        //Draw chart
        new ExpenseChartTask(this).execute(6);
    }

    private void ReDrawChart() {
        if (is6Month) {
            is6Month = false;
            monthLayout.setVisibility(View.GONE);
            yearLayout.setVisibility(View.VISIBLE);

            //Draw chart
            new ExpenseChartTask(this).execute(12);
        } else {
            is6Month = true;
            monthLayout.setVisibility(View.VISIBLE);
            yearLayout.setVisibility(View.GONE);
            new ExpenseChartTask(this).execute(6);
        }
    }
}

