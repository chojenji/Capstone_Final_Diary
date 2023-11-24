package com.example.capstonefinaldiary;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Loading{
    private Activity activity;
    private Dialog dialog;

    public Loading(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // Set the dialog window background to transparent
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.load, null);
        ImageView gifImageView = view.findViewById(R.id.loadingImage);

        Glide.with(activity)
                .asGif()
                .load(R.drawable.load_final)
                .into(gifImageView);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
