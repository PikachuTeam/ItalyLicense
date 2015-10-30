package com.tatteam.patente.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tatteam.patente.billing.IabException;
import com.tatteam.patente.billing.IabHelper;
import com.tatteam.patente.billing.IabResult;
import com.tatteam.patente.billing.Inventory;

import java.util.List;

/**
 * Created by tnguyenhuy on Apr 6, 2015.
 */

public class InAppBillingController {
    private static String BASE64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt1ehbBobfkzqEX2J/hJmPkahPEsquR+Ku18y4FP6ZjtmDmsIE4GzTjKrGWGQLyAd1P0ZQDCjpkYffRJkK2OKcq3mU3/R80iIBrAJwqIgqiRZOSxbZ6nPG7noa+t8XzVcERmMLvCnpj4DnL3O25Q8d3Qv3M4VfbyTrdcdcl1ytq7WR77CK76Vqn0xhAESNFVfkhq7zJhSR1UBMDANNJNv+LjjopxkAbSc6DueqegkF9idGkKeidERfnY8B/iVUC+6ZxGNFVx31fNT7/0j7YOUZJ/imHDwszbUVvZb9UDt4lsIYKehJiZKgSQtiupkAhHeVqvzen2t7Ze9xyvXEdk9YwIDAQAB";

    public static final int RC_REQUEST = 10001;
    public static final String SKU_REMOVE_ADS = "patente_remove_ads";

    private static InAppBillingController instance;
    private IabHelper mHelper;

    private InAppBillingController() {
    }

    public static InAppBillingController getInstace() {
        if (instance == null) {
            instance = new InAppBillingController();
        }
        return instance;
    }

    public void init(Context context, final Runnable finishedRunnable) {
        if (mHelper != null)
            return;
        try {
            mHelper = new IabHelper(context, BASE64_KEY);

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        return;
                    }
                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null)
                        return;

                    if (finishedRunnable != null) {
                        finishedRunnable.run();
                    }
                    // IAB is fully set up. Now, let's get an inventory of stuff we
                    // own.
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            mHelper = null;
        }

    }

    public IabHelper getIabHelper() {
        return mHelper;
    }

    public void queryInventoryAsync(IabHelper.QueryInventoryFinishedListener listener) {
        if (mHelper != null) {
            mHelper.queryInventoryAsync(true, listener);
        }
    }

    public Inventory queryInventory(List<String> skuList) throws IabException {
        if (mHelper != null) {
            return mHelper.queryInventory(true, skuList);
        }
        return null;
    }

    public void launchPurchaseFlow(Activity activity, String skuNumber, IabHelper.OnIabPurchaseFinishedListener listener) {
        if (mHelper != null) {
            mHelper.launchPurchaseFlow(activity, skuNumber, RC_REQUEST, listener);
        }
    }


    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
        } else {
//            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    public void destroy() {
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
        instance = null;
    }


}
