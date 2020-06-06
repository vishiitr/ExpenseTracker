package com.iitr.vishal.expensetracker.Processor;

import android.content.Context;

import com.iitr.vishal.expensetracker.Common.Cache;
import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Common.MerchantFormatter;
import com.iitr.vishal.expensetracker.Common.Pair;
import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.Banks.AmexBankProcessor;
import com.iitr.vishal.expensetracker.Processor.Banks.CitiBankProcessor;
import com.iitr.vishal.expensetracker.Processor.Banks.HsbcBankProcessor;
import com.iitr.vishal.expensetracker.Processor.Banks.IciciBankProcessor;
import com.iitr.vishal.expensetracker.Processor.Banks.IndusBankProcessor;
import com.iitr.vishal.expensetracker.Processor.Banks.SbiBankProcessor;
import com.iitr.vishal.expensetracker.db.AppDatabase;
import com.iitr.vishal.expensetracker.db.dao.BankDao;
import com.iitr.vishal.expensetracker.db.dao.CardBalanceDao;
import com.iitr.vishal.expensetracker.db.dao.ReminderDao;
import com.iitr.vishal.expensetracker.db.dao.TransactionDao;
import com.iitr.vishal.expensetracker.db.entity.BankEntity;
import com.iitr.vishal.expensetracker.db.entity.CardBalanceEntity;
import com.iitr.vishal.expensetracker.db.entity.ReminderEntity;
import com.iitr.vishal.expensetracker.db.entity.TransactionEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Divya on 17-03-2018.
 */

public class BankProcessor {
    private Context context;

    public interface IBankProcessor {
        TranscationModel onSaveTranscation(SmsModel smsModel); // If you want to pass something back to the listener add a param to this method
    }

    public BankProcessor(Context context) {
        this.context = context;
    }

    public void saveSms(SmsModel smsModel) {
        IBankProcessor bankProcessor = null;
        long bankId = 0;
        if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMECITI)) {
            bankProcessor = new CitiBankProcessor();
        } else if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMEINDUS)) {
            bankProcessor = new IndusBankProcessor();
        } else if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMEICICI)) {
            bankProcessor = new IciciBankProcessor();
        } else if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMEHSBC)) {
            bankProcessor = new HsbcBankProcessor();
        } else if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMESBI)) {
            bankProcessor = new SbiBankProcessor();
        }
        else if (smsModel.getAddress().toLowerCase().contains(Constants.BANKSMSNAMEAMEX)) {
            bankProcessor = new AmexBankProcessor();
        }

        if (bankProcessor != null) {
            TranscationModel transcationModel = bankProcessor.onSaveTranscation(smsModel);
            if (transcationModel != null) {
                bankId = saveBank(transcationModel.bankName, transcationModel.spendingCard);
                if (transcationModel.spentAt != null && !transcationModel.spentAt.isEmpty())//it means its a transcation sms
                {
                    saveTranscation(transcationModel.smsId, transcationModel.spentAmount, transcationModel.spentDate, transcationModel.spentAt, bankId);
                    saveOrUpdateBalance(transcationModel.availableBalance, transcationModel.spentDate, bankId);
                }
                else
                    saveReminder(transcationModel.smsId, transcationModel.spentAmount, transcationModel.spentDate, bankId);

            }
        }
    }

    public long saveBank(String bankName, String cardNbr) {
        boolean bankExists = Cache.BanksNCards.containsKey(new Pair<>(bankName, cardNbr));
        Long bankId;
        if (!bankExists) {
            BankDao bankDao = AppDatabase.getAppDatabase(context).bankDao();
            bankId = bankDao.insert(new BankEntity(bankName, cardNbr));
            Cache.BanksNCards.put(new Pair<>(bankName, cardNbr), bankId);
        } else {
            bankId = Cache.BanksNCards.get(new Pair<>(bankName, cardNbr));
        }
        return bankId;
    }

    public void saveTranscation(long id, float spentAmount, Date spentDate, String spentAt, long bankId) {
        TransactionDao transactionDao = AppDatabase.getAppDatabase(context).transactionDao();
        spentAt = MerchantFormatter.getFormattedName(spentAt);
        transactionDao.insert(new TransactionEntity(id, spentAmount, spentAt, spentDate, bankId));
    }

    public void saveReminder(long id, float spentAmount, Date spentDate, long bankId) {
        ReminderDao reminderDao = AppDatabase.getAppDatabase(context).reminderDao();
        reminderDao.insert(new ReminderEntity(id, spentAmount, spentDate, bankId));
    }

    public void saveOrUpdateBalance(float availableAmount, Date spentDate, long bankId) {
        CardBalanceDao cardBalanceDao = AppDatabase.getAppDatabase(context).cardBalanceDao();
        if (cardBalanceDao.getBalanceByBankCardId(bankId).size() == 0)
            cardBalanceDao.insert(new CardBalanceEntity(bankId, availableAmount, spentDate));
        else
            cardBalanceDao.updateReminderAlarmStatus(availableAmount, spentDate, bankId);
    }
}
