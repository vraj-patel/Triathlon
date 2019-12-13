package com.example.triathlon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    enum DifficultyLevel { EASY, MEDIUM, HARD, THREE_D }
    DifficultyLevel level;

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.easyBtn:
                level = DifficultyLevel.EASY;
                break;
            case R.id.mediumBtn:
                level = DifficultyLevel.MEDIUM;
                break;
            case R.id.hardBtn:
                level = DifficultyLevel.HARD;
                break;
            case R.id.threeDBtn:
                level = DifficultyLevel.THREE_D;
                break;
            case R.id.startBtn:
                Intent startIntent = new Intent(getApplicationContext(), GridActivity.class);
                startActivity(startIntent);
                break;


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button easyBtn = findViewById(R.id.easyBtn);
        Button mediumBtn = findViewById(R.id.mediumBtn);
        Button hardBtn = findViewById(R.id.hardBtn);
        Button threeDBtn = findViewById(R.id.threeDBtn);
        Button startBtn = findViewById(R.id.startBtn);

        easyBtn.setOnClickListener(this);
        mediumBtn.setOnClickListener(this);
        hardBtn.setOnClickListener(this);
        threeDBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);

    }
}
