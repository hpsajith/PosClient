package com.ites.pos.Activities.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ites.pos.Activities.ItemPunching;
import com.ites.pos.GuestFragmentType;
import com.ites.pos.ManagerListAdapter;
import com.ites.pos.Models.HouseAccount;
import com.ites.pos.Models.ReservationRoom;
import com.ites.pos.NetworkController;
import com.ites.pos.ReservRoomAdapter;
import com.ites.pos.ResponseCallBack;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 11/28/16.
 */

public class SelectGuestFragment extends Fragment {
    private static final String SNACK_TEXT_COLOR = "#387ef4";

    private int fragmentType;
    private String restaurantId, waiterId, mealId, restRoomId, tableId;
    private RecyclerView.Adapter adapter;
    private Button basicBtn, complBtn, extraBtn, cntBtn;
    private NumberPicker adultCount, kidsCount;

    public SelectGuestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<HouseAccount> houseAccountsList = new ArrayList<>();
        List<ReservationRoom> reservationRoomList = new ArrayList<>();

        Bundle callbackData = getArguments();

        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        waiterId = session.getString("userId", "0");
        mealId = session.getString("mealId", "0");
        restaurantId = session.getString("restaurantId", "0");

        final View fragmentView = inflater.inflate(R.layout.fragment_select_guest, container, false);

        adultCount = (NumberPicker) fragmentView.findViewById(R.id.adultCount);
        kidsCount = (NumberPicker) fragmentView.findViewById(R.id.childrenCount);
        LinearLayout roomListViewHeader = (LinearLayout) fragmentView.findViewById(R.id.reservRoomList);
        LinearLayout managerListViewHeader = (LinearLayout) fragmentView.findViewById(R.id.managerList);
        basicBtn = (Button) fragmentView.findViewById(R.id.basicBtn);
        complBtn = (Button) fragmentView.findViewById(R.id.complementaryBtn);
        extraBtn = (Button) fragmentView.findViewById(R.id.extraBtn);
        cntBtn = (Button) fragmentView.findViewById(R.id.cntBtn);

        final NetworkController netCtrl = new NetworkController(getContext());

        cntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentType == GuestFragmentType.WALK_IN) {
                    // make sure that button action is synchronized
                    cntBtn.setEnabled(false);
                    cntBtn.setBackgroundColor(Color.GRAY);

                    // network call
                    netCtrl.sendGuestDetails(getGenericObject(getSelectedItem()), new ResponseCallBack() {
                        @Override
                        public void gotAllUsers(String data) {

                        }

                        @Override
                        public void gotUserAuth(String data) {

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

                        @Override
                        public void gotRestaurantItems(String data) {
                            // redirect to itemPunching
                            Intent nxt = new Intent(getContext(), ItemPunching.class);
                            nxt.putExtra("restaurantItems", data);
                            startActivity(nxt);
                            getActivity().finish();
                        }
                    });

                    // notification
                    Snackbar snackbar = Snackbar.make(fragmentView, "Guest Details sent!", Snackbar.LENGTH_SHORT);
                    TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.parseColor(SNACK_TEXT_COLOR));
                    if (Build.VERSION.SDK_INT >= 17) {
                        snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    } else {
                        tv.setGravity(Gravity.CENTER);
                    }
                    snackbar.show();

                } else if (fragmentType == GuestFragmentType.MANAGER_LIST) {
                    proceedButtonClick(netCtrl, fragmentView, "You haven't set a Manager House Account!");
                }
            }
        });

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedButtonClick(netCtrl, fragmentView, "You haven't selected a Reservation Room!");
            }
        });

        complBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedButtonClick(netCtrl, fragmentView, "You haven't selected a Reservation Room!");
            }
        });

        extraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedButtonClick(netCtrl, fragmentView, "You haven't selected a Reservation Room!");
            }
        });

        try {
            if (this.fragmentType == GuestFragmentType.ROOM_LIST) {
                // set header views
                roomListViewHeader.setVisibility(View.VISIBLE);
                managerListViewHeader.setVisibility(View.GONE);

                // set buttons
                basicBtn.setVisibility(View.VISIBLE);
                complBtn.setVisibility(View.VISIBLE);
                extraBtn.setVisibility(View.VISIBLE);
                cntBtn.setVisibility(View.GONE);

                JSONArray roomList = new JSONArray(callbackData.getString("roomList"));
                for (int i = 0; i < roomList.length(); i++) {
                    reservationRoomList.add(new ReservationRoom(roomList.getJSONObject(i)));
                }

            } else if (this.fragmentType == GuestFragmentType.MANAGER_LIST) {
                // set header views
                roomListViewHeader.setVisibility(View.GONE);
                managerListViewHeader.setVisibility(View.VISIBLE);

                // set buttons
                basicBtn.setVisibility(View.GONE);
                complBtn.setVisibility(View.GONE);
                extraBtn.setVisibility(View.GONE);
                cntBtn.setVisibility(View.VISIBLE);

                JSONArray mgrList = new JSONArray(callbackData.getString("managerList"));
                for (int i = 0; i < mgrList.length(); i++) {
                    houseAccountsList.add(new HouseAccount(mgrList.getJSONObject(i)));
                }
            } else if (this.fragmentType == GuestFragmentType.WALK_IN) {
                // hide headers
                roomListViewHeader.setVisibility(View.INVISIBLE);
                managerListViewHeader.setVisibility(View.GONE);

                // set buttons
                basicBtn.setVisibility(View.GONE);
                complBtn.setVisibility(View.GONE);
                extraBtn.setVisibility(View.GONE);
                cntBtn.setVisibility(View.VISIBLE);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // setup number pickers
        adultCount.setMinValue(1);
        adultCount.setMaxValue(20);
        adultCount.setWrapSelectorWheel(true);
        adultCount.canScrollHorizontally(View.SCROLL_INDICATOR_RIGHT);

        kidsCount.setMinValue(0);
        kidsCount.setMaxValue(20);
        kidsCount.setWrapSelectorWheel(true);
        kidsCount.canScrollHorizontally(View.SCROLL_INDICATOR_RIGHT);

        if (this.fragmentType == GuestFragmentType.MANAGER_LIST) {
            adapter = ManagerListAdapter.getInstance(houseAccountsList);
        } else if (this.fragmentType == GuestFragmentType.ROOM_LIST) {
            adapter = ReservRoomAdapter.getInstance(reservationRoomList);
        } else {
            adapter = null;
        }

        RecyclerView listView = (RecyclerView) fragmentView.findViewById(R.id.selectGuestList);
        RecyclerView.LayoutManager mgr = new LinearLayoutManager(getContext());
        listView.setLayoutManager(mgr);
        listView.setAdapter(adapter);

        return fragmentView;
    }

    private void proceedButtonClick(NetworkController netCtrl, View fragmentView, String msg) {
        if (getSelectedItem() != null) {
            // make sure that button action is synchronized
            if(fragmentType == GuestFragmentType.MANAGER_LIST){
                cntBtn.setEnabled(false);
                cntBtn.setBackgroundColor(Color.GRAY);
            }else if(fragmentType == GuestFragmentType.ROOM_LIST){
                basicBtn.setEnabled(false);
                basicBtn.setBackgroundColor(Color.GRAY);
                complBtn.setEnabled(false);
                complBtn.setBackgroundColor(Color.GRAY);
                extraBtn.setEnabled(false);
                extraBtn.setBackgroundColor(Color.GRAY);
            }

            // network call
            netCtrl.sendGuestDetails(getGenericObject(getSelectedItem()), new ResponseCallBack() {
                @Override
                public void gotAllUsers(String data) {

                }

                @Override
                public void gotUserAuth(String data) {

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

                @Override
                public void gotRestaurantItems(String data) {
                    // redirect to itemPunching
                    Intent nxt = new Intent(getContext(), ItemPunching.class);
                    nxt.putExtra("restaurantItems", data);
                    startActivity(nxt);
                    getActivity().finish();
                }
            });

            // notification
            Snackbar snackbar = Snackbar.make(fragmentView, "Guest Details sent!", Snackbar.LENGTH_SHORT);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.parseColor(SNACK_TEXT_COLOR));
            if (Build.VERSION.SDK_INT >= 17) {
                snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                tv.setGravity(Gravity.CENTER);
            }
            snackbar.show();

            // redirect to itemPunching


        } else {
            // validation failed notification
            Snackbar snackbar = Snackbar.make(fragmentView, msg, Snackbar.LENGTH_SHORT);
            if (Build.VERSION.SDK_INT >= 17) {
                snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setGravity(Gravity.CENTER);
            }
            snackbar.show();
        }
    }

    private JSONObject getGenericObject(Object selected) {
        JSONObject genericObj = new JSONObject();
        try {
            genericObj.put("restaurantId", restaurantId);
            genericObj.put("waiterId", waiterId);
            genericObj.put("mealId", mealId);
            genericObj.put("roomId", restRoomId);
            genericObj.put("tableId", tableId);
            genericObj.put("guestFname", ((selected != null) && (fragmentType == GuestFragmentType.ROOM_LIST)) ? ((ReservationRoom) selected).getfName() : "");
            genericObj.put("guestLname", ((selected != null) && (fragmentType == GuestFragmentType.ROOM_LIST)) ? ((ReservationRoom) selected).getlName() : "");
            genericObj.put("adultsCount", adultCount.getValue());
            genericObj.put("kidsCount", kidsCount.getValue());
            genericObj.put("confNo", ((selected != null) && (fragmentType == GuestFragmentType.ROOM_LIST)) ? ((ReservationRoom) selected).getConfNo() : "0");
            genericObj.put("reservRoomNo", ((selected != null) && (fragmentType == GuestFragmentType.ROOM_LIST)) ? ((ReservationRoom) selected).getRoomNo() : "");
            genericObj.put("foodPackage", ((selected != null) && (fragmentType == GuestFragmentType.ROOM_LIST)) ? ((ReservationRoom) selected).getPakage() : "");
            genericObj.put("hAccountId", ((selected != null) && (fragmentType == GuestFragmentType.MANAGER_LIST)) ? ((HouseAccount) selected).gethAccountId() : "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return genericObj;
    }

    public void setFragmentType(int fragmentType, String[] tableSignatures) {
        this.fragmentType = fragmentType;
        this.restRoomId = tableSignatures[0];
        this.tableId = tableSignatures[1];
    }

    public Object getSelectedItem() {
        Object obj = null;
        if (adapter != null) {
            if (fragmentType == GuestFragmentType.ROOM_LIST) {
                obj = ((ReservRoomAdapter) adapter).getSelectedRoom();
            } else if (fragmentType == GuestFragmentType.MANAGER_LIST) {
                obj = ((ManagerListAdapter) adapter).getSelectedHouseAccount();
            }
        }
        return obj;
    }
}