package com.iitr.vishal.expensetracker.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.iitr.vishal.expensetracker.Common.Logger;
import com.iitr.vishal.expensetracker.MainActivity;
import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.List;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

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
                    {
                        try
                        {
                            NotificationScheduler.setReminder(context, AlarmReceiver.class, reminder.reminderDate, "Total Due: ₹ "+ reminder.amount + " on " + reminder.bankName.replace("-"," Credit Card t"));
                        }
                        catch (Exception ex)
                        {
                            Logger.getInstance(context).info(ex.getMessage());
                        }
                    }

                }
                LocalData localData = new LocalData(context);
                //NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");
        //Logger.getInstance(context).info("On Receive succesfykk");
        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "Bill Reminder", intent.getStringExtra("reminderMsg"));

    }
}


