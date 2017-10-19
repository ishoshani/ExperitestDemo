package com.example.isho.experitestdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by isho on 10/18/17.
 */

public class ScoreHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME      = "demoApp.db";
    private static final int DATABASE_VERSION      = 3;
    //Score Table
    private static final String Table_Name = "scores";
    private static final String Scores = "score";
    private static final String CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS"+Table_Name+"("+
            BaseColumns._ID+" Integer Primary Key AutoIncrement," +
            "Score Integer);";
    ScoreHelper (Context context){
        super(context,"SCORES",null,DATABASE_VERSION);
    }
    @Override 
    public void onUpgrade(SQLiteDatabase db, int from, int to){
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE  IF NOT EXISTS scores ("+ BaseColumns._ID+" Integer Primary Key," +
                "Score Integer)");
    }

}
