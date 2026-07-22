package com.example.smartlock;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class LauncherActivity extends AppCompatActivity {
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        Intent intent;
        super.onCreate(bundle);
        if (new PinManager(this).isPinSet()) {
            intent = new Intent(this, (Class<?>) MainActivity.class);
        } else {
            intent = new Intent(this, (Class<?>) SetPinActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
