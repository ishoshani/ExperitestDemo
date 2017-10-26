package com.example.isho.experitestdemo;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.io.FileOutputStream;

/**
 * Created by isho on 10/22/17.
 */

public class MyLog {
    private static final String fileName="expLog";
    private static final String initial = "Opened Application";
    private static MyLog myInstance;
    private File directory;
    private File file;
    private MyLog(File dir){
        directory=dir;
        file = new File(directory,fileName);
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(file, true));
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            out.append(currentDateTimeString+": "+ initial+"\n");
            out.close();
        }catch (FileNotFoundException e){
            Log.d("logging","filename not found"+e);
        }catch (IOException e){
            Log.d("logging", "Io exception on init"+e);
        }

    }
    public static MyLog getInstance(File dir){
        if(myInstance == null){
            myInstance = new MyLog(dir);
        }
        return myInstance;
    }
    public void log(String action) {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(file, true));
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            out.append(currentDateTimeString+": "+ action+"\n");
            out.close();

        } catch (FileNotFoundException e) {
            Log.d("logging","failed to find folder"+e);
        } catch (IOException e){
            Log.d("logging","io exception on log"+e);
        }


    }

    public String toString() {
        String txt = "";
        try{
            Scanner read = new Scanner(file);
            while(read.hasNext()){
                txt+=read.nextLine()+"\n";
            }
            read.close();
        }catch (FileNotFoundException e){
            Log.d("logging","failed to find file for reading");
            txt+="failed to access file";
        }
        return txt;

    }
    public void ClearLog(){
        log("attempted to deleting old log");

        try{
            PrintWriter deleter = new PrintWriter(file);
            deleter.append("");
            deleter.close();
        }catch (FileNotFoundException e){
            Log.d("logging","failed to find file for reading");
        }
    }
}
