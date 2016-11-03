package com.ites.pos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ites.pos.Activities.rooms.Room1;
import com.ites.pos.Activities.rooms.Room10;
import com.ites.pos.Activities.rooms.Room2;
import com.ites.pos.Activities.rooms.Room3;
import com.ites.pos.Activities.rooms.Room4;
import com.ites.pos.Activities.rooms.Room5;
import com.ites.pos.Activities.rooms.Room6;
import com.ites.pos.Activities.rooms.Room7;
import com.ites.pos.Activities.rooms.Room8;
import com.ites.pos.Activities.rooms.Room9;
import com.ites.pos.Controllers.NetworkController;
import com.ites.pos.Controllers.ResponseCallBack;
import com.ites.pos.Models.TableConfig;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private String restID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get session info
        SharedPreferences session = getApplicationContext().getSharedPreferences("session", 0);

        restID = session.getString("restaurantId", "0");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(session.getString("restaurantName", "Restaurant Name"));
        toolbar.setNavigationIcon(R.drawable.ic_dehaze_white_36dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private Fragment getFragment(int position, String tableConfigs) {
        switch (position) {
            case 0:
                Room1 fragR1 = new Room1();
                Bundle argsR1 = new Bundle();
                argsR1.putString("tableConfigs", tableConfigs);
                fragR1.setArguments(argsR1);
                return fragR1;
            case 1:
                Room2 fragR2 = new Room2();
                Bundle argsR2 = new Bundle();
                argsR2.putString("tableConfigs", tableConfigs);
                fragR2.setArguments(argsR2);
                return fragR2;
            case 2:
                Room3 fragR3 = new Room3();
                Bundle argsR3 = new Bundle();
                argsR3.putString("tableConfigs", tableConfigs);
                fragR3.setArguments(argsR3);
                return fragR3;
            case 3:
                Room4 fragR4 = new Room4();
                Bundle argsR4 = new Bundle();
                argsR4.putString("tableConfigs", tableConfigs);
                fragR4.setArguments(argsR4);
                return fragR4;
            case 4:
                Room5 fragR5 = new Room5();
                Bundle argsR5 = new Bundle();
                argsR5.putString("tableConfigs", tableConfigs);
                fragR5.setArguments(argsR5);
                return fragR5;
            case 5:
                return new Room6();
            case 6:
                return new Room7();
            case 7:
                return new Room8();
            case 8:
                return new Room9();
            case 9:
                return new Room10();
            default:
                return null;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final NetworkController netCtrl = new NetworkController(getApplicationContext());

        final List<TableConfig> configs = new ArrayList<>();
        final ViewPager viewPagerRef = viewPager;

        netCtrl.getAllTableConfigs(restID, new ResponseCallBack() {
            @Override
            public void gotTableConfigs(String responseStr) {
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


                /*for (Iterator<String> room = roomSet.iterator(); room.hasNext(); ) {
                    String tableConfigs;
                    String roomName = room.next();

                    // convert configMap to JSONArray
                    JSONArray configMapJsonArray = new JSONArray();
                    for (int j = 0; j < configMap.get(roomName).size(); j++) {
                        configMapJsonArray.put(configMap.get(roomName).get(j).toJSONObject());
                    }

                    tableConfigs = configMapJsonArray.toString();

                    adapter.addFragment(getFragment(i, tableConfigs), roomName);
                    i++;
                }*/


                int i = 0;
                for (String aRoom : roomSet) {
                    String tableConfigs;

                    // convert configMap to JSONArray
                    JSONArray configMapJsonArray = new JSONArray();
                    for (int j = 0; j < configMap.get(aRoom).size(); j++) {
                        configMapJsonArray.put(configMap.get(aRoom).get(j).toJSONObject());
                    }

                    tableConfigs = configMapJsonArray.toString();

                    adapter.addFragment(getFragment(i, tableConfigs), aRoom);
                    i++;
                }
                viewPagerRef.setAdapter(adapter);

                netCtrl.clearRequestQueue();
            }

            @Override
            public void gotOpenTableDetails(String data) {
            }
        });
    }

    // back navigation button disabled
    @Override
    public void onBackPressed() {
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}