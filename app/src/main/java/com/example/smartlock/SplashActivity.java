package com.example.smartlock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.example.smartlock.SplashActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m472lambda$onCreate$0$comexamplesmartlockSplashActivity();
            }
        }, 2000L);
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-smartlock-SplashActivity, reason: not valid java name */
    /* synthetic */ void m472lambda$onCreate$0$comexamplesmartlockSplashActivity() {
        startActivity(new Intent(this, (Class<?>) LauncherActivity.class));
        finish();
    }
}
