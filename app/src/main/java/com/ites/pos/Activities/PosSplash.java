package com.ites.pos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ites.pos.Interfaces.SAMs.AllUsers;
import com.ites.pos.NetworkController;
import com.ites.pos.main_activity.R;

/**
 * Created by wannix on 10/12/2016.
 */

public class PosSplash extends AppCompatActivity {
    private String allUsersRespStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide navigation bar and status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pos_splash);

        NetworkController ntCtrl = new NetworkController(getApplicationContext());
        ntCtrl.getAllUsers(new AllUsers() {
            @Override
            public void gotAllUsers(String data) {
                allUsersRespStr = data;
            }
        });

        // handler to redirect the splash
        int SPLASH_TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(PosSplash.this, Login.class);
                i.putExtra("allUsersResp", allUsersRespStr);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }
}