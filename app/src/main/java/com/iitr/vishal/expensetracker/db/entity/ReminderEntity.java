package com.iitr.vishal.expensetracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Divya on 27-03-2018.
 */

@Entity(tableName = "Reminders", foreignKeys = @ForeignKey(entity = BankEntity.class,
        parentColumns = "Id",
        childColumns = "bank_id",
        onDelete = CASCADE), indices = {@Index(value = "bank_id")
})
public class ReminderEntity {
    @PrimaryKey(autoGenerate = false)
    private long Id;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "reminder_date")
    private Date reminderDate;

    @ColumnInfo(name = "bank_id")
    private long bankId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getSpentDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public ReminderEntity() {

    }

    public ReminderEntity(long id, float amount, Date reminderDate, long bankId)
    {
        setId(id);
        setAmount(amount);
        setReminderDate(reminderDate);
        setBankId(bankId);
    }
}
