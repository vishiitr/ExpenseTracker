package com.iitr.vishal.expensetracker.Task;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iitr.vishal.expensetracker.Adapter.ExpenseAdapter;
import com.iitr.vishal.expensetracker.Adapter.ReminderAdapter;
import com.iitr.vishal.expensetracker.ExpenseFragment;
import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.ReminderFragment;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 27-03-2018.
 */

public class ReminderTask extends AsyncTask<String, Void, ArrayList<ReminderModel>> {
    private WeakReference<ReminderFragment> activityReference; // only retain a weak reference to the activity
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public ReminderTask(ReminderFragment context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected ArrayList<ReminderModel> doInBackground(String... params) {
        if (activityReference.get() != null) {
            List<ReminderModel> list;
            list = activityReference.get().appDatabase.reminderDao().getAllReminders();

            ArrayList<ReminderModel> array = new ArrayList<>(list.size());
            array.addAll(list);
            return array;
        }
        return null;
    }

    protected void onPostExecute(ArrayList<ReminderModel> notes) {

        mLayoutManager = new LinearLayoutManager(activityReference.get().getActivity());
        mRecyclerView = activityReference.get().reminderListView;
        if(notes.size()==0)
        {
            mRecyclerView.setVisibility(View.GONE);
            activityReference.get().emptyTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            ReminderAdapter expenseAdapter = new ReminderAdapter(activityReference.get().getActivity(), notes);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(expenseAdapter);
        }


    }
}
