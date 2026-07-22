package com.example.smartlock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/* JADX INFO: loaded from: classes.dex */
public class SettingsActivity extends AppCompatActivity {
    private PinManager pinManager;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);
        this.pinManager = new PinManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.SettingsActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m469lambda$onCreate$0$comexamplesmartlockSettingsActivity(view);
            }
        });
        ((RelativeLayout) findViewById(R.id.changePinLayout)).setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.SettingsActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m470lambda$onCreate$1$comexamplesmartlockSettingsActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-smartlock-SettingsActivity, reason: not valid java name */
    /* synthetic */ void m469lambda$onCreate$0$comexamplesmartlockSettingsActivity(View view) {
        onBackPressed();
    }

    /* JADX INFO: renamed from: lambda$onCreate$1$com-example-smartlock-SettingsActivity, reason: not valid java name */
    /* synthetic */ void m470lambda$onCreate$1$comexamplesmartlockSettingsActivity(View view) {
        showChangePinDialog();
    }

    private void showChangePinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflate = getLayoutInflater().inflate(R.layout.dialog_change_pin, (ViewGroup) null);
        builder.setView(viewInflate);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.oldPinEditText);
        final EditText editText2 = (EditText) viewInflate.findViewById(R.id.newPinEditText);
        final EditText editText3 = (EditText) viewInflate.findViewById(R.id.confirmPinEditText);
        builder.setTitle("Change PIN");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.SettingsActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m471x8714169d(editText, editText2, editText3, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.SettingsActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* JADX INFO: renamed from: lambda$showChangePinDialog$2$com-example-smartlock-SettingsActivity, reason: not valid java name */
    /* synthetic */ void m471x8714169d(EditText editText, EditText editText2, EditText editText3, DialogInterface dialogInterface, int i) {
        String string = editText.getText().toString();
        String string2 = editText2.getText().toString();
        String string3 = editText3.getText().toString();
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3)) {
            Toast.makeText(this, "All fields are required", 0).show();
            return;
        }
        if (!this.pinManager.checkPin(string)) {
            Toast.makeText(this, "Incorrect current PIN", 0).show();
            return;
        }
        if (string2.length() != 6) {
            Toast.makeText(this, "New PIN must be 6 digits", 0).show();
        } else if (!string2.equals(string3)) {
            Toast.makeText(this, "New PINs do not match", 0).show();
        } else {
            this.pinManager.savePin(string2);
            Toast.makeText(this, "PIN updated successfully", 0).show();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
