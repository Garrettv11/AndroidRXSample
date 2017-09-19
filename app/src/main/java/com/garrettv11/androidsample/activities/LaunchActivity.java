package com.garrettv11.androidsample.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.garrettv11.androidsample.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //navigate to the random number activity after a second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent loginIntent = new Intent(LaunchActivity.this, RandomNumberActivity.class);
                LaunchActivity.this.startActivity(loginIntent);
                LaunchActivity.this.finish();
            }
        }, 1000);

    }
}
