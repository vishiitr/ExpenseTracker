package com.iitr.vishal.expensetracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.iitr.vishal.expensetracker.db.entity.BankEntity;

import java.util.List;

/**
 * Created by Divya on 16-03-2018.
 */
@Dao
public interface BankDao {
    @Insert
    Long insert(BankEntity repo);

    @Update
    void update(BankEntity... repos);

    @Delete
    void delete(BankEntity... repos);

    @Query("SELECT * FROM BanksNCard")
    List<BankEntity> getAllBanknCard();
}
