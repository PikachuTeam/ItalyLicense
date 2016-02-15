package com.tatteam.patente.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.tatteam.patente.R;
import com.tatteam.patente.control.LocalSharedPreferManager;

import tatteam.com.app_common.ads.AdsBigBannerHandler;
import tatteam.com.app_common.ads.AdsSmallBannerHandler;
import tatteam.com.app_common.util.AppConstant;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public abstract class BaseActivity extends FragmentActivity {
    private static final int BIG_ADS_SHOWING_PERIOD = 5;
    private static int BIG_ADS_SHOWING_COUNTER = 1;

    private AdsSmallBannerHandler adsSmallBannerHandler;
    private AdsBigBannerHandler adsBigBannerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        this.setupAds();
        addFragmentContent();
    }


    private void setupAds() {
        if (enableAdMod() && !LocalSharedPreferManager.getInstance().isPurchased()) {
            ViewGroup adsContainer = (ViewGroup) findViewById(R.id.ads_container);
            adsSmallBannerHandler = new AdsSmallBannerHandler(this, adsContainer, AppConstant.AdsType.SMALL_BANNER_DRIVING_TEST);
            adsSmallBannerHandler.setup();

            adsBigBannerHandler = new AdsBigBannerHandler(this, AppConstant.AdsType.BIG_BANNER_DRIVING_TEST);
            adsBigBannerHandler.setup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adsSmallBannerHandler != null) {
            adsSmallBannerHandler.destroy();
        }
        if (adsBigBannerHandler != null) {
            adsBigBannerHandler.destroy();
        }
    }

    public void showBigAdsIfNeeded() {
        if (enableAdMod() && !LocalSharedPreferManager.getInstance().isPurchased() && adsBigBannerHandler != null) {
            if (BIG_ADS_SHOWING_COUNTER % BIG_ADS_SHOWING_PERIOD == 0) {
                try {
                    adsBigBannerHandler.show();
                } catch (Exception ex) {
                }
            }
            BIG_ADS_SHOWING_COUNTER++;
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (enableFinishActivityAnimation()) {
            this.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean isSuperBackPressed = true;
        FragmentManager fargmentManager = getSupportFragmentManager();
        if (fargmentManager != null) {
            if (fargmentManager.getBackStackEntryCount() > 0) {
                BaseFragment currentFragment = (BaseFragment) fargmentManager.findFragmentById(R.id.container);
                isSuperBackPressed = !currentFragment.onBackPressed();
            }
        }
        if (isSuperBackPressed) {
            super.onBackPressed();
        }
    }

    protected void addFragmentContent() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BaseFragment fragment = getFragmentContent();
        transaction.add(R.id.container, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    protected boolean enableAdMod() {
        return false;
    }

    protected boolean enableFinishActivityAnimation() {
        return true;
    }

    protected abstract BaseFragment getFragmentContent();

    public static void startActivityAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit);
    }

    public static void finishActivityAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
    }

}
