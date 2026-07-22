package com.example.smartlock;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public class AppInfo {
    private Drawable appIcon;
    private String appName;
    private boolean isLocked;
    private String packageName;

    public AppInfo(String str, Drawable drawable, String str2, boolean z) {
        this.appName = str;
        this.appIcon = drawable;
        this.packageName = str2;
        this.isLocked = z;
    }

    public String getAppName() {
        return this.appName;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean z) {
        this.isLocked = z;
    }
}
