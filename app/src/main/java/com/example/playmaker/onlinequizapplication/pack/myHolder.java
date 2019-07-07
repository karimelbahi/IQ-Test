package com.example.playmaker.onlinequizapplication.pack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playmaker.onlinequizapplication.R;

public class myHolder extends RecyclerView.ViewHolder {

    ImageView img;
    TextView name;

    public myHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.category_name);
        img = (ImageView) itemView.findViewById(R.id.category_image);
    }
}
