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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ites.pos.Models.User;
import com.ites.pos.NetworkController;
import com.ites.pos.ResponseCallBack;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Itess001 on 10/12/2016.
 */

public class Login extends AppCompatActivity {

    ArrayList<String> userList = new ArrayList<>();
    ArrayList<User> allUserList = new ArrayList<>();

    // UI references
    private Spinner uname;
    private EditText passwd;
    private Button lgn_btn;
    private View login_progress;

    // authenticate
    private String username, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent i = getIntent();

        try {
            JSONArray jArry = new JSONArray(i.getStringExtra("allUsersResp"));
            for (int j = 0; j < jArry.length(); j++) {
                JSONObject jObj = jArry.getJSONObject(j);
                User u = new User(jObj);
                allUserList.add(u);

                // extract usernames list
                userList.add(u.getUserName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){
            Toast.makeText(this, "Connection is not available! Check your connection and try again!", Toast.LENGTH_LONG).show();
        }

        uname = (Spinner) findViewById(R.id.uname);
        passwd = (EditText) findViewById(R.id.passwd);
        lgn_btn = (Button) findViewById(R.id.login_btn);
        login_progress = findViewById(R.id.progressBar);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.login_spinner_item, userList);

        uname.setAdapter(dataAdapter);

        uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                username = parentView.getItemAtPosition(position).toString();

                // extract userID
                Iterator<User> i = allUserList.iterator();
                while (i.hasNext()){
                    User u = i.next();
                    if(u.getUserName() == username){
                        userId = u.getUserId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // skip
            }
        });
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

    // Shows the progress bar
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
            NetworkController ntCtrl = new NetworkController(getApplicationContext());
            ntCtrl.authenticateUser(username, password, new ResponseCallBack() {
                @Override
                public void gotAllUsers(String data) {

                }

                @Override
                public void gotUserAuth(String data) {
                    try {
                        JSONArray responseArray = new JSONArray(data);

                        // invalid logins returns [{null}]
                        if (responseArray.getJSONObject(0) == null) {
                            // make the JSONException throws
                            responseArray.getJSONObject(0);
                        } else {
                            // valid activity_login re direct to select restaurant
                            showProgress(false);
                            Intent i = new Intent(Login.this, LoginSuccess.class);
                            i.putExtra("username", username);
                            i.putExtra("userId", userId);
                            i.putExtra("validatedResponse", data);
                            startActivity(i);
                            finish();
                        }
                    } catch (JSONException ex) {
                        // notify user : Invalid activity_login
                        Toast.makeText(getApplicationContext(), "Wrong password! Try again!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }

                @Override
                public void gotTableConfigs(String data) {

                }

                @Override
                public void gotOpenTableDetails(String data) {

                }

                @Override
                public void gotReservationRoomList(String data) {

                }

                @Override
                public void gotHouseAccList(String data) {

                }
            });

            return true;
        }
    }
}