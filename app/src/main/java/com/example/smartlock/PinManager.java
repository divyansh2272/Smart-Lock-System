package com.example.smartlock;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes.dex */
public class PinManager {
    private static final String KEY_PIN = "user_pin";
    private static final String PREF_NAME = "SmartLockPrefs";
    private SharedPreferences sharedPreferences;

    public PinManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
    }

    public String getPin() {
        return this.sharedPreferences.getString(KEY_PIN, null);
    }

    public void savePin(String str) {
        this.sharedPreferences.edit().putString(KEY_PIN, str).apply();
    }

    public boolean isPinSet() {
        return getPin() != null;
    }

    public boolean checkPin(String str) {
        String pin = getPin();
        return pin != null && pin.equals(str);
    }
}
