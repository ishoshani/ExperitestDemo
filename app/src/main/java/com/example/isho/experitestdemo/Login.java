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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.Permission;
import java.util.Locale;

public class Login extends AppCompatActivity {
    Button loginButton;
    EditText topField;
    EditText bottomField;
    TextView wifiText;
    TextView GPSText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button)findViewById(R.id.loginButton);
        topField = (EditText)findViewById(R.id.matchText1);
        bottomField =(EditText) findViewById(R.id.matchText2);
        setupStats();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Login",topField.getText().toString());
                Log.d("Login",bottomField.getText().toString());
                if(topField.getText().toString().equals(bottomField.getText().toString())){
                    Log.d("Login","Success");
                    GoToNextActivity(view);


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
        if (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION")== PackageManager.PERMISSION_GRANTED) {
            Criteria compare = new Criteria();
            compare.setAccuracy(Criteria.ACCURACY_COARSE);
            Location current_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = current_location.getLatitude();
            double lon = current_location.getLongitude();
            Log.d("Stats", lat+" : "+lon);
            GPSText.setText(String.format(Locale.getDefault(),"Latitude:%.2f  Longitude:%.2f", lat, lon));
        }
        else{
            Log.d("Stats", "didn't get permissions");
        }
    }

}
