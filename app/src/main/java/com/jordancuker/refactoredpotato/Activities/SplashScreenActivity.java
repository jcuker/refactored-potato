package com.jordancuker.refactoredpotato.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jordancuker.refactoredpotato.BuildConfig;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        boolean blnFirstLaunch = sharedPreferences.getBoolean("first_launch", true);

        Intent intent;

        if(blnFirstLaunch){
            intent = new Intent(this, FirstLaunchActivity.class);
        }
        else{
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);

        finish();
    }

}