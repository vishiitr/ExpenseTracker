package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Divya on 18-03-2018.
 */

public class ExpenseAdapter extends BaseAdapter {
    private ArrayList<TransactionEntity> listData;
    private LayoutInflater layoutInflater;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MMM-yyyy");
    public ExpenseAdapter(Context aContext, ArrayList<TransactionEntity> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_expense_row, null);
            holder = new ViewHolder();
            holder.amountView = (TextView) convertView.findViewById(R.id.amount);
            holder.spentAtView = (TextView) convertView.findViewById(R.id.spentAt);
            holder.dateView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.amountView.setText("₹ " + Float.toString(listData.get(position).getAmount()));
        holder.spentAtView.setText(listData.get(position).getSpentAt());
        holder.dateView.setText(simpleDate.format(listData.get(position).getSpentDate()));
        return convertView;
    }

    static class ViewHolder {
        TextView amountView;
        TextView spentAtView;
        TextView dateView;
    }
}