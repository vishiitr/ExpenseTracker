package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.stetho.Stetho;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.iitr.vishal.expensetracker.Task.MonthlyExpenseTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity  {

    public AppDatabase appDatabase;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        displayList();

        spinner = (Spinner) findViewById(R.id.travelType_spinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("6 Month");
        categories.add("1 Year");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        findViewById(R.id.spinnerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    private void displayList() { // initialize database instance
        appDatabase = AppDatabase.getAppDatabase(MainActivity.this);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.recent_expenses);
        Bundle bundle = fragment.getArguments();
        bundle.putString("monthName", "");

        //Draw chart
        new MonthlyExpenseTask(this).execute();
    }


}
