package com.iitr.vishal.expensetracker.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.List;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");

                AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
                List<ReminderModel> reminders = appDatabase.reminderDao().getAllReminders();
                for (ReminderModel reminder : reminders) {
                    if (reminder.isReminderSet)
                        NotificationScheduler.setReminder(context, AlarmReceiver.class, reminder.reminderDate, "Total Due: ₹ "+ reminder.amount + " on " + reminder.bankName.replace("-"," Credit Card t"));
                }
                LocalData localData = new LocalData(context);
                //NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "Bill Reminder", intent.getStringExtra("reminderMsg"));

    }
}


