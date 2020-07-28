package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
           Intent intent = new Intent(this, Walkthrough.class);
           startActivity(intent);
           finish();
        }
    }