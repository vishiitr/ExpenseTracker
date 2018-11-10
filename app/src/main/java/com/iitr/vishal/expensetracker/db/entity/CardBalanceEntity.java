package com.iitr.vishal.expensetracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.iitr.vishal.expensetracker.db.dao.CardBalanceDao;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Divya on 08-11-2018.
 */

@Entity(tableName = "CardBalance", primaryKeys = {"bank_id"}, foreignKeys = @ForeignKey(entity = BankEntity.class,
        parentColumns = "Id",
        childColumns = "bank_id",

        onDelete = CASCADE), indices = {@Index(value = "bank_id")
})
public class CardBalanceEntity {
    @NonNull
    @ColumnInfo(name = "bank_id")
    private long bankId;

    @ColumnInfo(name = "balance")
    private float balance;

    @ColumnInfo(name = "last_transcation_date")
    private Date lastTranscationDate;

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public Date getLastTranscationDate() {
        return lastTranscationDate;
    }

    public void setLastTranscationDate(Date lastTranscationDate) {
        this.lastTranscationDate = lastTranscationDate;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public CardBalanceEntity(long bankId, float balance, Date lastTranscationDate) {
        setBalance(balance);
        setBankId(bankId);
        setLastTranscationDate(lastTranscationDate);
    }
}
