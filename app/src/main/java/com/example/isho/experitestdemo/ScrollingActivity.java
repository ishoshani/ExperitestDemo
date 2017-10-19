package com.example.isho.experitestdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {
    TextView recordsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
       recordsView = (TextView)findViewById(R.id.recordsView);
        SQLiteDatabase db = new ScoreHelper(getApplicationContext()).getReadableDatabase();
        String[] columns = {BaseColumns._ID,"Score"};
        Cursor cs = db.query(true,"scores",columns,null,null,null,null,"Score DESC",null);
        cs.moveToFirst();
        String txt = "Game:   Score \n";
        while(!cs.isAfterLast()){
            String id = cs.getString(0);
            String score = cs.getString(1);
            txt+=id + ":          "+score+"\n";
            cs.moveToNext();
        }
        recordsView.setText(txt);

    }
}
