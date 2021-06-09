package com.example.portfolioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView animation;
    TextView app_name;
    //LinearLayout SplashLayout;
    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        animation=findViewById(R.id.lottie_anim);
        app_name=findViewById(R.id.App_Name);
        //SplashLayout=findViewById(R.id.Splash_layout);

        timer = new Thread(){
            @Override
            public void run() {
                try{
                    synchronized (this){
                        wait(4000);
                    }
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }finally {
                    Intent i = new Intent(SplashActivity.this,Startactivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();


        //SplashLayout.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        //app_name.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        //animation.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

    }
}