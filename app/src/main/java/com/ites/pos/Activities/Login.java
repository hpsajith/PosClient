package com.ites.pos.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Itess001 on 10/12/2016.
 */

public class Login extends AppCompatActivity {

    ArrayList<String> userList = new ArrayList<>();

    // authenticate the user
    final String urlAuthUser = "http://10.1.1.66:8080/UserController/validateLoginUser";
//    final String urlAuthUser = "http://192.168.43.178:8080/UserController/validateLoginUser";

    // UI references
    private Spinner uname;
    private EditText passwd;
    private Button lgn_btn;
    private View login_progress;

    // athenticate
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        userList = i.getStringArrayListExtra("userList");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname = (Spinner) findViewById(R.id.uname);
        passwd = (EditText) findViewById(R.id.passwd);
        lgn_btn = (Button) findViewById(R.id.login_btn);
        login_progress = findViewById(R.id.progressBar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        lgn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.login_spinner_item, userList);

        uname.setAdapter(dataAdapter);

        uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                username = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // skip
            }
        });
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }

    private void attemptLogin() {
        // perform the user activity_login attempt.
        showProgress(true);

        String password = passwd.getText().toString();

        UserAuth userAuth = new UserAuth(username, password);
        userAuth.execute((Void) null);
    }

    /**
     * Shows the progress
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // in greater versions than HONEYCOMB
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            login_progress.setVisibility(show ? TextView.VISIBLE : TextView.GONE);
            login_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_progress.setVisibility(show ? TextView.VISIBLE : TextView.GONE);

                    //disable input fields
                    uname.setEnabled(!show);
                    passwd.setEnabled(!show);
                }
            });
        } else {
            // in older versions
            login_progress.setVisibility(show ? TextView.VISIBLE : TextView.GONE);

            //disable input fields
            uname.setEnabled(!show);
            passwd.setEnabled(!show);
        }
    }

    public class UserAuth extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserAuth(String uname, String psswd) {
            username = uname;
            password = psswd;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String urlAuth;
            urlAuth = urlAuthUser + "?username=" + username + "&password=" + password;

            try {
                // Simulate network access.
                final RequestQueue rqlgn = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest reqlgn = new JsonArrayRequest(urlAuth,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONArray responseArray;

                                try {
                                    responseArray = new JSONArray(response.toString());

                                    // invalid logins returns [{null}]
                                    if (responseArray.getJSONObject(0) == null) {
                                        // make the JSONException throws
                                        responseArray.getJSONObject(0);
                                    } else {
                                        // valid activity_login re direct to select restaurant
                                        showProgress(false);
                                        Intent i = new Intent(Login.this, LoginSuccess.class);
                                        i.putExtra("username", username);
                                        i.putExtra("validatedResponse", response.toString());
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException ex) {
                                    // notify user : Invalid activity_login
                                    Toast.makeText(getApplicationContext(), "Wrong password! Try again!", Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Volley Debug", "Error: " + error.getMessage());
                    }
                });

                rqlgn.add(reqlgn);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }
    }
}