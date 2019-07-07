package com.example.playmaker.onlinequizapplication.pack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class FirebaseClient {
    Context context;
    String DB_URL;
    RecyclerView re;

    public FirebaseClient(Context context, String DB_URL, RecyclerView re) {
        this.context = context;
        this.DB_URL = DB_URL;
        this.re = re;

        //INITIALIZE

    }
}
