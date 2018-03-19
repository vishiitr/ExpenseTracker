package com.iitr.vishal.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iitr.vishal.expensetracker.Task.SplashloadingTask;

public class SplashscreenActivity extends Activity implements SplashloadingTask.SplashloadingTaskFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
        TextView textView =(TextView) findViewById(R.id.txtrere);

        new SplashloadingTask(progressBar,textView,this).execute();
    }

    @Override
    public void onTaskFinished() {
        completeSplash();
    }

    private void completeSplash() {
        startApp();
        finish();
    }

    private void startApp() {
        Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
