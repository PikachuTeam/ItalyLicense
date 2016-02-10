package com.tatteam.patente;

import android.app.Application;

import com.tatteam.patente.control.InAppBillingController;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.database.DataSource;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.sqlite.DatabaseLoader;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class ClientApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalSharedPreferManager.getInstance().initIfNeeded(getApplicationContext());
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        LocalSharedPreferManager.getInstance().destroy();
        InAppBillingController.getInstace().destroy();
        AppCommon.getInstance().destroy();
        DatabaseLoader.getInstance().destroy();
        super.onTerminate();
    }
}
