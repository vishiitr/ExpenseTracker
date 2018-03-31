package com.iitr.vishal.expensetracker;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.Task.RecentExpenseTask;
import com.iitr.vishal.expensetracker.Task.ReminderTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends Fragment {

    public RecyclerView reminderListView;
    public AppDatabase appDatabase;

    public ReminderFragment() {
        super();
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reminder, container, false);
        appDatabase = AppDatabase.getAppDatabase(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reminderListView = (RecyclerView) getActivity().findViewById(R.id.reminders_list);
        new ReminderTask(this).execute();
    }

}
