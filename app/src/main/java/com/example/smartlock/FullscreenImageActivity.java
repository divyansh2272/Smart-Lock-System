package com.example.smartlock;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class FullscreenImageActivity extends AppCompatActivity {
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fullscreen_image);
        ImageView imageView = (ImageView) findViewById(R.id.fullscreenImageView);
        TextView textView = (TextView) findViewById(R.id.timestampTextView);
        String stringExtra = getIntent().getStringExtra("image_path");
        if (stringExtra != null) {
            File file = new File(stringExtra);
            Glide.with((FragmentActivity) this).load(file).into(imageView);
            textView.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date(file.lastModified())));
        }
    }
}
