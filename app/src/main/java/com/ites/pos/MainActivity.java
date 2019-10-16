package com.ites.pos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ites.pos.Activities.PosSplash;
import com.ites.pos.Activities.fragments.RoomFragment;
import com.ites.pos.Interfaces.SAMs.HouseAccList;
import com.ites.pos.Interfaces.SAMs.ReservationRoomList;
import com.ites.pos.Interfaces.SAMs.TableConfigs;
import com.ites.pos.Models.TableConfig;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private static final String SNACK_ACTION_COLOR = "#387ef4";
    private ProgressBar loading;
    private String userId,restID, houseAccList, reservRoomList;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get session info
        final SharedPreferences session = getApplicationContext().getSharedPreferences("session", 0);

        restID = session.getString("restaurantId", "0");
        userId= session.getString("userId", "0");
        loading = (ProgressBar) findViewById(R.id.tableLoadingProgress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(session.getString("restaurantName", "Restaurant Name"));
        toolbar.setNavigationIcon(R.drawable.ic_nav_toggle_white_32dp);
        setSupportActionBar(toolbar);
        final DrawerLayout navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // configs actionbar and status bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // set nav_header params
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email);
        Button logout = (Button) header.findViewById(R.id.logout);
        name.setText("User: " + session.getString("username", "Username"));
        email.setText("Meal Period: " + session.getString("mealPeriodName", "Meal Period"));

        // logout listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(MainActivity.this, PosSplash.class);
                session.edit().clear().apply();
                startActivity(logout);
                finish();
            }
        });

        // navigation menu toggle
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navDrawer.openDrawer(GravityCompat.START, true);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // setup view pager, making network calls
        makeNetworkCalls(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Fragment getFragment(int position, String tableConfigs, String houseAccList, String reservRoomList) {
        switch (position) {
            case 0:
                RoomFragment fragR1 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR1;
            case 1:
                RoomFragment fragR2 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR2;
            case 2:
                RoomFragment fragR3 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR3;
            case 3:
                RoomFragment fragR4 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR4;
            case 4:
                RoomFragment fragR5 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR5;
            case 5:
                RoomFragment fragR6 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR6;
            case 6:
                RoomFragment fragR7 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR7;
            case 7:
                RoomFragment fragR8 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR8;
            case 8:
                RoomFragment fragR9 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR9;
            case 9:
                RoomFragment fragR10 = RoomFragment.newInstance(tableConfigs, houseAccList, reservRoomList);
                return fragR10;
            default:
                return null;
        }
    }

    private void makeNetworkCalls(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        final NetworkController netCtrl = new NetworkController(getApplicationContext());
        final List<TableConfig> configs = new ArrayList<>();

        netCtrl.getHouseAccListList(new HouseAccList() {
            @Override
            public void gotHouseAccList(String data) {
                houseAccList = data;
            }

            @Override
            public void errorHouseAccList(VolleyError error) {
                makeNetworkCalls(viewPager);
            }
        });

        netCtrl.getReservationRoomList(new ReservationRoomList() {
            @Override
            public void gotReservationRoomList(String data) {
                reservRoomList = data;
            }

            @Override
            public void errorReservationRoomList(VolleyError error) {
                makeNetworkCalls(viewPager);
            }
        });

        netCtrl.getAllTableConfigs(restID,userId, new TableConfigs() {
            @Override
            public void gotTableConfigs(String responseStr) {
                // set progressbar invisible
                loading.setVisibility(ProgressBar.INVISIBLE);

                try {
                    JSONArray response = new JSONArray(responseStr);

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject respObj = new JSONObject(response.getJSONObject(i).toString());
                        configs.add(new TableConfig(respObj));
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                Set<String> roomSet = new TreeSet<>();
                Map<String, List<TableConfig>> configMap = new HashMap<>();

                for (TableConfig config : configs) {
                    if (configMap.containsKey(config.getRoomName())) {
                        // Room exists in Map, add config to arraylist
                        configMap.get(config.getRoomName()).add(config);
                    } else {
                        // Room doesn't exist in Map
                        List<TableConfig> tmp = new ArrayList<>();
                        tmp.add(config);
                        configMap.put(config.getRoomName(), tmp);
                    }

                    roomSet.add(config.getRoomName());
                }

                int i = 0;
                for (String aRoom : roomSet) {
                    String tableConfigs;

                    // convert configMap to JSONArray
                    JSONArray configMapJsonArray = new JSONArray();
                    for (int j = 0; j < configMap.get(aRoom).size(); j++) {
                        configMapJsonArray.put(configMap.get(aRoom).get(j).toJSONObject());
                    }

                    tableConfigs = configMapJsonArray.toString();

                    adapter.addFragment(getFragment(i, tableConfigs, houseAccList, reservRoomList), aRoom);
                    i++;
                }
                viewPager.setAdapter(adapter);

                netCtrl.clearRequestQueue();
            }

            @Override
            public void errorTableConfigs(VolleyError error) {
                Snackbar.make(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "Error Occurred!", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // retry making network calls
                        makeNetworkCalls(viewPager);
                    }
                }).setActionTextColor(Color.parseColor(SNACK_ACTION_COLOR)).show();
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}