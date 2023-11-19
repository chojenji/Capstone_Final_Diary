package com.example.capstonefinaldiary;
import android.app.Activity;
import android.app.Dialog;
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
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.load, null);
        ImageView gifImageView = view.findViewById(R.id.loadingImage);

        Glide.with(activity)
                .asGif()
                .load(R.drawable.loading)
                .into(gifImageView);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
