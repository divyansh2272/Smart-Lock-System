package com.example.smartlock;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IntruderGalleryActivity extends AppCompatActivity {
    private IntruderPhotoAdapter adapter;
    private RecyclerView intruderRecyclerView;
    private TextView noPhotosText;
    private List<File> photoFiles = new ArrayList();

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_intruder_gallery);
        this.intruderRecyclerView = (RecyclerView) findViewById(R.id.intruderRecyclerView);
        this.noPhotosText = (TextView) findViewById(R.id.noPhotosText);
        setupRecyclerView();
        loadIntruderPhotos();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        loadIntruderPhotos();
    }

    private void setupRecyclerView() {
        this.intruderRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        IntruderPhotoAdapter intruderPhotoAdapter = new IntruderPhotoAdapter(this, this.photoFiles);
        this.adapter = intruderPhotoAdapter;
        this.intruderRecyclerView.setAdapter(intruderPhotoAdapter);
    }

    private void loadIntruderPhotos() {
        this.photoFiles.clear();
        findPhotosRecursively(new File(getFilesDir(), "intruder_photos"));
        if (this.photoFiles.isEmpty()) {
            showEmptyMessage();
            return;
        }
        Collections.sort(this.photoFiles, new Comparator() { // from class: com.example.smartlock.IntruderGalleryActivity$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Long.compare(((File) obj2).lastModified(), ((File) obj).lastModified());
            }
        });
        this.adapter.notifyDataSetChanged();
        this.intruderRecyclerView.setVisibility(0);
        this.noPhotosText.setVisibility(8);
    }

    private void findPhotosRecursively(File file) {
        File[] fileArrListFiles;
        if (!file.exists() || (fileArrListFiles = file.listFiles()) == null) {
            return;
        }
        for (File file2 : fileArrListFiles) {
            if (file2.isDirectory()) {
                findPhotosRecursively(file2);
            } else if (file2.isFile() && file2.getName().endsWith(".jpg")) {
                this.photoFiles.add(file2);
            }
        }
    }

    private void showEmptyMessage() {
        this.intruderRecyclerView.setVisibility(8);
        this.noPhotosText.setVisibility(0);
    }

    public void checkEmptyState() {
        if (this.photoFiles.isEmpty()) {
            showEmptyMessage();
        }
    }
}
