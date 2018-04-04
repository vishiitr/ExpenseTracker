package com.iitr.vishal.expensetracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;

import java.util.List;

/**
 * Created by Divya on 27-03-2018.
 */

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReminderEntity repo);

    @Query("SELECT Reminders.Id,Reminders.reminder_date as reminderDate, Reminders.amount, BanksNCard.bank_name || '-' || BanksNCard.card_nbr  as bankName " +
                "FROM Reminders INNER JOIN BanksNCard ON Reminders.bank_id = BanksNCard.Id where reminder_date/1000 > (julianday('now') - 2440587.5)*86400.0 order by reminder_date asc")
    List<ReminderModel> getAllReminders();
}
