package com.example.smartlock;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import java.util.HashSet;

/* JADX INFO: loaded from: classes.dex */
public class AppLockService extends AccessibilityService {
    public static boolean isServiceRunning = false;
    public static final HashSet<String> unlockedApps = new HashSet<>();
    private LockedAppManager lockedAppManager;
    private String previousPackageName = "";

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        super.onServiceConnected();
        isServiceRunning = true;
        this.lockedAppManager = new LockedAppManager(this);
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() != 32 || accessibilityEvent.getPackageName() == null) {
            return;
        }
        String string = accessibilityEvent.getPackageName().toString();
        if (string.contains("launcher")) {
            unlockedApps.clear();
            this.previousPackageName = string;
        } else {
            if (string.equals(this.previousPackageName)) {
                return;
            }
            if (this.lockedAppManager.isAppLocked(string) && !unlockedApps.contains(string)) {
                Intent intent = new Intent(this, (Class<?>) MainActivity.class);
                intent.addFlags(276824064);
                intent.putExtra("locked_app_package", string);
                startActivity(intent);
            }
            this.previousPackageName = string;
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
        isServiceRunning = false;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        isServiceRunning = false;
        return super.onUnbind(intent);
    }
}
