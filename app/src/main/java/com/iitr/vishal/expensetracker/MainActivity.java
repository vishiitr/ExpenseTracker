package com.iitr.vishal.expensetracker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.iitr.vishal.expensetracker.Task.MonthlyExpenseTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

public class MainActivity extends FragmentActivity {

    public AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayList();
    }

    private void displayList() { // initialize database instance
        appDatabase = AppDatabase.getAppDatabase(MainActivity.this);

        //Draw chart
        new MonthlyExpenseTask(this).execute();
    }


}
