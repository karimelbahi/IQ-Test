package com.example.playmaker.onlinequizapplication.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.playmaker.onlinequizapplication.Interface.ItemClickListener;
import com.example.playmaker.onlinequizapplication.R;

/**
 * Created by playmaker on 3/30/2018.
 */


public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;
    public ImageView category_image;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image = (ImageView) itemView.findViewById(R.id.category_image);
        category_name = (TextView) itemView.findViewById(R.id.category_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

//    public void setCategory_name(String name) {
//        category_name= ((TextView)itemView.findViewById(R.id.category_name));
//        category_name.setText(name);
//    }
    /**public void setTitle(String title){
     TextView post_title = (TextView)mView.findViewById(R.id.post_title);
     post_title.setText(title);
     }***/
}
