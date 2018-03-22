package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.iitr.vishal.expensetracker.Task.MonthlyExpenseTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.List;

public class MainActivity extends Activity implements OnChartValueSelectedListener {

    public AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayList();
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        List<String> values = ((BarChart) findViewById(R.id.barchart)).getXAxis().getValues();
        String value = values.get(e.getXIndex());
        Intent intent = new Intent(MainActivity.this, MonthlyexpenseActivity.class);
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("monthName", value);

        //Add the bundle to the intent
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {

    }
}
