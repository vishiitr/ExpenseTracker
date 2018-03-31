package com.iitr.vishal.expensetracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Divya on 27-03-2018.
 */

@Entity(tableName = "Reminders",primaryKeys = { "bank_id", "reminder_date" }, foreignKeys = @ForeignKey(entity = BankEntity.class,
        parentColumns = "Id",
        childColumns = "bank_id",

        onDelete = CASCADE), indices = {@Index(value = "bank_id")
})
public class ReminderEntity {

    private long Id;

    @ColumnInfo(name = "amount")
    private float amount;

    @NonNull
    @ColumnInfo(name = "reminder_date")
    private Date reminderDate;

    @NonNull
    @ColumnInfo(name = "bank_id")
    private long bankId;

    @Ignore
    public String bankName;

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

    public Date getReminderDate() {
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

    /*public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }*/

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
