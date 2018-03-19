package com.iitr.vishal.expensetracker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Divya on 16-03-2018.
 */

@Entity(tableName = "BanksNCard")
public class BankEntity {
    @PrimaryKey(autoGenerate = true)@NonNull
    private long Id;

    @ColumnInfo(name = "bank_name")
    private String BankName;

    @ColumnInfo(name = "card_nbr")
    private String CardNbr;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }

    public String getCardNbr() {
        return CardNbr;
    }

    public void setCardNbr(String cardNbr) {
        this.CardNbr = cardNbr;
    }

    public BankEntity() {
    }

    public BankEntity(String bankName, String cardNbr) {
        this.BankName = bankName;
        this.CardNbr = cardNbr;
    }
}
