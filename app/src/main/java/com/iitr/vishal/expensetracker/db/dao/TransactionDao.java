package com.iitr.vishal.expensetracker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.iitr.vishal.expensetracker.Model.MonthlyExpenseModel;
import com.iitr.vishal.expensetracker.Model.MonthlyTopModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.util.List;

/**
 * Created by Divya on 16-03-2018.
 */
@Dao
public interface TransactionDao {
    @Insert
    void insert(TransactionEntity repo);

    @Update
    void update(TransactionEntity... repos);

    @Delete
    void delete(TransactionEntity... repos);

    @Query("SELECT * FROM Transactions where strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') = '2017-10' order by spent_date desc")
    List<TransactionEntity> getAllTransactions();

    @Query("SELECT * FROM Transactions where strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') like :monthName order by spent_date desc")
    List<TransactionEntity> getMonthlyTransactions(String monthName);

    @Query("SELECT * FROM Transactions order by Id desc limit 5 ")
    List<TransactionEntity> getRecentTransactions();

    @Query("SELECT strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') FROM Transactions where Id = 7529")
    List<String> getDate();

    @Query("SELECT MAX(Id) from Transactions")
    long getLastSmsId();

    @Query("SELECT SUM(amount) as expenditure, \n" +
            "       strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') as 'month_year' \n" +
            "       from Transactions group by strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') order by spent_date desc limit :range")
    List<MonthlyExpenseModel> getMonthlyExpenditure(int range);

    @Query("Select SUM(amount) as expenditure, bank_id as month_year "+
                "from Transactions where strftime('%m', spent_date / 1000, 'unixepoch' ,'localtime') =  strftime('%m',date('now')) " +
            " and strftime('%Y', spent_date / 1000, 'unixepoch' ,'localtime') =  strftime('%Y',date('now')) group by bank_id " )
    List<MonthlyExpenseModel> getCurrentMonthExpense();

    @Query("SELECT SUM(amount) as spentAmount, \n" +
            "       spent_at as spentAt from Transactions \n" +
            "       where strftime('%Y-%m', spent_date / 1000, 'unixepoch' ,'localtime') like :monthName  group by trim(spent_at) order by spentAmount desc limit 5")
    List<MonthlyTopModel> getMonthlyTopExpenditure(String monthName);
}
