package com.iitr.vishal.expensetracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.iitr.vishal.expensetracker.Model.CardBalanceModel;
import com.iitr.vishal.expensetracker.Model.ReminderModel;
import com.iitr.vishal.expensetracker.db.entity.CardBalanceEntity;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Divya on 10-11-2018.
 */

@Dao
public interface CardBalanceDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(CardBalanceEntity repo);

    @Query("Select * from CardBalance where bank_id = :bank_id")
    List<CardBalanceEntity> getBalanceByBankCardId(long bank_id);

    @Query("Select *, 0 AS monthlySpent from CardBalance INNER JOIN BanksNCard ON CardBalance.bank_id = BanksNCard.Id order by last_transcation_date desc")
    List<CardBalanceModel> getAllCardBalance();

    @Query("UPDATE CardBalance SET balance = :balance, last_transcation_date = :lastTranscationDate  WHERE bank_id = :bankId")
    void updateReminderAlarmStatus(float balance, Date lastTranscationDate, long bankId);

    @Query("delete from  CardBalance")
    void deleteAll();


}
