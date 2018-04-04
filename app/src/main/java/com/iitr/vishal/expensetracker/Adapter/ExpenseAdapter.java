package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Divya on 18-03-2018.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    String[] Colors = {"#64dd17", "#00b8d4", "#304ffe", "#fea904", "#01c653", "#d30100"};
    private ArrayList<TransactionEntity> listData;
    private LayoutInflater layoutInflater;
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MMM-yyyy");
    Random rand = new Random();


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
        holder.shoppingView = (ImageView) convertView.findViewById(R.id.shoppingIcon);
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
        holder.shoppingView.setBackgroundColor(Color.parseColor(Colors[rand.nextInt(6)]));

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
        ImageView shoppingView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}