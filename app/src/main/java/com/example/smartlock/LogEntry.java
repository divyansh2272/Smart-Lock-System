package com.example.smartlock;

/* JADX INFO: loaded from: classes.dex */
public class LogEntry {
    private String appName;
    private String enteredPin;
    private String photoPath;
    private String status;
    private long timestamp;

    public LogEntry(String str, long j, String str2, String str3, String str4) {
        this.status = str;
        this.timestamp = j;
        this.photoPath = str2;
        this.enteredPin = str3;
        this.appName = str4;
    }

    public String getStatus() {
        return this.status;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public String getEnteredPin() {
        return this.enteredPin;
    }

    public String getAppName() {
        return this.appName;
    }
}
