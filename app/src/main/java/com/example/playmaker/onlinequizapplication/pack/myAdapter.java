package com.example.playmaker.onlinequizapplication.pack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playmaker.onlinequizapplication.R;
import com.example.playmaker.onlinequizapplication.model.Category;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myHolder> {

    Context context;
    ArrayList<Category> movies;

    public myAdapter(Context context, ArrayList<Category> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_category, parent, false);
        myHolder holder = new myHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.name.setText(movies.get(position).getName());
        PicassoClient.downloadImage(context, movies.get(position).getName(), holder.img);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }
}
