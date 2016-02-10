package com.tatteam.patente.ui;

import android.content.Intent;

import com.tatteam.patente.R;
import com.tatteam.patente.control.LocalSharedPreferManager;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;
import tatteam.com.app_common.ui.activity.BaseSplashActivity;
import tatteam.com.app_common.util.AppConstant;


public class SplashActivity extends BaseSplashActivity {
    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateContentView() {

    }

    @Override
    protected void onInitAppCommon() {
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().increaseLaunchTime();
        AppCommon.getInstance().syncAdsIfNeeded(AppConstant.AdsType.SMALL_BANNER_TEST, AppConstant.AdsType.SMALL_BANNER_DRIVING_TEST);

        LocalSharedPreferManager.getInstance().initIfNeeded(getApplicationContext());

        DatabaseLoader.getInstance().createIfNeeded(getApplicationContext(), "drivingquiz.db");
    }

    @Override
    protected void onFinishInitAppCommon() {
        switchToChooseTargetActivity();
    }


    private void switchToChooseTargetActivity() {
        startActivity(new Intent(SplashActivity.this, ChooseTargetActivity.class));
//        BaseActivity.startActivityAnimation(this,new Intent(SplashActivity.this, ChooseTargetActivity.class));
        this.finish();
    }

}
