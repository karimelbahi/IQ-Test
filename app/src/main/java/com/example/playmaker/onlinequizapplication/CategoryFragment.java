package com.example.playmaker.onlinequizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playmaker.onlinequizapplication.Common.Common;
import com.example.playmaker.onlinequizapplication.Interface.ItemClickListener;
import com.example.playmaker.onlinequizapplication.ViewHolder.CategoryViewHolder;
import com.example.playmaker.onlinequizapplication.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class CategoryFragment extends Fragment {

    View myFragment;
    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale.getDefault().getLanguage().startsWith("en");


        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);
        listCategory = (RecyclerView) myFragment.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(layoutManager);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // it mak app crash
        categories.keepSynced(true);

        loadCategories();
        return myFragment;
    }

    private void loadCategories() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Category").orderByKey();

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class).build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, int position,
                                            @NonNull final Category model) {

                viewHolder.category_name.setText(model.getName());

                Picasso.with(getActivity()).load(model.getImage()).into(viewHolder.category_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),
//                                model.getName()), Toast.LENGTH_SHORT).show();
                        Intent startGame = new Intent(getActivity(), Start.class);
                        Common.categoryID = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent,
                        false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
                return holder;
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
