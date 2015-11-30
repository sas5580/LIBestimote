package com.estimote.examples.demos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.estimote.examples.demos.R;


/**
 * Created by alicia on 2015-11-28.
 */
public class SplashScreen extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //Create new thread for splash timer
        Thread timerThread = new Thread() {
            public void run() {
                try {//try to display splash for 4 seconds
                    sleep(3000);
                } catch (InterruptedException e) {//otherwise print error
                    e.printStackTrace();
                } finally {//display the next screen
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        };
        timerThread.start();
    }
}
