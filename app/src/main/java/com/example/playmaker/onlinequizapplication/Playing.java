package com.example.playmaker.onlinequizapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.playmaker.onlinequizapplication.Common.Common;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class Playing extends AppCompatActivity implements View.OnClickListener {


    final static long INTERVAL = 1000, TIMEOUT = 100000; //1 and 7 sec
    int progressValue = 0;

    CountDownTimer mcountDownTimer;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;

    //Firebase
//    FirebaseDatabase database;
//    DatabaseReference question;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD, btnE;
    TextView txtScore, txtQuestionNum, question_text;

    MediaPlayer MPCorrect, MPWrong;

    Animation animationAlpha;
    Animation animationScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        //firebase
//        database=FirebaseDatabase.getInstance();
//        question=database.getReference("Questions");

        //View
        txtScore = (TextView) findViewById(R.id.txtSore);
        txtQuestionNum = (TextView) findViewById(R.id.txtTotalQuestion);
        // question_text = (TextView) findViewById(R.id.question_text);
        question_image = (ImageView) findViewById(R.id.question_image);

        MPCorrect = MediaPlayer.create(this, R.raw.correct);
        MPWrong = MediaPlayer.create(this, R.raw.fail);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        animationAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        animationScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);
        btnE = (Button) findViewById(R.id.btnAnswerE);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
        btnE.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        mcountDownTimer.cancel();

        if (index < totalQuestion) { //still have question in list

            Button clikedButton = (Button) v;
            if (clikedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                //Choose correct answer
                v.startAnimation(animationAlpha);
                MPCorrect.start();
                score += 10;
                correctAnswer++;
                Log.e(" index", "" + index);
                showQuestion(++index);//next question
            } else {
                //Choose wrong answer
                MPWrong.start();
                v.startAnimation(animationScale);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {
                        //here you can start your Activity B.
                        Intent intent = new Intent(getBaseContext(), Done.class);
                        Bundle dataSend = new Bundle();
                        dataSend.putInt("SCORE", score);
                        dataSend.putInt("TOTAL", thisQuestion);
                        dataSend.putInt("CORRECT", correctAnswer);
                        intent.putExtras(dataSend);
                        startActivity(intent);
                        finish();
                    }

                }, 2000);

                //long start = System.currentTimeMillis();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


            }
            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {

            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                //if is image
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                // question_text.setVisibility(View.INVISIBLE);
            } else {
                // question_text.setText(Common.questionList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                // question_text.setVisibility(View.VISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
            btnE.setText(Common.questionList.get(index).getAnswerE());

            mcountDownTimer.start(); //start timer
        } else {
            //if final question
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", thisQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mcountDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mcountDownTimer.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}

