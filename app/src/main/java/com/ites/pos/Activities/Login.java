package com.ites.pos.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ites.pos.Models.User;
import com.ites.pos.NetworkController;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Formatter;

/**
 * Created by wannix on 10/12/2016.
 */

public class Login extends AppCompatActivity {
    private static final String SNACK_ACTION_COLOR = "#387ef4";
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<User> allUserList = new ArrayList<>();

    // UI references
    private Spinner uname;
    private EditText passwd;
    private Button lgn_btn;
    private View login_progress;

    // authenticate
    private String username, userId;
    private int userType;

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
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        uname = (Spinner) findViewById(R.id.uname);
        passwd = (EditText) findViewById(R.id.passwd);
        lgn_btn = (Button) findViewById(R.id.login_btn);
        login_progress = findViewById(R.id.progressBar);

        /*WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        Log.d("your IP : ", android.text.format.Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));
        Log.d("Mac Add : ", wifiManager.getConnectionInfo().getMacAddress());*/

        passwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    attemptLogin();
                }
                return false;
            }
        });

        lgn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(getParent());
                attemptLogin();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.login_spinner_item, userList);

        uname.setAdapter(dataAdapter);

        uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                username = parentView.getItemAtPosition(position).toString();

                // extract userID
                Iterator<User> i = allUserList.iterator();
                while (i.hasNext()) {
                    User u = i.next();
                    if (u.getUserName() == username) {
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null
                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText
                && !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                dismissKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void dismissKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
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
            makeNetworkCall();

            return true;
        }

        private void makeNetworkCall() {
            NetworkController ntCtrl = new NetworkController(getApplicationContext());
            ntCtrl.authenticateUser(username, password, new com.ites.pos.Interfaces.SAMs.UserAuth() {
                @Override
                public void gotUserAuth(String data) {
                    try {
                        JSONArray responseArray = new JSONArray(data);

                        // invalid logins returns [{null}]
                        if (responseArray.getJSONObject(0) == null) {
                            // make the JSONException throws
                            responseArray.getJSONObject(0);
                        } else {
                            for(User u: allUserList){
                                if(userId.equals(u.getUserId())){
                                    userType = u.getUserType();
                                    break;
                                }
                            }

                            // valid activity_login re direct to select restaurant
                            showProgress(false);
                            Intent i = new Intent(Login.this, LoginSuccess.class);
                            i.putExtra("username", username);
                            i.putExtra("userId", userId);
                            i.putExtra("validatedResponse", data);
                            i.putExtra("userType", userType);
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
                public void errorUserAuth(VolleyError error) {
                    Snackbar.make(((ViewGroup)findViewById(android.R.id.content)).getChildAt(0),"Error Occurred!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // retry making network call
                            makeNetworkCall();
                        }
                    }).setActionTextColor(Color.parseColor(SNACK_ACTION_COLOR)).show();
                }
            });
        }
    }
}