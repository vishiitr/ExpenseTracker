package com.iitr.vishal.expensetracker.Processor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.db.AppDatabase;

import java.util.concurrent.Callable;

/**
 * Created by Divya on 17-03-2018.
 */

public class SmsProcessor {
    private int totalSmsCount;
    private Cursor cursor;
    private Context context;

    public int TotalSMSCount() {
        return totalSmsCount;
    }

    public SmsProcessor(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        ContentResolver cr = null;
        try {
            cr = context.getContentResolver();
        } catch (Exception e) {
            int a = 0;
            //
        }
        long lastSmsId = getLastSmsId();
        String where = "abs(" + Telephony.Sms.ADDRESS + ")=0.0 and _id > " + lastSmsId ;
        cursor = cr.query(Telephony.Sms.CONTENT_URI, null, where, null, "_id desc");
        if (cursor != null) {
            totalSmsCount = cursor.getCount();
        } else
            totalSmsCount = 0;
    }

    public void processSms(Callable<Void> callbackForSmsProcessed) {
        if (cursor.moveToFirst()) {
            SmsModel smsModel = new SmsModel();
            for (int j = 0; j < totalSmsCount; j++) {

                try {
                    callbackForSmsProcessed.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                smsModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID)));
                smsModel.setTime(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)));
                smsModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)));
                smsModel.setMsg(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)));
                //Date dateFormat = new Date(Long.valueOf(smsDate));

                saveToDatabasae(smsModel);
                cursor.moveToNext();
            }
        }
    }

    private void saveToDatabasae(SmsModel smsModel) {
        BankProcessor bankProcessor = new BankProcessor(context);
        bankProcessor.saveSms(smsModel);
    }

    private long getLastSmsId() {
        return AppDatabase.getAppDatabase(context).transactionDao().getLastSmsId();
    }
}
