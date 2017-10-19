package com.example.isho.experitestdemo;

import android.os.AsyncTask;

/**
 * Created by isho on 10/16/17.
 */

public class TimeHandler extends AsyncTask<Double,Double,Boolean> {
    private Double SetTime;
    public boolean canceled;
    TimeCaller caller;
    public TimeHandler(TimeCaller caller) {
        this.caller = caller;
        canceled = false;
    }
    @Override
    protected Boolean doInBackground(Double...time){
        Double setTime = time[0];
        while(setTime>0){
            if(canceled){
                return false;
            }
            try {
                synchronized (this) {
                    this.wait(1);
                    setTime -= 0.1;
                    publishProgress(setTime);
                }
            } catch (InterruptedException e){
                return true;
            }
        }
        return true;

    }
    protected void onProgressUpdate(Double...time){
        caller.timerUpdate(time[0]);
    }
    protected void onPostExecute(Boolean end){
        if(end) {
            caller.timerEnd();
        }else {
            caller.timerReset();
        }
    }
}
