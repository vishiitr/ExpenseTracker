package com.iitr.vishal.expensetracker.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iitr.vishal.expensetracker.Alarm.AlarmReceiver;
import com.iitr.vishal.expensetracker.Alarm.NotificationScheduler;
import com.iitr.vishal.expensetracker.Common.CustomSwitch;
import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.R;
import com.iitr.vishal.expensetracker.db.AppDatabase;
import com.iitr.vishal.expensetracker.db.dao.ReminderDao;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        final View convertView = layoutInflater.inflate(R.layout.layout_reminder_row, null);
        holder = new ReminderAdapter.ViewHolder(convertView);
        holder.amountView = (TextView) convertView.findViewById(R.id.amount);
        holder.bankNameView = (TextView) convertView.findViewById(R.id.bankName);
        holder.dateDayView = (TextView) convertView.findViewById(R.id.date_day);
        holder.dateMonthView = (TextView) convertView.findViewById(R.id.date_month);
        holder.cardNbrView = (TextView) convertView.findViewById(R.id.cardNbr);
        holder.reminderView = (CustomSwitch) convertView.findViewById(R.id.reminderSwitch);
        holder.reminderView.setTag(holder);

        holder.reminderView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    NotificationScheduler.setReminder(
                            layoutInflater.getContext(),
                            AlarmReceiver.class,
                            ((ViewHolder)compoundButton.getTag()).reminderDate,
                            ((ViewHolder)compoundButton.getTag()).amountView.getText().toString() + " on " + ((ViewHolder)compoundButton.getTag()).bankNameView.getText().toString());
                } else {
                    NotificationScheduler.cancelReminder(layoutInflater.getContext(), AlarmReceiver.class);
                }
                AppDatabase.getAppDatabase(layoutInflater.getContext()).reminderDao().updateReminderAlarmStatus(isChecked,((ViewHolder)compoundButton.getTag()).rowId);
            }
        });
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
        holder.reminderView.setCheckedSilent(listData.get(position).isReminderSet);
        holder.rowId = listData.get(position).Id;
        holder.reminderDate = listData.get(position).reminderDate;
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
        CustomSwitch reminderView;
        long rowId;
        Date reminderDate;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
