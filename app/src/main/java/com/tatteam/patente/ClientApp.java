package com.tatteam.patente;

import android.app.Application;

import com.tatteam.patente.control.InAppBillingController;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.database.DataSource;
/**
 * Created by ThanhNH on 2/1/2015.
 */
public class ClientApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalSharedPreferManager.getInstance().initIfNeeded(getApplicationContext());
        DataSource.getInstance().initIfNeeded(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        DataSource.getInstance().destroy();
        LocalSharedPreferManager.getInstance().destroy();
        InAppBillingController.getInstace().destroy();
        super.onTerminate();
    }
}
