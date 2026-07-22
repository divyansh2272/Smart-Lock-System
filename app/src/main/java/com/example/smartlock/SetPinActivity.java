package com.example.smartlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/* JADX INFO: loaded from: classes.dex */
public class SetPinActivity extends AppCompatActivity {
    private EditText confirmPinEditText;
    private EditText pinEditText;
    private PinManager pinManager;
    private Button savePinButton;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_set_pin);
        this.pinEditText = (EditText) findViewById(R.id.pinEditText);
        this.confirmPinEditText = (EditText) findViewById(R.id.confirmPinEditText);
        this.savePinButton = (Button) findViewById(R.id.savePinButton);
        this.pinManager = new PinManager(this);
        this.savePinButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.SetPinActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m468lambda$onCreate$0$comexamplesmartlockSetPinActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-smartlock-SetPinActivity, reason: not valid java name */
    /* synthetic */ void m468lambda$onCreate$0$comexamplesmartlockSetPinActivity(View view) {
        String string = this.pinEditText.getText().toString();
        String string2 = this.confirmPinEditText.getText().toString();
        if (string.length() != 6) {
            Toast.makeText(this, "PIN must be 6 digits", 0).show();
            return;
        }
        if (!string.equals(string2)) {
            Toast.makeText(this, "PINs do not match", 0).show();
            return;
        }
        this.pinManager.savePin(string);
        Toast.makeText(this, "PIN saved successfully", 0).show();
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        intent.addFlags(268468224);
        startActivity(intent);
        finish();
    }
}
