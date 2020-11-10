package com.iitr.vishal.expensetracker.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.iitr.vishal.expensetracker.db.dao.BankDao;
import com.iitr.vishal.expensetracker.db.dao.CardBalanceDao;
import com.iitr.vishal.expensetracker.db.dao.ReminderDao;
import com.iitr.vishal.expensetracker.db.dao.TransactionDao;
import com.iitr.vishal.expensetracker.db.entity.BankEntity;
import com.iitr.vishal.expensetracker.db.entity.CardBalanceEntity;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

/**
 * Created by Divya on 17-03-2018.
 */

@Database(entities = {BankEntity.class, TransactionEntity.class, ReminderEntity.class, CardBalanceEntity.class}, version = 3)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract BankDao bankDao();

    public abstract TransactionDao transactionDao();

    public abstract ReminderDao reminderDao();

    public abstract CardBalanceDao cardBalanceDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "transcations-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries().addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3)
                            .build();
        }
        return INSTANCE;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `CardBalance`(`bank_id` INTEGER NOT NULL, "
                    + "`balance` REAL NOT NULL, `last_transcation_date` INTEGER, PRIMARY KEY(`bank_id`),"+"" +
                    "FOREIGN KEY(`bank_id`) REFERENCES `BanksNCard`(`Id`) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX index_CardBalance_bank_id ON CardBalance (bank_id)");

        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `Transactions` ADD `is_deleted` INTEGER NOT NULL default 0");
        }
    };
    public static void destroyInstance() {
        INSTANCE = null;
    }
}