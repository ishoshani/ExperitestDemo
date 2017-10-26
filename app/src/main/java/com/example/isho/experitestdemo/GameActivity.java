package com.example.isho.experitestdemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TimeCaller{
    ImageButton Clicker;
    Button BackButton;
    Button AgainButton;
    TextView Counter;
    TextView Clock;
    MyLog log;
    Random rand;
    TimeHandler time;
    SQLiteDatabase scores;
    TextView wifiText;
    TextView GPSText;
    WebView webBox;
    double time_left;
    int score;
    int size_x;
    int size_y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        log = MyLog.getInstance(getFilesDir());
        setupStats();
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
                log.log("pressed game button");
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
                log.log("pressed Again button");
                time = new TimeHandler(GameActivity.this);
                Counter.setText(String.valueOf(score));
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
                log.log("pressed end game");
                GameActivity.this.finish();
            }
        });

    }
    public void displaceClicker(){
        Clicker.setX(rand.nextInt(size_x-100));
        Clicker.setY(rand.nextInt(size_y-100));
        Clicker.bringToFront();
        Clicker.invalidate();
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
        score = 0;
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
    public void setupStats() {
        wifiText = (TextView) findViewById(R.id.wifiBox);
        GPSText = (TextView) findViewById(R.id.GPSBox);
        webBox = (WebView) findViewById(R.id.WebBox);
        String SSID = null;
        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo.isConnected()) {

            WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            SSID = wifiInfo.getSSID();
        }
        wifiText.setText(SSID);
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION");

        if (permission == PermissionChecker.PERMISSION_GRANTED) {

            Criteria compare = new Criteria();
            compare.setAccuracy(Criteria.ACCURACY_COARSE);
            Location current_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = current_location.getLatitude();
            double lon = current_location.getLongitude();
            Log.d("Stats", lat+" : "+lon);
            GPSText.setText(String.format(Locale.getDefault(),"Lat:%.2f  Long:%.2f", lat, lon));
        }
        else{
            Log.d("Stats", "didn't get permissions");
        }
        permission = PermissionChecker.checkSelfPermission(getApplicationContext(), "android.permission.INTERNET");

        if (permission == PermissionChecker.PERMISSION_GRANTED) {
            TimeZone tz = TimeZone.getDefault();
            String tzName = tz.getID();
            log.log("sent request for time at "+tzName);
            webBox.loadUrl("https://experitest-server.herokuapp.com?size=12px&tz="+tzName);
        }

    }
}
