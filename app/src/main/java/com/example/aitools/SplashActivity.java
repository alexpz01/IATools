package com.example.aitools;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timer t = new Timer();

        // Delay activity change by 3 seconds
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent toMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivity);
                    overridePendingTransition(0, R.anim.splash_out);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.schedule(tt, 0);

    }
}
