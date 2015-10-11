package com.tatteam.patente.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tatteam.patente.app.BaseActivity;
import com.tatteam.patente.app.BaseFragment;
import com.tatteam.patente.billing.IabHelper;
import com.tatteam.patente.billing.IabResult;
import com.tatteam.patente.control.InAppBillingController;
import com.tatteam.patente.billing.Inventory;
import com.tatteam.patente.control.LocalSharedPreferManager;
import com.tatteam.patente.database.DataSource;
import com.tatteam.patente.ui.fragment.ChooseTargetFragment;
import com.tatteam.patente.utility.SharingControler;

/**
 * Created by ThanhNH on 2/1/2015.
 */
public class ChooseTargetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalSharedPreferManager.getInstance().init(getApplicationContext());

        InAppBillingController.getInstace().init(this, new Runnable() {
            @Override
            public void run() {
                if (!LocalSharedPreferManager.getInstance().isPurchased()) {
                    requestCheckPurchasedItem();
                }
            }
        });
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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SharingControler.SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        DataSource.getInstance().destroy();
        LocalSharedPreferManager.getInstance().destroy();
        InAppBillingController.getInstace().destroy();
        super.onDestroy();
    }


}
