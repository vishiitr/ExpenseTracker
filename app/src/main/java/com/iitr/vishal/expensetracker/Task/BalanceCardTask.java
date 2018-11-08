package com.iitr.vishal.expensetracker.Task;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iitr.vishal.expensetracker.Adapter.BalanceCardAdapter;
import com.iitr.vishal.expensetracker.MainActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 28-10-2018.
 */

public class BalanceCardTask extends AsyncTask<String, Void, ArrayList<String>> {
    private WeakReference<MainActivity> activityReference; // only retain a weak reference to the activity
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public BalanceCardTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if (activityReference.get() != null) {
            ArrayList<String> a =  new ArrayList<String>();
            a.add("Vishal");            a.add("Bansal");
            return  a;

        }
        return null;
    }

    protected void onPostExecute(ArrayList<String> notes) {
        BalanceCardAdapter balanceCardAdapter = new BalanceCardAdapter(activityReference.get().getApplicationContext(),notes);
        mLayoutManager = new LinearLayoutManager(activityReference.get().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = activityReference.get().balanceCardView;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(balanceCardAdapter);
    }
}
