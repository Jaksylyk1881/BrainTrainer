package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView variant0;
    private TextView variant1;
    private TextView variant2;
    private TextView variant3;
    private TextView textViewTask;
    private TextView score;
    private TextView rightOrWrong;
    private TextView timer;
    private int firstNum;
    private int secondNum;
    private int rightAnswer;
    private int rightAnsPos;
    private int questionCount=0;
    private int rightQuestionCount=0;
    private ArrayList<TextView> textViews;
    private boolean game = false;
    private long allSeconds=20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        variant0 = findViewById(R.id.variant0);
        variant1 = findViewById(R.id.variant1);
        variant2 = findViewById(R.id.variant2);
        variant3 = findViewById(R.id.variant3);
        rightOrWrong = findViewById(R.id.rightOrWrong);
        textViewTask =findViewById(R.id.textViewTask);
        score = findViewById(R.id.score);
        timer = findViewById(R.id.timer);
        textViews = new ArrayList<>();
        textViews.add(variant0);
        textViews.add(variant1);
        textViews.add(variant2);
        textViews.add(variant3);

        generateTask();

        CountDownTimer timeR = new CountDownTimer(allSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(getTimer(millisUntilFinished));
                if (millisUntilFinished<=10000){
                    timer.setTextColor(Color.parseColor("#B00020"));

                }else {
                    timer.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            @Override
            public void onFinish() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int maxScore = preferences.getInt("MaxScore",0);
                if (rightQuestionCount>=maxScore){
                    preferences.edit().putInt("MaxScore",rightQuestionCount).apply();
                }
                game=true;
                Intent intent  = new Intent(MainActivity.this,ResultsActivity.class);
                        intent.putExtra("score", rightQuestionCount);
                startActivity(intent);
            }
        };
        timeR.start();
    }

    private String getTimer(long millisUntilFinished){
        int seconds = (int) (millisUntilFinished/1000);
        int minutes = seconds/60;
        seconds = seconds%60;

        return String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
    }


    public void generateTask(){
        generateNums();
        textViewTask.setText(Integer.toString(firstNum)+getString(R.string.minusSign)+Integer.toString(secondNum));
        for (int i=0; i<4; i++){
            if (rightAnsPos==i){
                textViews.get(i).setText(Integer.toString(rightAnswer));
            }else {
                int wrongAns = getWrongAnswer();
                textViews.get(i).setText(Integer.toString(wrongAns));
            }
        }
        String scoreformat = String.format("%s/%s",questionCount,rightQuestionCount);
        score.setText(scoreformat);
    }

    public void generateNums(){
        firstNum=(int) (Math.random()*100);
        secondNum=(int) (Math.random()*100);
        rightAnswer = firstNum-secondNum;
        rightAnsPos = (int) (Math.random()*4);
    }

    public int getWrongAnswer(){
        int wrongAnswer = (int) (Math.random()*100);
        if (wrongAnswer == rightAnswer) {
            wrongAnswer = getWrongAnswer();
        }
        return wrongAnswer;
    }

    public void onClickChooseAns(View view) {
        if (!game){
        TextView button = (TextView) view;
        int tag = (Integer.parseInt(button.getTag().toString().trim()));
        questionCount++;
        if (tag==rightAnsPos){
            rightOrWrong.setText("Правильно");
            Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
            allSeconds+=3000;
            rightQuestionCount++;
        }else {
            rightOrWrong.setText("Не правильно");
            allSeconds-=5000;
            Toast.makeText(this, "Не правильно", Toast.LENGTH_SHORT).show();
        }
        generateTask();
    }}
}