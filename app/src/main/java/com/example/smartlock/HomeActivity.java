package com.example.smartlock;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.MailTo;

/* JADX INFO: loaded from: classes.dex */
public class HomeActivity extends AppCompatActivity {
    LinearLayout menuHelp;
    LinearLayout menuIntruders;
    LinearLayout menuLockApps;
    LinearLayout menuLogs;
    ImageView profileIcon;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        this.menuLockApps = (LinearLayout) findViewById(R.id.menuLockApps);
        this.menuLogs = (LinearLayout) findViewById(R.id.menuLogs);
        this.menuIntruders = (LinearLayout) findViewById(R.id.menuIntruders);
        this.menuHelp = (LinearLayout) findViewById(R.id.menuHelp);
        this.profileIcon = (ImageView) findViewById(R.id.profileIcon);
        this.menuLockApps.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m449lambda$onCreate$0$comexamplesmartlockHomeActivity(view);
            }
        });
        this.menuLogs.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m450lambda$onCreate$1$comexamplesmartlockHomeActivity(view);
            }
        });
        this.menuIntruders.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m451lambda$onCreate$2$comexamplesmartlockHomeActivity(view);
            }
        });
        this.menuHelp.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m452lambda$onCreate$3$comexamplesmartlockHomeActivity(view);
            }
        });
        this.profileIcon.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m453lambda$onCreate$4$comexamplesmartlockHomeActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m449lambda$onCreate$0$comexamplesmartlockHomeActivity(View view) {
        startActivity(new Intent(this, (Class<?>) AppListActivity.class));
    }

    /* JADX INFO: renamed from: lambda$onCreate$1$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m450lambda$onCreate$1$comexamplesmartlockHomeActivity(View view) {
        startActivity(new Intent(this, (Class<?>) ViewLogsActivity.class));
    }

    /* JADX INFO: renamed from: lambda$onCreate$2$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m451lambda$onCreate$2$comexamplesmartlockHomeActivity(View view) {
        startActivity(new Intent(this, (Class<?>) IntruderGalleryActivity.class));
    }

    /* JADX INFO: renamed from: lambda$onCreate$3$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m452lambda$onCreate$3$comexamplesmartlockHomeActivity(View view) {
        showHelpDialog();
    }

    /* JADX INFO: renamed from: lambda$onCreate$4$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m453lambda$onCreate$4$comexamplesmartlockHomeActivity(View view) {
        openProfileMenu();
    }

    private void openProfileMenu() {
        PopupMenu popupMenu = new PopupMenu(this, this.profileIcon);
        popupMenu.getMenu().add("Settings");
        popupMenu.getMenu().add("Change Theme");
        popupMenu.getMenu().add("Change App Icon");
        popupMenu.getMenu().add("Add Fingerprint");
        popupMenu.getMenu().add("Feedback");
        popupMenu.getMenu().add("About");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda8
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return this.f$0.m454lambda$openProfileMenu$5$comexamplesmartlockHomeActivity(menuItem);
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX INFO: renamed from: handleMenuClick, reason: merged with bridge method [inline-methods] */
    public boolean m454lambda$openProfileMenu$5$comexamplesmartlockHomeActivity(MenuItem menuItem) {
        String string = menuItem.getTitle().toString();
        string.hashCode();
        switch (string) {
            case "Change Theme":
                Toast.makeText(this, "Theme options coming soon", 0).show();
                return true;
            case "Feedback":
                showFeedbackChoiceDialog();
                return true;
            case "About":
                showAboutDialog();
                return true;
            case "Change App Icon":
                Toast.makeText(this, "App icon change coming soon", 0).show();
                return true;
            case "Add Fingerprint":
                Toast.makeText(this, "Fingerprint setup coming soon", 0).show();
                return true;
            case "Settings":
                startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
                return true;
            default:
                return true;
        }
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this).setTitle("Help & FAQ").setMessage(Html.fromHtml("<b>How do I change my PIN?</b><br>Go to Settings -> Change PIN.<br><br><b>How do I view intruder photos?</b><br>Tap the \"Intruders\" button on the main screen.<br><br><b>How do I contact support?</b><br>Go to Feedback -> Send via Email.")).setPositiveButton("Got it", (DialogInterface.OnClickListener) null).show();
    }

    private void showFeedbackChoiceDialog() {
        final CharSequence[] charSequenceArr = {"Send via Email", "Submit In-App Feedback"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Feedback Method");
        builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m455xc9dadc80(charSequenceArr, dialogInterface, i);
            }
        });
        builder.show();
    }

    /* JADX INFO: renamed from: lambda$showFeedbackChoiceDialog$6$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m455xc9dadc80(CharSequence[] charSequenceArr, DialogInterface dialogInterface, int i) {
        if (charSequenceArr[i].equals("Send via Email")) {
            sendFeedbackEmail();
        } else if (charSequenceArr[i].equals("Submit In-App Feedback")) {
            showInAppFeedbackDialog();
        }
    }

    private void showInAppFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflate = getLayoutInflater().inflate(R.layout.dialog_in_app_feedback, (ViewGroup) null);
        builder.setView(viewInflate);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.feedbackEditText);
        builder.setTitle("Submit Feedback");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m456xfbc69198(editText, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.HomeActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* JADX INFO: renamed from: lambda$showInAppFeedbackDialog$7$com-example-smartlock-HomeActivity, reason: not valid java name */
    /* synthetic */ void m456xfbc69198(EditText editText, DialogInterface dialogInterface, int i) {
        if (editText.getText().toString().isEmpty()) {
            return;
        }
        Toast.makeText(this, "Thanks for your feedback!", 1).show();
    }

    private void sendFeedbackEmail() {
        String str = "Device: " + Build.MANUFACTURER + " " + Build.MODEL + "\nAndroid Version: " + Build.VERSION.RELEASE + "\n\n---\n\nPlease write your feedback below:\n\n";
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"smartlocksystem7@gmail.com"});
        intent.putExtra("android.intent.extra.SUBJECT", "Feedback for Smart Lock App");
        intent.putExtra("android.intent.extra.TEXT", str);
        try {
            startActivity(Intent.createChooser(intent, "Send Feedback"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "There are no email clients installed.", 0).show();
        }
    }

    private void showAboutDialog() {
        String str;
        try {
            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            str = "";
        }
        String str2 = "<b>Version:</b> " + str + "<br><br><b>Developed by:</b> Divyansh Patel<br><br><a href=\"https://www.instagram.com/divyansh_patel_227\">Connect on Instagram</a>";
        AlertDialog.Builder title = new AlertDialog.Builder(this).setTitle("About Smart Lock");
        if (Build.VERSION.SDK_INT >= 24) {
            title.setMessage(Html.fromHtml(str2, 0));
        } else {
            title.setMessage(Html.fromHtml(str2));
        }
        TextView textView = (TextView) title.setPositiveButton("OK", (DialogInterface.OnClickListener) null).show().findViewById(android.R.id.message);
        if (textView != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
