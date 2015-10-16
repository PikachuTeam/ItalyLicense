package com.tatteam.patente.control;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ThanhNH on 5/2/2015.
 */
public class LocalSharedPreferManager {
    private static final String PREF_NAME = "patente_prefer";
    private static final String KEY_IS_PURCHASED = "is_purchased";
    private static LocalSharedPreferManager instance;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LocalSharedPreferManager() {
    }

    public static LocalSharedPreferManager getInstance() {
        if (instance == null) {
            instance = new LocalSharedPreferManager();
        }
        return instance;
    }

    public void initIfNeeded(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }

    public boolean isPurchased() {
        return pref.getBoolean(KEY_IS_PURCHASED, false);
    }

    public void setIsPurchased(boolean isPurchased) {
        editor.putBoolean(KEY_IS_PURCHASED, isPurchased);
        editor.commit();
    }

    public void destroy() {
        instance = null;
    }

}
