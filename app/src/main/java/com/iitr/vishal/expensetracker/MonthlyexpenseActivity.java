package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Task.MonthlyHorChartTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.text.Normalizer;

public class MonthlyexpenseActivity extends Activity {
    public AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyexpense);

        Bundle bundleIntent = getIntent().getExtras();

        //Extract the data…
        String monthName = bundleIntent.getString("monthName");

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.recent_expenses);
        Bundle bundle = fragment.getArguments();
        bundle.putString("monthName", Formatter.monthDbFormatter(monthName));

        DisplayChart(monthName);

        TextView monthText = findViewById(R.id.monthName);
        TextView staticText = findViewById(R.id.staticText);
        monthText.setText(monthName.split("\'")[0]);
        monthText.setBackgroundColor(Color.rgb(170, 32, 108));
        monthText.setTextColor(Color.WHITE);
    }


    public void DisplayChart(String monthName) {
        appDatabase = AppDatabase.getAppDatabase(MonthlyexpenseActivity.this);
        new MonthlyHorChartTask(this).execute(Formatter.monthDbFormatter(monthName));
    }
}
