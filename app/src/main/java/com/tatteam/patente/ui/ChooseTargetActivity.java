package com.tatteam.patente.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tatteam.patente.R;
import com.tatteam.patente.app.BaseActivity;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.billing.IabHelper;
import com.tatteam.patente.billing.IabResult;
import com.tatteam.patente.billing.Inventory;
import com.tatteam.patente.control.InAppBillingController;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.ui.fragment.ChooseTargetFragment;

import tatteam.com.app_common.AppCommon;
import tatteam.com.app_common.util.CloseAppHandler;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class ChooseTargetActivity extends BaseActivity implements CloseAppHandler.OnCloseAppListener {
    private CloseAppHandler closeAppHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCommon.getInstance().initIfNeeded(getApplicationContext());
        LocalSharedPreferManager.getInstance().initIfNeeded(getApplicationContext());

        InAppBillingController.getInstace().init(this, new Runnable() {
            @Override
            public void run() {
                if (!LocalSharedPreferManager.getInstance().isPurchased()) {
                    requestCheckPurchasedItem();
                }
            }
        });


        closeAppHandler = new CloseAppHandler(this);
        closeAppHandler.setListener(this);
    }

    private void requestCheckPurchasedItem() {
        InAppBillingController.getInstace().queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (result.isFailure()) {
                } else {
                    boolean isPurchased = inv.hasPurchase(InAppBillingController.SKU_REMOVE_ADS);
                    LocalSharedPreferManager.getInstance().setIsPurchased(isPurchased);
                }
            }
        });
    }

    @Override
    protected boolean enableAdMod() {
        return false;
    }

    @Override
    protected boolean enableFinishActivityAnimation() {
        return false;
    }

    @Override
    protected BaseFragment getFragmentContent() {
        return new ChooseTargetFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        InAppBillingController.getInstace().handleActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        closeAppHandler.setKeyBackPress(this);
    }


    @Override
    public void onRateAppDialogClose() {
        finish();
    }

    @Override
    public void onTryToCloseApp() {
        Toast.makeText(this, getString(R.string.close_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReallyWantToCloseApp() {
        finish();
    }

    @Override
    protected void onDestroy() {
        InAppBillingController.getInstace().destroy();
//        LocalSharedPreferManager.getInstance().destroy();
        super.onDestroy();
    }


}
