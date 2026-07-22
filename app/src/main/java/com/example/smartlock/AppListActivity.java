package com.example.smartlock;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppListActivity extends AppCompatActivity {
    private AppListAdapter adapter;
    private List<AppInfo> appInfoList = new ArrayList();
    private RecyclerView appRecyclerView;
    private Button enablePermissionButton;
    private ProgressBar loadingProgressBar;
    private LockedAppManager lockedAppManager;
    private LinearLayout permissionLayout;
    private TextView permissionText;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_app_list);
        this.appRecyclerView = (RecyclerView) findViewById(R.id.appRecyclerView);
        this.loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        this.permissionLayout = (LinearLayout) findViewById(R.id.permissionLayout);
        this.permissionText = (TextView) findViewById(R.id.permissionText);
        this.enablePermissionButton = (Button) findViewById(R.id.enablePermissionButton);
        this.lockedAppManager = new LockedAppManager(this);
        setupRecyclerView();
        new LoadAppsTask().execute(new Void[0]);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
        if (!AppLockService.isServiceRunning) {
            this.permissionText.setText("Accessibility Service is required for App Lock to work.");
            this.enablePermissionButton.setText("Enable Service");
            this.enablePermissionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.AppListActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.m445lambda$checkPermissions$0$comexamplesmartlockAppListActivity(view);
                }
            });
            this.permissionLayout.setVisibility(0);
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            this.permissionText.setText("'Display over other apps' permission is required.");
            this.enablePermissionButton.setText("Grant Permission");
            this.enablePermissionButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.AppListActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.m446lambda$checkPermissions$1$comexamplesmartlockAppListActivity(view);
                }
            });
            this.permissionLayout.setVisibility(0);
            return;
        }
        this.permissionLayout.setVisibility(8);
    }

    /* JADX INFO: renamed from: lambda$checkPermissions$0$com-example-smartlock-AppListActivity, reason: not valid java name */
    /* synthetic */ void m445lambda$checkPermissions$0$comexamplesmartlockAppListActivity(View view) {
        openAccessibilitySettings();
    }

    /* JADX INFO: renamed from: lambda$checkPermissions$1$com-example-smartlock-AppListActivity, reason: not valid java name */
    /* synthetic */ void m446lambda$checkPermissions$1$comexamplesmartlockAppListActivity(View view) {
        openOverlaySettings();
    }

    private void openAccessibilitySettings() {
        startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
    }

    private void openOverlaySettings() {
        startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())));
    }

    private void setupRecyclerView() {
        this.appRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppListAdapter appListAdapter = new AppListAdapter(this, this.appInfoList);
        this.adapter = appListAdapter;
        this.appRecyclerView.setAdapter(appListAdapter);
    }

    public void sortAndRefreshList() {
        Collections.sort(this.appInfoList, new Comparator() { // from class: com.example.smartlock.AppListActivity$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return AppListActivity.lambda$sortAndRefreshList$2((AppInfo) obj, (AppInfo) obj2);
            }
        });
        this.adapter.notifyDataSetChanged();
    }

    static /* synthetic */ int lambda$sortAndRefreshList$2(AppInfo appInfo, AppInfo appInfo2) {
        if (appInfo.isLocked() && !appInfo2.isLocked()) {
            return -1;
        }
        if (appInfo.isLocked() || !appInfo2.isLocked()) {
            return appInfo.getAppName().compareTo(appInfo2.getAppName());
        }
        return 1;
    }

    private class LoadAppsTask extends AsyncTask<Void, Void, List<AppInfo>> {
        private LoadAppsTask() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            AppListActivity.this.loadingProgressBar.setVisibility(0);
            AppListActivity.this.appRecyclerView.setVisibility(8);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public List<AppInfo> doInBackground(Void... voidArr) {
            PackageManager packageManager = AppListActivity.this.getPackageManager();
            List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(128);
            ArrayList arrayList = new ArrayList();
            for (ApplicationInfo applicationInfo : installedApplications) {
                if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null && !AppListActivity.this.getPackageName().equals(applicationInfo.packageName)) {
                    arrayList.add(new AppInfo(applicationInfo.loadLabel(packageManager).toString(), applicationInfo.loadIcon(packageManager), applicationInfo.packageName, AppListActivity.this.lockedAppManager.isAppLocked(applicationInfo.packageName)));
                }
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<AppInfo> list) {
            super.onPostExecute(list);
            AppListActivity.this.appInfoList.clear();
            AppListActivity.this.appInfoList.addAll(list);
            AppListActivity.this.sortAndRefreshList();
            AppListActivity.this.loadingProgressBar.setVisibility(8);
            AppListActivity.this.appRecyclerView.setVisibility(0);
        }
    }
}
