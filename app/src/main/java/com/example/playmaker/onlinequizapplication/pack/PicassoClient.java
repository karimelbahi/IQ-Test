package com.example.playmaker.onlinequizapplication.pack;

import android.content.Context;
import android.widget.ImageView;

import com.example.playmaker.onlinequizapplication.R;
import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadImage(Context context, String URL, ImageView img) {
        if (URL != null && URL.length() > 0) {
            Picasso.with(context).load(URL).placeholder(R.drawable.background).into(img);
        } else {
            Picasso.with(context).load(R.drawable.background).into(img);
        }
    }
}
