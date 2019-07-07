package com.example.playmaker.onlinequizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.playmaker.onlinequizapplication.Common.Common;
import com.example.playmaker.onlinequizapplication.Interface.ItemClickListener;
import com.example.playmaker.onlinequizapplication.Interface.RankingCallBack;
import com.example.playmaker.onlinequizapplication.ViewHolder.RankingViewHolder;
import com.example.playmaker.onlinequizapplication.model.QuestionScore;
import com.example.playmaker.onlinequizapplication.model.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class RakingFragment extends Fragment {


    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;
    int sum = 0;

    /**not understood**/
    public static RakingFragment newInstance() {
        RakingFragment rakingFragment = new RakingFragment();
        return rakingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_raking, container, false);

        //init view
        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(container.getContext());
        rankingList.setHasFixedSize(true);
        //because orderBuChile method of firebase will sort list
        // with ascending so we need revers our recycler data
        // by layoutmanager
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);
        //now we need implement callback
        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //update to ranking table
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
              //  showRanking(); //after upload we will sort ranking table and show result
            }
        });

        loadCategories();
        return myFragment;
    }

    private void loadCategories() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Ranking").orderByKey();

        FirebaseRecyclerOptions<Ranking> options = new FirebaseRecyclerOptions.Builder<Ranking>()
                .setQuery(query, Ranking.class).build();
        //set adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RankingViewHolder holder, int position, @NonNull final Ranking model) {


                holder.txt_name.setText(model.getUserName());

                holder.txt_score.setText(String.valueOf(model.getScore()));
                //fixed crash when click to item
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.e("", "onCreateViewHolder: ");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ranking, parent,
                        false);
                RankingViewHolder holder = new RankingViewHolder(view);
                return holder;
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

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

//    private void showRanking() {
//        //print log to show
//        rankingTbl.orderByChild("score")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot data:dataSnapshot.getChildren()){
//                            Ranking local=data.getValue(Ranking.class);
//                            Log.d("DEBUG",local.getUserName());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//    }
    //here we need to create interface Callback to process value
    /**not understood**/
    public interface RandkingCallback<T>{
        void callBack(T ranking);
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(ques.getScore());
                        }
                        //after summary all score we need process sum variable here
                        //because firebase is async db so if process outside out sum
                        //value will be rest to zero
                        Ranking ranking = new Ranking(userName, sum);
                        callBack.callBack(ranking);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
