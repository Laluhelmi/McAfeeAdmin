package com.example.l.macprojectadmin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    ImageView gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        gambar = (ImageView) findViewById(R.id.screen1);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        gambar.startAnimation(fade);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fade2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade2);
                gambar.startAnimation(fade2);

                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left, R.anim.left_exit);
                finish();
            }

        }, 3000L);
    }}

