package com.example.playmaker.onlinequizapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playmaker.onlinequizapplication.ViewHolder.ScoreDetailViewHolder;
import com.example.playmaker.onlinequizapplication.model.QuestionScore;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView scoreList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    String viewUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        //firebase
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        //view
        scoreList = (RecyclerView) findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);


        if (getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if (!viewUser.isEmpty())
            loadDetails(viewUser);
    }

    private void loadDetails(String viewUser) {

        Query query = FirebaseDatabase.getInstance().getReference().child("Question_Score").orderByKey();

        FirebaseRecyclerOptions<QuestionScore> options = new FirebaseRecyclerOptions.Builder<QuestionScore>()
                .setQuery(query, QuestionScore.class).build();

        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ScoreDetailViewHolder holder, int position, @NonNull QuestionScore model) {

                holder.txt_name.setText(model.getCategoryName());
                holder.txt_score.setText(model.getScore());
            }

            @NonNull
            @Override
            public ScoreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_detail_layout, parent,
                        false);
                ScoreDetailViewHolder holder = new ScoreDetailViewHolder(view);
                return holder;
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
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
