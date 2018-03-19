package com.iitr.vishal.expensetracker.Task;

import android.os.AsyncTask;

import com.iitr.vishal.expensetracker.Adapter.ExpenseAdapter;
import com.iitr.vishal.expensetracker.ExpenseFragment;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 18-03-2018.
 */

public class RecentExpenseTask extends AsyncTask<Void, Void, ArrayList<TransactionEntity>> {
    private WeakReference<ExpenseFragment> activityReference; // only retain a weak reference to the activity

    public RecentExpenseTask(ExpenseFragment context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected ArrayList<TransactionEntity> doInBackground(Void... voids) {
        if (activityReference.get() != null)
        {
            List<TransactionEntity> list =  activityReference.get().appDatabase.transactionDao().getRecentTransactions();
            ArrayList<TransactionEntity> array = new ArrayList<>(list.size());
            array.addAll(list);
            return array;
        }
        return null;
    }
    protected void onPostExecute(ArrayList<TransactionEntity> notes) {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(activityReference.get().getActivity(),notes);
        activityReference.get().monthsListView.setAdapter(expenseAdapter);
    }
}
