package com.example.smartlock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ViewLogsActivity extends AppCompatActivity {
    private LogAdapter adapter;
    private Button clearLogsButton;
    private List<LogEntry> logEntries = new ArrayList();
    private RecyclerView logsRecyclerView;
    private TextView noLogsText;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_logs);
        this.logsRecyclerView = (RecyclerView) findViewById(R.id.logsRecyclerView);
        this.noLogsText = (TextView) findViewById(R.id.noLogsText);
        this.clearLogsButton = (Button) findViewById(R.id.clearLogsButton);
        setupRecyclerView();
        loadLogs();
        this.clearLogsButton.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.ViewLogsActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m473lambda$onCreate$0$comexamplesmartlockViewLogsActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-smartlock-ViewLogsActivity, reason: not valid java name */
    /* synthetic */ void m473lambda$onCreate$0$comexamplesmartlockViewLogsActivity(View view) {
        showClearLogsConfirmationDialog();
    }

    private void setupRecyclerView() {
        this.logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LogAdapter logAdapter = new LogAdapter(this, this.logEntries);
        this.adapter = logAdapter;
        this.logsRecyclerView.setAdapter(logAdapter);
    }

    private void loadLogs() {
        File file = new File(getFilesDir(), "logs.json");
        if (!file.exists()) {
            showEmptyMessage();
            return;
        }
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(file);
            try {
                List list = (List) gson.fromJson(fileReader, new TypeToken<ArrayList<LogEntry>>() { // from class: com.example.smartlock.ViewLogsActivity.1
                }.getType());
                if (list == null || list.isEmpty()) {
                    showEmptyMessage();
                } else {
                    this.logEntries.clear();
                    this.logEntries.addAll(list);
                    Collections.reverse(this.logEntries);
                    this.adapter.notifyDataSetChanged();
                    this.logsRecyclerView.setVisibility(0);
                    this.noLogsText.setVisibility(8);
                    this.clearLogsButton.setVisibility(0);
                }
                fileReader.close();
            } catch (Throwable th) {
                try {
                    fileReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            Log.e("LOG", "Error reading log file: " + e.getMessage());
            showEmptyMessage();
        }
    }

    private void showEmptyMessage() {
        this.logsRecyclerView.setVisibility(8);
        this.noLogsText.setVisibility(0);
        this.clearLogsButton.setVisibility(8);
    }

    private void showClearLogsConfirmationDialog() {
        new AlertDialog.Builder(this).setTitle("Clear Logs").setMessage("Are you sure you want to delete all log entries?").setPositiveButton("Clear All", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.ViewLogsActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m474xab146de3(dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: renamed from: lambda$showClearLogsConfirmationDialog$1$com-example-smartlock-ViewLogsActivity, reason: not valid java name */
    /* synthetic */ void m474xab146de3(DialogInterface dialogInterface, int i) {
        clearLogs();
    }

    private void clearLogs() {
        File file = new File(getFilesDir(), "logs.json");
        if (file.exists()) {
            if (file.delete()) {
                this.logEntries.clear();
                this.adapter.notifyDataSetChanged();
                showEmptyMessage();
                Toast.makeText(this, "Logs cleared", 0).show();
                return;
            }
            Toast.makeText(this, "Failed to clear logs", 0).show();
        }
    }
}
