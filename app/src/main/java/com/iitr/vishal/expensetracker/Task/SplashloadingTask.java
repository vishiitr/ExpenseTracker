package com.iitr.vishal.expensetracker.Task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.Common.Cache;
import com.iitr.vishal.expensetracker.Common.Pair;
import com.iitr.vishal.expensetracker.Processor.SmsProcessor;
import com.iitr.vishal.expensetracker.db.AppDatabase;
import com.iitr.vishal.expensetracker.db.dao.BankDao;
import com.iitr.vishal.expensetracker.db.entity.BankEntity;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Divya on 17-03-2018.
 */

public class SplashloadingTask extends AsyncTask<String, Integer, Integer> {

    private final ProgressBar progressBar;
    private final SplashloadingTaskFinishedListener finishedListener;
    private final TextView textView;

    private int currSmsNbr = 0;
    private int totalSmsCount = 0;

    public interface SplashloadingTaskFinishedListener {
        void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }

    public SplashloadingTask(ProgressBar progressBar, TextView textView, SplashloadingTaskFinishedListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
        this.textView = textView;
    }

    @Override
    protected Integer doInBackground(String... params) {
        updateCache();
        uploadSmsToDatabase();
        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = (int) ((values[0] / (float) totalSmsCount) * 100);
        progressBar.setProgress(progress); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
        textView.setText("Parsing SMSes " + values[0] + "/" + totalSmsCount);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
    }

    private void updateCache() {
        BankDao bankDao = AppDatabase.getAppDatabase((Activity)finishedListener).bankDao();
        //AppDatabase.getAppDatabase((Activity)finishedListener).transactionDao().Merchant();
        List<BankEntity> bankEntities = bankDao.getAllBanknCard();
        Cache.BanksNCards.clear();
        for (BankEntity item : bankEntities) {
            Cache.BanksNCards.put(new Pair<>(item.getBankName(), item.getCardNbr()), (long) item.getId());
        }
    }

    private void uploadSmsToDatabase() {
        SmsProcessor  smsProcessor = new SmsProcessor((Activity)finishedListener);
        totalSmsCount = smsProcessor.TotalSMSCount();
        smsProcessor.processSms(new Callable<Void>() {
            public Void call() {
                UpdateSMSCount();
                return null;
            }
        });
    }

    private void UpdateSMSCount() {
        currSmsNbr++;
        publishProgress(currSmsNbr);
    }
}
