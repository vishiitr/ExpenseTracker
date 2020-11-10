package com.iitr.vishal.expensetracker;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.iitr.vishal.expensetracker.Adapter.ExpenseAdapter;
import com.iitr.vishal.expensetracker.Task.RecentExpenseTask;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    public RecyclerView monthsListView;
    public AppDatabase appDatabase;

    public ExpenseFragment() {
        super();
        setArguments(new Bundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        appDatabase = AppDatabase.getAppDatabase(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        monthsListView = (RecyclerView) getActivity().findViewById(R.id.months_list);

        String monthName = getArguments().getString("monthName");
        long bank_id = getArguments().getLong("bank_id",-1);
        new RecentExpenseTask(this).execute(monthName, Long.toString(bank_id));
    }

}
