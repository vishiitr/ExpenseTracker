package com.iitr.vishal.expensetracker.db.dao;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.util.List;

/**
 * Created by Divya on 27-03-2018.
 */

public interface ReminderDao {
    @Insert
    void insert(TransactionEntity repo);

    @Query("SELECT * FROM Transactions where strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') = '2017-10' order by spent_date desc")
    List<TransactionEntity> getAllReminders();
}
