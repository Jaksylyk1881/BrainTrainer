package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        textViewResult = findViewById(R.id.textViewResult);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int maxScore = sharedPreferences.getInt("MaxScore",0);
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("score")){
            int score = intent.getIntExtra("score",0);
            String result = String.format("Рекорд: %s\nВаш результат: %s",maxScore,score);
            textViewResult.setText(result);
        }
    }

    public void onClickStartAgain(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}