package com.ites.pos.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ites.pos.MainActivity;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginSuccess extends AppCompatActivity {
    private String username = null;
    private String selectedRestaurantName = null;
    private String selectedMealPeriodName = null;
    private String selectedRestaurantId = null;
    private String selectedMealPeriodId = null;
    private ArrayList<String> restId = new ArrayList<>();
    private ArrayList<String> mealPeriodId = new ArrayList<>();
    private ArrayList<String> restName = new ArrayList<>();
    private ArrayList<String> mealPeriodName = new ArrayList<>();
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        SharedPreferences session = getApplicationContext().getSharedPreferences("session", 0);
        editor = session.edit();

        Spinner sel_rest = (Spinner) findViewById(R.id.select_restaurant);
        Spinner sel_meal = (Spinner) findViewById(R.id.select_meal);
        Button cont_btn = (Button) findViewById(R.id.select_rest_done);

        cont_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Continue();
            }
        });

        Intent i = getIntent();
        username = i.getStringExtra("username");

        JSONArray responseArray, restaurantList, mealPeriodList;

        try {
            responseArray = new JSONArray(i.getStringExtra("validatedResponse"));
            restaurantList = new JSONArray(responseArray.getJSONObject(0).getString("restuarantList"));
            mealPeriodList = new JSONArray(responseArray.getJSONObject(0).getString("mealPeriodList"));

            int c1 = 0, c2 = 0;
            while (c1 < restaurantList.length()) {
                try {
                    JSONObject obj = restaurantList.getJSONObject(c1);

                    restName.add(obj.getString("rname"));
                    restId.add(obj.getString("restrauntId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                c1++;
            }

            while (c2 < mealPeriodList.length()) {
                try {
                    JSONObject obj = mealPeriodList.getJSONObject(c2);

                    mealPeriodName.add(obj.getString("mealPeriadName"));
                    mealPeriodId.add(obj.getString("mealPeriodId"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                c2++;
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        ArrayAdapter<String> spinAdapterRestaurant = new ArrayAdapter<>(this, R.layout.login_spinner_item, restName);
        ArrayAdapter<String> spinAdapterMeal = new ArrayAdapter<>(this, R.layout.login_spinner_item, mealPeriodName);

        sel_rest.setAdapter(spinAdapterRestaurant);
        sel_rest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRestaurantName = parent.getItemAtPosition(position).toString();
                selectedRestaurantId = restId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // to implement
            }
        });

        sel_meal.setAdapter(spinAdapterMeal);
        sel_meal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMealPeriodName = parent.getItemAtPosition(position).toString();
                selectedMealPeriodId = mealPeriodId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // to implement
            }
        });
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }

    public void Continue() {
        editor.putString("username", username);
        editor.putString("restaurantId", selectedRestaurantId);
        editor.putString("restaurantName", selectedRestaurantName);
        editor.putString("mealPeriodId", selectedMealPeriodId);
        editor.putString("mealPeriodName", selectedMealPeriodName);
        editor.commit();

        Intent nxt = new Intent(LoginSuccess.this, MainActivity.class);
        startActivity(nxt);
        finish();
    }
}
