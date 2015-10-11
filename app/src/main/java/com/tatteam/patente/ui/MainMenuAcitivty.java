package com.tatteam.patente.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tatteam.patente.app.BaseActivity;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.ui.fragment.MainMenuFragment;
import com.tatteam.patente.utility.SharingControler;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class MainMenuAcitivty extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return new MainMenuFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SharingControler.SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected boolean enableAdMod() {
        return true;
    }
}
