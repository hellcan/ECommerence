package com.example.fengcheng.main.ecommerence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * entry activity
 */


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //log in fragment
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frame_container, new LoginFragment(), "logFgt").
                commit();

    }
}
