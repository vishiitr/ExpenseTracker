package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Divya on 27-03-2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>  {

    private ArrayList<ReminderModel> listData;
    private LayoutInflater layoutInflater;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MMM-yyyy");


    public ReminderAdapter(Context aContext, ArrayList<ReminderModel> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReminderAdapter.ViewHolder holder;
        // if (convertView == null) {
        View convertView = layoutInflater.inflate(R.layout.layout_reminder_row, null);
        holder = new ReminderAdapter.ViewHolder(convertView);
        holder.amountView = (TextView) convertView.findViewById(R.id.amount);
        holder.bankNameView = (TextView) convertView.findViewById(R.id.bankName);
        holder.dateDayView = (TextView) convertView.findViewById(R.id.date_day);
        holder.dateMonthView = (TextView) convertView.findViewById(R.id.date_month);
        holder.cardNbrView = (TextView) convertView.findViewById(R.id.cardNbr);

        //  convertView.setTag(holder);
        //} else {
        //  holder = (ViewHolder) convertView.getTag();
        //}


        return holder;
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int position) {
        String[] date = simpleDate.format(listData.get(position).reminderDate).split("-");
        String[] cardDetails = listData.get(position).bankName.split("-");
        holder.amountView.setText("Total Due: ₹ " + Float.toString(listData.get(position).amount));
        holder.bankNameView.setText(cardDetails[0] + " Credit Card");
        holder.dateDayView.setText(date[0]);
        holder.dateMonthView.setText(date[1]);
        holder.cardNbrView.setText(cardDetails[1]);
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
        TextView bankNameView;
        TextView dateDayView;
        TextView dateMonthView;
        TextView cardNbrView;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
