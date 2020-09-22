package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    boolean isLogged;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        Thread  th = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                    if(isLogged == true){
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    } else{
                        startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();

    }

    private void init() {
        sp = getSharedPreferences("logStatus", MODE_PRIVATE );
        isLogged = sp.getBoolean("isLogged", false);
        user = sp.getString("user", "none");
    }
}
