package com.tatteam.patente.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tatteam.patente.app.BaseActivity;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.ui.fragment.MainMenuFragment;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class MainMenuAcitivty extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalSharedPreferManager.getInstance().initIfNeeded(getApplicationContext());
        DataSource.getInstance().initIfNeeded(getApplicationContext());
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return new MainMenuFragment();
    }

    @Override
    protected void addFragmentContent() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() == 0) {
            super.addFragmentContent();
        }else{
            manager.popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected boolean enableAdMod() {
        return true;
    }

    @Override
    protected void onDestroy() {
//        DataSource.getInstance().destroy();
        super.onDestroy();
    }
}
