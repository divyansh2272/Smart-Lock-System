package com.example.smartlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IntruderPhotoAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<File> photoFiles;

    public IntruderPhotoAdapter(Context context, List<File> list) {
        this.context = context;
        this.photoFiles = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_intruder_photo, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final File file = this.photoFiles.get(i);
        Glide.with(this.context).load(file).centerCrop().into(viewHolder.imageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.example.smartlock.IntruderPhotoAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m457xdaebd6b2(file, view);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.example.smartlock.IntruderPhotoAdapter$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return this.f$0.m458x9dd84011(file, viewHolder, view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$onBindViewHolder$0$com-example-smartlock-IntruderPhotoAdapter, reason: not valid java name */
    /* synthetic */ void m457xdaebd6b2(File file, View view) {
        Intent intent = new Intent(this.context, (Class<?>) FullscreenImageActivity.class);
        intent.putExtra("image_path", file.getAbsolutePath());
        this.context.startActivity(intent);
    }

    /* JADX INFO: renamed from: lambda$onBindViewHolder$1$com-example-smartlock-IntruderPhotoAdapter, reason: not valid java name */
    /* synthetic */ boolean m458x9dd84011(File file, ViewHolder viewHolder, View view) {
        showDeleteConfirmationDialog(file, viewHolder.getAdapterPosition());
        return true;
    }

    private void showDeleteConfirmationDialog(final File file, final int i) {
        new AlertDialog.Builder(this.context).setTitle("Delete Photo").setMessage("Are you sure you want to delete this photo?").setPositiveButton("Delete", new DialogInterface.OnClickListener() { // from class: com.example.smartlock.IntruderPhotoAdapter$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                this.f$0.m459xc6ef5578(file, i, dialogInterface, i2);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: renamed from: lambda$showDeleteConfirmationDialog$2$com-example-smartlock-IntruderPhotoAdapter, reason: not valid java name */
    /* synthetic */ void m459xc6ef5578(File file, int i, DialogInterface dialogInterface, int i2) {
        deletePhoto(file, i);
    }

    private void deletePhoto(File file, int i) {
        if (file.exists()) {
            if (file.delete()) {
                this.photoFiles.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, this.photoFiles.size());
                Toast.makeText(this.context, "Photo deleted", 0).show();
                Context context = this.context;
                if (context instanceof IntruderGalleryActivity) {
                    ((IntruderGalleryActivity) context).checkEmptyState();
                    return;
                }
                return;
            }
            Toast.makeText(this.context, "Failed to delete photo", 0).show();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.photoFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.intruderImageView);
        }
    }
}
