package com.tatteam.patente.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tatteam.patente.R;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.utility.ShareUtil;


public class SplashActivity extends Activity {
    private static final long SPLASH_DURATION = 2000;
    private Handler handler;
    private boolean isDatabaseImported = false;
    private boolean isWaitingInitData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LocalSharedPreferManager.getInstance().init(getApplicationContext());
        DataSource.getInstance().init(getApplicationContext());

        importDatabase();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (isDatabaseImported) {
                    isDatabaseImported = false;
                    switchToChooseTargetActivity();
                } else {
                    isWaitingInitData = true;
                }
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(0, SPLASH_DURATION);
    }

    private void importDatabase() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataSource.getInstance().createDatabaseIfNeed();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                isDatabaseImported = true;
                if (isWaitingInitData) {
                    switchToChooseTargetActivity();
                }
            }
        };
        task.execute();
    }

    private void switchToChooseTargetActivity() {
        startActivity(new Intent(SplashActivity.this, ChooseTargetActivity.class));
//        BaseActivity.startActivityAnimation(this,new Intent(SplashActivity.this, ChooseTargetActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
