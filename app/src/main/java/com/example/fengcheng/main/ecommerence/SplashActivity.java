package com.example.fengcheng.main.ecommerence;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;


public class SplashActivity extends AppCompatActivity {
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        avLoadingIndicatorView = findViewById(R.id.anim);
        avLoadingIndicatorView.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                avLoadingIndicatorView.hide();
                finish();

            }
        }, 3000);
    }
}
