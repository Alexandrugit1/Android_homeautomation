package com.example.homeautomation.StartingPart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homeautomation.AuthenticationPart.ActivityLogin;
import com.example.homeautomation.UserDetails;
import com.example.homeautomation.ConnectionPart.ActivityConnectAccessHome;
import com.example.homeautomation.R;
import com.google.gson.Gson;

public class ActivitySplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initializeSplashScreen();
    }

    private void initializeSplashScreen()
    {
        LogoLauncher launcher = new LogoLauncher();
        launcher.start();
    }

    private UserDetails retrieveUserFromSharedPreferences()
    {
        SharedPreferences preferences = getSharedPreferences("APP_USER", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("currentUser", "");

        return gson.fromJson(json, UserDetails.class);
    }

    public class LogoLauncher extends Thread
    {
        public void run()
        {
            try
            {
                sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

            UserDetails details = retrieveUserFromSharedPreferences();
            Intent intent;

            if(details != null)
                intent = new Intent(ActivitySplashScreen.this, ActivityConnectAccessHome.class);
            else intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);

            startActivity(intent);
            finish();
        }
    }
}