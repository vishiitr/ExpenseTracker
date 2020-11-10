package com.iitr.vishal.expensetracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Divya on 16-03-2018.
 */

@Entity(tableName = "Transactions", foreignKeys = @ForeignKey(entity = BankEntity.class,
        parentColumns = "Id",
        childColumns = "bank_id",
        onDelete = CASCADE), indices = {@Index(value = "bank_id")
})
public class TransactionEntity {
    @PrimaryKey(autoGenerate = false)
    private long Id;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "spent_at")
    private String spentAt;

    @ColumnInfo(name = "spent_date")
    private Date spentDate;

    @ColumnInfo(name = "bank_id")
    private long bankId;

    @ColumnInfo(name = "is_deleted")@NonNull
    private boolean isDeleted;

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

    public String getSpentAt() {
        return spentAt;
    }

    public void setSpentAt(String spentAt) {
        this.spentAt = spentAt;
    }

    public Date getSpentDate() {
        return spentDate;
    }

    public void setSpentDate(Date spentDate) {
        this.spentDate = spentDate;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public boolean getIsDeleted() {return isDeleted; }

    public  void  setIsDeleted(boolean isDeleted) {this.isDeleted = isDeleted;}

    public TransactionEntity() {

    }

    public TransactionEntity(long id, float amount, String spentAt, Date spentDate, long bankId)
    {
        setId(id);
        setAmount(amount);
        setSpentAt(spentAt);
        setSpentDate(spentDate);
        setBankId(bankId);
    }
}
