package com.ites.pos.Activities.pos_splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ites.pos.Activities.login.Login;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Itess001 on 10/12/2016.
 */

public class PosSplash extends AppCompatActivity {
    final String TAG = "Debug: ";
    ArrayList<String> userList = new ArrayList<>();

    // get user list
        final String urlGetUsers = "http://10.1.1.66:8080/UserController/getAllUsers";
//    final String urlGetUsers = "http://192.168.43.178:8080/UserController/getAllUsers";

    // splash waiting time
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide navigation bar and status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pos_splash);

        final RequestQueue rq = Volley.newRequestQueue(PosSplash.this);

        JsonArrayRequest req = new JsonArrayRequest(urlGetUsers,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray users = new JSONArray(response.toString());
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                userList.add(user.getString("userName"));
                            }
                            Log.d(TAG, "Userss>>>" + users.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        rq.add(req);

        // handler to redirect the splash
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(PosSplash.this, Login.class);
                i.putStringArrayListExtra("userList", userList);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
        return;
    }
}