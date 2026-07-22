package com.example.smartlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<AppInfo> appInfoList;
    private Context context;
    private LockedAppManager lockedAppManager;

    public AppListAdapter(Context context, List<AppInfo> list) {
        this.context = context;
        this.appInfoList = list;
        this.lockedAppManager = new LockedAppManager(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_app_list, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final AppInfo appInfo = this.appInfoList.get(i);
        viewHolder.appName.setText(appInfo.getAppName());
        viewHolder.appIcon.setImageDrawable(appInfo.getAppIcon());
        viewHolder.lockSwitch.setOnCheckedChangeListener(null);
        viewHolder.lockSwitch.setChecked(appInfo.isLocked());
        viewHolder.lockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.example.smartlock.AppListAdapter$$ExternalSyntheticLambda0
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$0.m447lambda$onBindViewHolder$0$comexamplesmartlockAppListAdapter(appInfo, compoundButton, z);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onBindViewHolder$0$com-example-smartlock-AppListAdapter, reason: not valid java name */
    /* synthetic */ void m447lambda$onBindViewHolder$0$comexamplesmartlockAppListAdapter(AppInfo appInfo, CompoundButton compoundButton, boolean z) {
        appInfo.setLocked(z);
        if (z) {
            this.lockedAppManager.addLockedApp(appInfo.getPackageName());
        } else {
            this.lockedAppManager.removeLockedApp(appInfo.getPackageName());
        }
        Context context = this.context;
        if (context instanceof AppListActivity) {
            ((AppListActivity) context).sortAndRefreshList();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.appInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        SwitchCompat lockSwitch;

        public ViewHolder(View view) {
            super(view);
            this.appIcon = (ImageView) view.findViewById(R.id.appIcon);
            this.appName = (TextView) view.findViewById(R.id.appName);
            this.lockSwitch = (SwitchCompat) view.findViewById(R.id.lockSwitch);
        }
    }
}
