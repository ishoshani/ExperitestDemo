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
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.Permission;
import java.util.Locale;
import java.util.TimeZone;

public class Login extends AppCompatActivity {
    Button loginButton;
    EditText topField;
    EditText bottomField;
    TextView wifiText;
    TextView GPSText;
    WebView webBox;
    MyLog log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log = MyLog.getInstance(getApplication().getFilesDir());
        loginButton = (Button)findViewById(R.id.loginButton);
        topField = (EditText)findViewById(R.id.matchText1);
        topField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    log.log("Typed into Top Field "+topField.getText().toString());
                }
            }
        }
        );
        bottomField =(EditText) findViewById(R.id.matchText2);
        bottomField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    log.log("Typed into Bottom Field "+topField.getText().toString());
                }
            }
        });
        setupStats();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log.log("pressed Login Button");
                 if(topField.getText().toString().equals(bottomField.getText().toString())){
                    log.log("Successfully logged in");
                    GoToNextActivity(view);
                }
                else{
                     log.log("Failed to login");
                 }
            }
        });
    }
    public void GoToNextActivity(View view){
        Intent intent = new Intent(this, MainMenu.class);
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
            log.log("sent request for time at "+tzName);
            webBox.loadUrl("https://experitest-server.herokuapp.com?size=12px&tz="+tzName);
        }


    }

}
