package com.example.playmaker.onlinequizapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.playmaker.onlinequizapplication.Common.Common;
import com.example.playmaker.onlinequizapplication.model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTextResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        database = FirebaseDatabase.getInstance();


        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView) findViewById(R.id.txtTotalScore);
        getTextResultQuestion = (TextView) findViewById(R.id.txtTotalQuestion);
        progressBar = (ProgressBar) findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);


        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //Get data frm bundle and ast to view
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalquestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");
            totalquestion = 10;

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTextResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer, totalquestion));

            progressBar.setMax(totalquestion);
            progressBar.setProgress(correctAnswer);

            //upload point to DB
            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),
                    Common.categoryID))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                            Common.categoryID),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryID,
                            Common.categoryName));
        }

    }
}
