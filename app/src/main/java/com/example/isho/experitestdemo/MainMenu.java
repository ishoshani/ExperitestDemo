package com.example.isho.experitestdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.TimeZone;

public class MainMenu extends AppCompatActivity {
    Button GameButton;
    Button RecordsButton;
    Button LogButton;
    MyLog log;
    TextView wifiText;
    TextView GPSText;
    WebView webBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupStats();
        log = MyLog.getInstance(getFilesDir());
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
        LogButton = (Button)findViewById(R.id.LogsButton);
        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLog(view);
            }
        });

    }
    public void launchGame(View view){
        log.log("pressed Launch Game");
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }
    public void launchRecords(View view){
        log.log("pressed view records");
        Intent intent = new Intent(this,RecordsActivity.class);
        startActivity(intent);
    }
    public  void launchLog(View view){
        log.log("pressed view logs");
        Intent intent = new Intent(this,LogActivity.class);
        startActivity(intent);
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
            //log.log("sent request for time at "+tzName);
            webBox.loadUrl("https://experitest-server.herokuapp.com?size=12px&tz="+tzName);
        }

    }
}
