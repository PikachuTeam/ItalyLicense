package com.tatteam.patente.app;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tatteam.patente.R;
import com.tatteam.patente.control.LocalSharedPreferManager;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public abstract class BaseActivity extends FragmentActivity {

    private AdView mAdView;
    private boolean isAdLoadingFine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        this.setupAdView();
        addFragmentContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupAdView() {
        mAdView = (AdView) findViewById(R.id.adView);
        if (enableAdMod() && !LocalSharedPreferManager.getInstance().isPurchased()) {
            mAdView.setVisibility(View.VISIBLE);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    isAdLoadingFine = false;
                    mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    isAdLoadingFine = true;
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
            this.loadADIfNeed();
        } else {
            mAdView.setVisibility(View.GONE);
        }

    }

    public void loadADIfNeed() {
        if (enableAdMod() && !LocalSharedPreferManager.getInstance().isPurchased() ) {
            if(!isAdLoadingFine){
                mAdView.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        } else {
            mAdView.setVisibility(View.GONE);
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

    private void addFragmentContent() {
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
