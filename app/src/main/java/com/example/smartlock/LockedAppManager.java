package com.example.smartlock;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class LockedAppManager {
    private static final String KEY_LOCKED_APPS = "locked_apps";
    private static final String PREF_NAME = "SmartLockPrefs";
    private SharedPreferences sharedPreferences;

    public LockedAppManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void addLockedApp(String str) {
        Set<String> lockedApps = getLockedApps();
        lockedApps.add(str);
        saveLockedApps(lockedApps);
    }

    public void removeLockedApp(String str) {
        Set<String> lockedApps = getLockedApps();
        lockedApps.remove(str);
        saveLockedApps(lockedApps);
    }

    public Set<String> getLockedApps() {
        return new HashSet(this.sharedPreferences.getStringSet(KEY_LOCKED_APPS, new HashSet()));
    }

    private void saveLockedApps(Set<String> set) {
        this.sharedPreferences.edit().putStringSet(KEY_LOCKED_APPS, set).apply();
    }

    public boolean isAppLocked(String str) {
        return getLockedApps().contains(str);
    }
}
