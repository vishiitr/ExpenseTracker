package com.iitr.vishal.expensetracker.Task;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iitr.vishal.expensetracker.Adapter.BalanceCardAdapter;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.CardBalanceModel;
import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.db.entity.CardBalanceEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 28-10-2018.
 */

public class BalanceCardTask extends AsyncTask<String, Void, List<CardBalanceModel>> {
    private WeakReference<MainActivity> activityReference; // only retain a weak reference to the activity
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public BalanceCardTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected List<CardBalanceModel> doInBackground(String... params) {
        /*if (activityReference.get() != null) {
            ArrayList<String> a =  new ArrayList<String>();
            a.add("Vishal");            a.add("Bansal");
            return  a;

        }
        return null;*/
        if (activityReference.get() != null) {
            List<CardBalanceModel> cardBalanceModels = activityReference.get().appDatabase.cardBalanceDao().getAllCardBalance();
            List<MonthlyExpenseModel> monthlyExpenseModels = activityReference.get().appDatabase.transactionDao().getCurrentMonthExpense();
            for (int i = 0; i < cardBalanceModels.size(); i++) {
                CardBalanceModel item = cardBalanceModels.get(i);

                for (MonthlyExpenseModel monthlyExpenseModel : monthlyExpenseModels) {
                    if (monthlyExpenseModel.getMonth_year().equals( Long.toString(item.bank_id))) {
                        item.monthlySpent = monthlyExpenseModel.getExpenditure();
                        break;
                    }
                }
            }

            return cardBalanceModels;
        } else
            return new ArrayList<CardBalanceModel>();
    }

    protected void onPostExecute(List<CardBalanceModel> notes) {
        BalanceCardAdapter balanceCardAdapter = new BalanceCardAdapter(activityReference.get(), notes);
        mLayoutManager = new LinearLayoutManager(activityReference.get().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = activityReference.get().balanceCardView;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(balanceCardAdapter);
    }
}
