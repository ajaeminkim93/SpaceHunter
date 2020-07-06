package com.example.spacehunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                    Intent startgame = new Intent(getApplication(), MainActivity.class);
                    startActivity(startgame);
                    finish();
                } catch (InterruptedException e) {}
            } // end of run
        }; // end of thread

        myThread.start();


    } // end of on create

}
