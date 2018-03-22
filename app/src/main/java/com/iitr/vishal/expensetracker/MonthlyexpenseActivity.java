package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.iitr.vishal.expensetracker.Common.Formatter;

import java.text.Normalizer;

public class MonthlyexpenseActivity extends Activity {

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
    }
}
