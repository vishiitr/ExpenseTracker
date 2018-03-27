package com.iitr.vishal.expensetracker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.iitr.vishal.expensetracker.db.dao.BankDao;
import com.iitr.vishal.expensetracker.db.dao.ReminderDao;
import com.iitr.vishal.expensetracker.db.dao.TransactionDao;
import com.iitr.vishal.expensetracker.db.entity.BankEntity;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

/**
 * Created by Divya on 17-03-2018.
 */

@Database(entities = {BankEntity.class, TransactionEntity.class, ReminderEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract BankDao bankDao();

    public abstract TransactionDao transactionDao();

    public abstract ReminderDao reminderDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "transcations-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}