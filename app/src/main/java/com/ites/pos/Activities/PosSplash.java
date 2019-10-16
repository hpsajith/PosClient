package com.ites.pos.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ites.pos.Interfaces.SAMs.AllUsers;
import com.ites.pos.NetworkController;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by wannix on 10/12/2016.
 */

public class PosSplash extends AppCompatActivity {
    private static final String SNACK_ACTION_COLOR = "#387ef4";
    private static String deviceIP = "0.0.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        deviceIP = Formatter.formatIpAddress(wifiMgr.getConnectionInfo().getIpAddress());


        // hide navigation bar and status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pos_splash);

        makeNetworkCall();
    }

    private void makeNetworkCall() {
        NetworkController ntCtrl = new NetworkController(getApplicationContext());
        ntCtrl.getAllUsers(new AllUsers() {
            @Override
            public void gotAllUsers(final String data) {
                // handler to redirect the splash
                int SPLASH_TIME_OUT = 3000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jArry = new JSONArray(data);
                            JSONObject jObj = jArry.getJSONObject(0);
                            String virginDataStr = jObj.getString("posMachine");
                            String[] devices = virginDataStr.split("\"");

                            boolean valFlag = false;

                            for (int i = 0; i < devices.length; i++) {
                                    if (deviceIP.equals(devices[i])) {
                                        valFlag = true;
                                        Intent in = new Intent(PosSplash.this, Login.class);
                                        in.putExtra("allUsersResp", data);
                                        startActivity(in);
                                        finish();
                                    }
                                //}
                            }
                            if (!valFlag) {

                                Snackbar.make(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "Device has not been configured!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // retry making network call
                                        makeNetworkCall();
                                    }
                                }).setActionTextColor(Color.parseColor(SNACK_ACTION_COLOR)).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, SPLASH_TIME_OUT);
            }

            @Override
            public void errorAllUsers(VolleyError error) {
                Snackbar.make(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "Error Occurred! Check the Network Connectivity.", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // retry making network call
                        makeNetworkCall();
                    }
                }).setActionTextColor(Color.parseColor(SNACK_ACTION_COLOR)).show();
            }
        });
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }
}