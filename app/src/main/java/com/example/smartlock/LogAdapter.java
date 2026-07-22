package com.example.smartlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class LogAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<LogEntry> logEntries;

    public LogAdapter(Context context, List<LogEntry> list) {
        this.context = context;
        this.logEntries = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(android.R.layout.simple_list_item_2, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        LogEntry logEntry = this.logEntries.get(i);
        String str = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date(logEntry.getTimestamp()));
        viewHolder.text1.setText(logEntry.getStatus() + " on " + (logEntry.getAppName() != null ? logEntry.getAppName() : "SmartLock") + " (PIN: " + logEntry.getEnteredPin() + ")");
        viewHolder.text2.setText(str);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.logEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public ViewHolder(View view) {
            super(view);
            this.text1 = (TextView) view.findViewById(android.R.id.text1);
            this.text2 = (TextView) view.findViewById(android.R.id.text2);
        }
    }
}
