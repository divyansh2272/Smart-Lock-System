package com.example.smartlock;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class CameraXManager {

    public interface PhotoCaptureListener {
        void onError(String str);

        void onPhotoCaptured(String str);
    }

    public static void takePicture(final Context context, final LifecycleOwner lifecycleOwner, final String str, final PhotoCaptureListener photoCaptureListener) {
        final Executor mainExecutor = ContextCompat.getMainExecutor(context);
        final ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(context);
        processCameraProvider.addListener(new Runnable() { // from class: com.example.smartlock.CameraXManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CameraXManager.lambda$takePicture$0(processCameraProvider, lifecycleOwner, context, str, mainExecutor, photoCaptureListener);
            }
        }, mainExecutor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$takePicture$0(ListenableFuture listenableFuture, LifecycleOwner lifecycleOwner, Context context, String str, Executor executor, final PhotoCaptureListener photoCaptureListener) {
        try {
            final ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) listenableFuture.get();
            ImageCapture imageCaptureBuild = new ImageCapture.Builder().setCaptureMode(1).build();
            processCameraProvider.bindToLifecycle(lifecycleOwner, new CameraSelector.Builder().requireLensFacing(0).build(), imageCaptureBuild);
            File file = new File(context.getFilesDir(), "intruder_photos");
            final String appName = getAppName(context, str);
            if (str != null && !str.isEmpty()) {
                file = new File(file, appName.replaceAll("[^a-zA-Z0-9._-]+", "_"));
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            final File file2 = new File(file, appName.replaceAll("[^a-zA-Z0-9._-]+", "_") + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg");
            imageCaptureBuild.m153lambda$takePicture$4$androidxcameracoreImageCapture(new ImageCapture.OutputFileOptions.Builder(file2).build(), executor, new ImageCapture.OnImageSavedCallback() { // from class: com.example.smartlock.CameraXManager.1
                @Override // androidx.camera.core.ImageCapture.OnImageSavedCallback
                public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                    try {
                        CameraXManager.addWatermark(file2, appName, new Date());
                        photoCaptureListener.onPhotoCaptured(file2.getAbsolutePath());
                    } catch (Exception e) {
                        photoCaptureListener.onError("Failed to add watermark: " + e.getMessage());
                    }
                    processCameraProvider.unbindAll();
                }

                @Override // androidx.camera.core.ImageCapture.OnImageSavedCallback
                public void onError(ImageCaptureException imageCaptureException) {
                    photoCaptureListener.onError("Capture failed: " + imageCaptureException.getMessage());
                    processCameraProvider.unbindAll();
                }
            });
        } catch (Exception e) {
            photoCaptureListener.onError("Camera initialization failed: " + e.getMessage());
        }
    }

    private static String getAppName(Context context, String str) {
        if (str == null || str.isEmpty()) {
            return "SmartLock";
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addWatermark(File file, String str, Date date) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        if (bitmapDecodeFile == null) {
            throw new IOException("Failed to decode bitmap from file.");
        }
        Bitmap bitmapCopy = bitmapDecodeFile.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmapCopy);
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setTextSize(48.0f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(2.0f, 1.0f, 1.0f, ViewCompat.MEASURED_STATE_MASK);
        String str2 = "Intruder: " + str;
        String str3 = "Time: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(date);
        Rect rect = new Rect();
        paint.getTextBounds(str2, 0, str2.length(), rect);
        int height = (bitmapCopy.getHeight() - rect.height()) - 100;
        canvas.drawText(str2, 50.0f, height, paint);
        canvas.drawText(str3, 50.0f, height + rect.height() + 20, paint);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            bitmapCopy.compress(Bitmap.CompressFormat.JPEG, 95, fileOutputStream);
            fileOutputStream.close();
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
