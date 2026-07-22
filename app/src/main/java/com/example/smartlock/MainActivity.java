package com.example.smartlock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 101;
    private MediaPlayer alarmPlayer;
    private TextView passwordDots;
    private PinManager pinManager;
    private StringBuilder enteredPin = new StringBuilder();
    private int wrongAttempts = 0;
    private String savedIntruderPhotoPath = null;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.pinManager = new PinManager(this);
        this.passwordDots = (TextView) findViewById(R.id.passwordDots);
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 101);
        }
        setupNumpadButtons();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0;
    }

    private void setupNumpadButtons() {
        int[] iArr = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.example.smartlock.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m462lambda$setupNumpadButtons$0$comexamplesmartlockMainActivity(view);
            }
        };
        for (int i = 0; i < 10; i++) {
            findViewById(iArr[i]).setOnClickListener(onClickListener);
        }
        ((ImageButton) findViewById(R.id.btnBackspace)).setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m463lambda$setupNumpadButtons$1$comexamplesmartlockMainActivity(view);
            }
        });
        ((Button) findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m464lambda$setupNumpadButtons$2$comexamplesmartlockMainActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$setupNumpadButtons$0$com-example-smartlock-MainActivity, reason: not valid java name */
    /* synthetic */ void m462lambda$setupNumpadButtons$0$comexamplesmartlockMainActivity(View view) {
        Button button = (Button) view;
        if (this.enteredPin.length() < 6) {
            this.enteredPin.append(button.getText().toString());
            updateDots();
        }
    }

    /* JADX INFO: renamed from: lambda$setupNumpadButtons$1$com-example-smartlock-MainActivity, reason: not valid java name */
    /* synthetic */ void m463lambda$setupNumpadButtons$1$comexamplesmartlockMainActivity(View view) {
        if (this.enteredPin.length() > 0) {
            StringBuilder sb = this.enteredPin;
            sb.deleteCharAt(sb.length() - 1);
            updateDots();
        }
    }

    /* JADX INFO: renamed from: lambda$setupNumpadButtons$2$com-example-smartlock-MainActivity, reason: not valid java name */
    /* synthetic */ void m464lambda$setupNumpadButtons$2$comexamplesmartlockMainActivity(View view) {
        checkPassword();
    }

    private void updateDots() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.enteredPin.length(); i++) {
            sb.append("• ");
        }
        this.passwordDots.setText(sb.toString().trim());
    }

    private void checkPassword() {
        String stringExtra = getIntent().getStringExtra("locked_app_package");
        if (this.pinManager.checkPin(this.enteredPin.toString())) {
            this.wrongAttempts = 0;
            stopAlarm();
            if (stringExtra != null) {
                AppLockService.unlockedApps.add(stringExtra);
                finish();
                return;
            } else {
                Intent intent = new Intent(this, (Class<?>) HomeActivity.class);
                intent.addFlags(268468224);
                startActivity(intent);
                finish();
                return;
            }
        }
        int i = this.wrongAttempts + 1;
        this.wrongAttempts = i;
        if (i == 1) {
            captureIntruderPhotoAndLog();
        } else if (i == 2) {
            Toast.makeText(this, "Try again", 0).show();
        } else if (i >= 3) {
            triggerAlarm();
        }
        this.enteredPin.setLength(0);
        updateDots();
    }

    private void captureIntruderPhotoAndLog() {
        String string = this.enteredPin.toString();
        String stringExtra = getIntent().getStringExtra("locked_app_package");
        CameraXManager.takePicture(this, this, stringExtra, new AnonymousClass1(string, stringExtra));
    }

    /* JADX INFO: renamed from: com.example.smartlock.MainActivity$1, reason: invalid class name */
    class AnonymousClass1 implements CameraXManager.PhotoCaptureListener {
        final /* synthetic */ String val$incorrectPin;
        final /* synthetic */ String val$lockedAppPackage;

        AnonymousClass1(String str, String str2) {
            this.val$incorrectPin = str;
            this.val$lockedAppPackage = str2;
        }

        @Override // com.example.smartlock.CameraXManager.PhotoCaptureListener
        public void onPhotoCaptured(String str) throws Throwable {
            MainActivity.this.savedIntruderPhotoPath = str;
            Log.d("CAM", "Photo saved to: " + str);
            MainActivity.this.runOnUiThread(new Runnable() { // from class: com.example.smartlock.MainActivity$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m467lambda$onPhotoCaptured$0$comexamplesmartlockMainActivity$1();
                }
            });
            MainActivity.this.saveLogEntry("Incorrect PIN", System.currentTimeMillis(), str, this.val$incorrectPin, this.val$lockedAppPackage);
        }

        /* JADX INFO: renamed from: lambda$onPhotoCaptured$0$com-example-smartlock-MainActivity$1, reason: not valid java name */
        /* synthetic */ void m467lambda$onPhotoCaptured$0$comexamplesmartlockMainActivity$1() {
            Toast.makeText(MainActivity.this, "Photo Captured!", 0).show();
        }

        @Override // com.example.smartlock.CameraXManager.PhotoCaptureListener
        public void onError(final String str) {
            Log.e("CAM", "Error: " + str);
            MainActivity.this.runOnUiThread(new Runnable() { // from class: com.example.smartlock.MainActivity$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m466lambda$onError$1$comexamplesmartlockMainActivity$1(str);
                }
            });
        }

        /* JADX INFO: renamed from: lambda$onError$1$com-example-smartlock-MainActivity$1, reason: not valid java name */
        /* synthetic */ void m466lambda$onError$1$comexamplesmartlockMainActivity$1(String str) {
            Toast.makeText(MainActivity.this, "Camera Error: " + str, 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveLogEntry(String str, long j, String str2, String str3, String str4) throws Throwable {
        List list;
        FileWriter fileWriter;
        Gson gson = new Gson();
        File file = new File(getFilesDir(), "logs.json");
        List arrayList = new ArrayList();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                try {
                    list = (List) gson.fromJson(fileReader, new TypeToken<ArrayList<LogEntry>>() { // from class: com.example.smartlock.MainActivity.2
                    }.getType());
                    if (list == null) {
                        try {
                            arrayList = new ArrayList();
                        } catch (Throwable th) {
                            th = th;
                            Throwable th2 = th;
                            try {
                                try {
                                    fileReader.close();
                                    throw th2;
                                } catch (IOException e) {
                                    e = e;
                                    arrayList = list;
                                    Log.e("LOG", "Error reading log file: " + e.getMessage());
                                    arrayList.add(new LogEntry(str, j, str2, str3, str4));
                                    fileWriter = new FileWriter(file);
                                    try {
                                        gson.toJson(arrayList, fileWriter);
                                        Log.d("LOG", "Log entry saved successfully.");
                                        fileWriter.close();
                                    } catch (Throwable th3) {
                                        try {
                                            fileWriter.close();
                                            throw th3;
                                        } catch (Throwable th4) {
                                            th3.addSuppressed(th4);
                                            throw th3;
                                        }
                                    }
                                }
                            } catch (Throwable th5) {
                                th2.addSuppressed(th5);
                                throw th2;
                            }
                        }
                    } else {
                        arrayList = list;
                    }
                    fileReader.close();
                } catch (Throwable th6) {
                    th = th6;
                    list = arrayList;
                }
            } catch (IOException e2) {
                e = e2;
            }
        }
        arrayList.add(new LogEntry(str, j, str2, str3, str4));
        try {
            fileWriter = new FileWriter(file);
            gson.toJson(arrayList, fileWriter);
            Log.d("LOG", "Log entry saved successfully.");
            fileWriter.close();
        } catch (IOException e3) {
            Log.e("LOG", "Error writing to log file: " + e3.getMessage());
        }
    }

    private void triggerAlarm() {
        try {
            if (this.alarmPlayer == null) {
                this.alarmPlayer = MediaPlayer.create(this, R.raw.alarm);
            }
            this.alarmPlayer.setLooping(false);
            this.alarmPlayer.start();
            this.alarmPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.example.smartlock.MainActivity$$ExternalSyntheticLambda0
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    this.f$0.m465lambda$triggerAlarm$3$comexamplesmartlockMainActivity(mediaPlayer);
                }
            });
        } catch (Exception e) {
            Log.e("ALARM", "Error: " + e.getMessage());
        }
        Vibrator vibrator = (Vibrator) getSystemService("vibrator");
        if (vibrator != null) {
            vibrator.vibrate(500L);
        }
    }

    /* JADX INFO: renamed from: lambda$triggerAlarm$3$com-example-smartlock-MainActivity, reason: not valid java name */
    /* synthetic */ void m465lambda$triggerAlarm$3$comexamplesmartlockMainActivity(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.release();
        this.alarmPlayer = null;
    }

    private void stopAlarm() {
        MediaPlayer mediaPlayer = this.alarmPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.alarmPlayer.release();
            this.alarmPlayer = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 101 || allPermissionsGranted()) {
            return;
        }
        Toast.makeText(this, "Camera permission is required to use this feature.", 0).show();
    }
}
