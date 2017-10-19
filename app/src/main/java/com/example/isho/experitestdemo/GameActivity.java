package com.example.isho.experitestdemo;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Timer;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TimeCaller{
    ImageButton Clicker;
    Button BackButton;
    Button AgainButton;
    TextView Counter;
    TextView Clock;
    Random rand;
    TimeHandler time;
    SQLiteDatabase scores;
    double time_left;
    int score;
    int size_x;
    int size_y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        score = 0;
        rand = new Random();
        scores = new ScoreHelper(getApplicationContext()).getWritableDatabase();
        Clock = (TextView)findViewById(R.id.timer);
        Clicker = (ImageButton)findViewById(R.id.clicker_button);
        Counter = (TextView)findViewById(R.id.counter);
        BackButton=(Button)findViewById(R.id.ExitButton);
        AgainButton=(Button)findViewById(R.id.AgainButton);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        size_x = Resources.getSystem().getDisplayMetrics().widthPixels;
        size_y = Resources.getSystem().getDisplayMetrics().heightPixels;
        displaceClicker();
        time_left = 3.00;
        time = new TimeHandler(this);
        time.execute(time_left);

        Clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score++;
                String scoreString = String.valueOf(score);
                Counter.setText(scoreString);
                displaceClicker();
                time.canceled=true;

            }
        });
        AgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_left=3.00;
                time = new TimeHandler(GameActivity.this);
                Clicker.setVisibility(View.VISIBLE);
                AgainButton.setVisibility(View.INVISIBLE);
                BackButton.setVisibility((View.INVISIBLE));
                displaceClicker();
                time.execute(time_left);
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.this.finish();
            }
        });

    }
    public void displaceClicker(){
        Clicker.setX(rand.nextInt(size_x-100));
        Clicker.setY(rand.nextInt(size_y-100));
    }

    @Override
    public void timerUpdate(double time) {
        Clock.setText(String.format("%.3f",(time)));
    }
    public void timerEnd(){
        Clock.setText("YOU LOSE");
        ContentValues content = new ContentValues();
        content.put("score",score);
        scores.insert("scores",null, content);
        Clicker.setVisibility(View.INVISIBLE);
        AgainButton.setVisibility(View.VISIBLE);
        BackButton.setVisibility((View.VISIBLE));

    }
    public void timerReset(){
        time_left = time_left*0.95;
        time = new TimeHandler(this);
        time.execute(time_left);
        time.canceled=false;
    }
}
