package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>  {
    private ArrayList<TransactionEntity> listData;
    private LayoutInflater layoutInflater;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MMM-yyyy");


    public ExpenseAdapter(Context aContext, ArrayList<TransactionEntity> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
       // if (convertView == null) {
           View convertView = layoutInflater.inflate(R.layout.layout_expense_row, null);
            holder = new ViewHolder(convertView);
            holder.amountView = (TextView) convertView.findViewById(R.id.amount);
            holder.spentAtView = (TextView) convertView.findViewById(R.id.spentAt);
            holder.dateView = (TextView) convertView.findViewById(R.id.date);
          //  convertView.setTag(holder);
        //} else {
          //  holder = (ViewHolder) convertView.getTag();
        //}


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amountView.setText("₹ " + Float.toString(listData.get(position).getAmount()));
        holder.spentAtView.setText(listData.get(position).getSpentAt());
        holder.dateView.setText(simpleDate.format(listData.get(position).getSpentDate()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView amountView;
        TextView spentAtView;
        TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}