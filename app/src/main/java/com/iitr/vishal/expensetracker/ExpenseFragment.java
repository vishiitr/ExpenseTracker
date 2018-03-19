package com.iitr.vishal.expensetracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iitr.vishal.expensetracker.Task.RecentExpenseTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    public ListView monthsListView;
    public AppDatabase appDatabase;

    public ExpenseFragment() {
        appDatabase = AppDatabase.getAppDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        monthsListView = (ListView) view.findViewById(R.id.months_list);

        new RecentExpenseTask(this).execute();

        return view;
    }

}
