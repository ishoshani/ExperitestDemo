package com.example.isho.experitestdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    Button GameButton;
    Button RecordsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        GameButton = (Button)findViewById(R.id.GameButton);
        GameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGame(view);
            }
        });
        RecordsButton= (Button)findViewById(R.id.recordsButton);
        RecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchRecords(view);
            }
        });

    }
    public void launchGame(View view){
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
    public void launchRecords(View view){
        Intent intent = new Intent(this,ScrollingActivity.class);
        startActivity(intent);
    }
}
