package com.ites.pos.Activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ites.pos.Activities.ItemPunching;
import com.ites.pos.Adapters.BillAdapter;
import com.ites.pos.Interfaces.GuestFragmentType;
import com.ites.pos.Interfaces.SAMs.CloseGuestBill;
import com.ites.pos.Interfaces.SAMs.OpenTableDetails;
import com.ites.pos.Interfaces.SAMs.PosGuestDetails;
import com.ites.pos.Interfaces.SAMs.PrintGuestBill;
import com.ites.pos.Interfaces.SAMs.RestaurantItems;
import com.ites.pos.MainActivity;
import com.ites.pos.Models.HouseAccount;
import com.ites.pos.Models.OrderBillItem;
import com.ites.pos.Models.ReservationRoom;
import com.ites.pos.NetworkController;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 10/24/16.
 */

public class RoomFragment extends Fragment {
    private static final String SNACK_ACTION_COLOR = "#387ef4";
    private int BILL_MAX_HEIGHT = 210;
    private JSONArray tableConfigs;
    private PopupWindow orderInfo;
    private ImageButton closeBtn, closeBtn_selectGuest;
    private TextView tableHeader, guestNoD, roomNoD, timeStampD, usernameD, kotTotalD;
    private Spinner kotNoD;
    private RecyclerView billItems;
    private Button newBtn, editBtn, guestBtn, tableCloseBtn;
    private int tableStatus;
    private LinearLayout comp1, comp2, comp3, kotTotalPanel;
    private ProgressBar loadingKotProgressBar;
    private TabLayout selectGuestTabLayout;
    private ViewPager selectGuestViewPager;
    private String houseAccList, reservationRoomList;
    private SelectGuestFragment inHouse, manager;
    private int restId, userId, userType,mealNo;
    private String userName;
    private String kotNo;

    private String[] tableSignatures = new String[3];

    public RoomFragment() {
    }

    public static RoomFragment newInstance(String tableConfigs, String houseAccList, String reservationRoomList) {
        Bundle args = new Bundle();
        args.putString("tableConfigs", tableConfigs);
        args.putString("houseAccList", houseAccList);
        args.putString("reservationRoomList", reservationRoomList);

        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        SharedPreferences session = getContext().getSharedPreferences("session", 0);
        userId = Integer.parseInt(session.getString("userId", "0"));
        userName = session.getString("username", "user");
        userType = session.getInt("userType", 0);
        mealNo = Integer.parseInt(session.getString("mealPeriodId", "0"));

        if (getResources().getConfiguration().smallestScreenWidthDp < 420) {
            BILL_MAX_HEIGHT = 270;
        }

        Bundle args = getArguments();

        houseAccList = args.get("houseAccList") + "";
        reservationRoomList = args.get("reservationRoomList") + "";

        try {
            tableConfigs = new JSONArray(args.getString("tableConfigs"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_room, container, false);

        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        TableAdapter tableAdapter = new TableAdapter(getContext());

        gridview.setAdapter(tableAdapter);

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                String tableH = null;
                String tableId = null;

                try {
                    JSONObject tmp = tableConfigs.getJSONObject(position);

                    tableSignatures[0] = tmp.getString("room_Id");
                    tableSignatures[1] = tableId = tmp.getString("table_Id");
                    tableSignatures[2] = tableH = tmp.getString("tableName");
                    tableStatus = tmp.getInt("tableStatus");
                    restId = tmp.getInt("restrauntId");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                if (tableStatus > 0) {

                    View orderInfoDisplay = inflater.inflate(R.layout.order_info, parent, false);

                    // refer UI components
                    closeBtn = (ImageButton) orderInfoDisplay.findViewById(R.id.closeBtn);
                    tableHeader = (TextView) orderInfoDisplay.findViewById(R.id.tableHeader);
                    guestNoD = (TextView) orderInfoDisplay.findViewById(R.id.guestVal);
                    roomNoD = (TextView) orderInfoDisplay.findViewById(R.id.roomNoVal);
                    kotNoD = (Spinner) orderInfoDisplay.findViewById(R.id.kotNoVal);
                    timeStampD = (TextView) orderInfoDisplay.findViewById(R.id.timeStampVal);
                    usernameD = (TextView) orderInfoDisplay.findViewById(R.id.userVal);
                    billItems = (RecyclerView) orderInfoDisplay.findViewById(R.id.billItems);
                    kotTotalD = (TextView) orderInfoDisplay.findViewById(R.id.kotTotal);
                    newBtn = (Button) orderInfoDisplay.findViewById(R.id.newBtn);
                    editBtn = (Button) orderInfoDisplay.findViewById(R.id.editBtn);
                    guestBtn = (Button) orderInfoDisplay.findViewById(R.id.guestBtn);
                    tableCloseBtn = (Button) orderInfoDisplay.findViewById(R.id.tableCloseBtn);
                    comp1 = (LinearLayout) orderInfoDisplay.findViewById(R.id.comp1);
                    comp2 = (LinearLayout) orderInfoDisplay.findViewById(R.id.comp2);
                    comp3 = (LinearLayout) orderInfoDisplay.findViewById(R.id.comp3);
                    kotTotalPanel = (LinearLayout) orderInfoDisplay.findViewById(R.id.kotTotalPanel);
                    loadingKotProgressBar = (ProgressBar) orderInfoDisplay.findViewById(R.id.loadingKotProgressBar);

                    RecyclerView.LayoutManager mgr = new LinearLayoutManager(getContext());
                    billItems.setLayoutManager(mgr);

                    // set the table name UI
                    tableHeader.setText(tableH);

                    // feed data from service
                    makeNetworkCall(tableId, tableH, rootView, inflater, position);

                    orderInfo = new PopupWindow(orderInfoDisplay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        orderInfo.setElevation(5.0f);
                    }

                    orderInfo.showAtLocation(rootView, Gravity.CENTER, 0, 0);

                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            orderInfo.dismiss();
                        }
                    });
                } else {
                    // add new kot
                    newKot(rootView, inflater, tableH);
                }

                return false;
            }
        });

        return rootView;
    }

    private void makeNetworkCall(final String tableId, final String tableH, final View rootView, final LayoutInflater inflater, final int position) {
        final NetworkController netCtrl = new NetworkController(getContext());

        netCtrl.getOpenTableDetails(tableId, new OpenTableDetails() {
            @Override
            public void gotOpenTableDetails(String data) {
                // hide progress bar
                loadingKotProgressBar.setVisibility(ProgressBar.INVISIBLE);

                // show relavant UI components
                if (tableStatus > 0) {
                    loadingKotProgressBar.setVisibility(ProgressBar.GONE);
                    comp1.setVisibility(LinearLayout.VISIBLE);
                    comp2.setVisibility(LinearLayout.VISIBLE);
                    comp3.setVisibility(LinearLayout.VISIBLE);
                    billItems.setVisibility(RecyclerView.VISIBLE);
                    kotTotalPanel.setVisibility(LinearLayout.VISIBLE);
                }

                final List<OrderBillItem> billItemsListAll = new ArrayList<>();
                final List<OrderBillItem> billItemsList = new ArrayList<>();
                final List<String> KOTNums = new ArrayList<>();

                try {
                    JSONArray oderBillItems = new JSONArray(data);

                    for (int i = 0; i < oderBillItems.length(); i++) {
                        JSONObject tmp = oderBillItems.getJSONObject(i);
                        tmp.put("tableName", tableH);

                        OrderBillItem billItem = new OrderBillItem(tmp);

                        billItemsListAll.add(billItem);
                    }

                    // split the billItemsList
                    for (int i = 0; i < billItemsListAll.size(); i++) {
                        if (!KOTNums.contains(billItemsListAll.get(i).getKotNo())) {
                            KOTNums.add(billItemsListAll.get(i).getKotNo());
                        }
                    }

                    // set KOT No spinner
                    ArrayAdapter<String> kotAdapter = new ArrayAdapter<>(getContext(), R.layout.kot_spinner_item, KOTNums);
                    kotNoD.setAdapter(kotAdapter);

                    // spinner item selected listener
                    kotNoD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                            billItemsList.clear();
                            kotNo = KOTNums.get(position);

                            for (int i = 0; i < billItemsListAll.size(); i++) {

                                if (billItemsListAll.get(i).getKotNo().equals(kotNo)) {
                                    billItemsList.add(billItemsListAll.get(i));
                                }
                            }

                            guestNoD.setText(billItemsList.get(0).getGuestNo());
                            roomNoD.setText(billItemsList.get(0).getRoomNo());
                            timeStampD.setText(billItemsList.get(0).getSystemDate());
                            usernameD.setText(billItemsList.get(0).getUserName());

                            RecyclerView.Adapter adapter = new BillAdapter(billItemsList);

                            // set bill max height
                            ViewGroup.LayoutParams params = billItems.getLayoutParams();
                            if (billItemsList.size() > 5) {
                                params.height = BILL_MAX_HEIGHT;
                            } else {
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            }

                            // binding data to recycle view
                            billItems.setLayoutParams(params);
                            billItems.setAdapter(adapter);

                            kotTotalD.setText(Float.toString(calculateKotTotal(billItemsList)));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // skip
                        }
                    });

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                // button action listeners
                newBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // dismiss oderinfo popup
                        orderInfo.dismiss();
                        // add new KOT
                        newKot(rootView, inflater, tableH);
                    }
                });

                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // edit KOT
                        if (billItemsList.size() != 0) {
                            editKot(billItemsList);
                        } else {
                            Toast.makeText(getContext(), "There is nothing to edit!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                guestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // generate guest bill
                        if (billItemsList.size() != 0) {
                            netCtrl.printGuestBill(Integer.valueOf(kotNo), userId, restId, userName, new PrintGuestBill() {
                                @Override
                                public void gotPrintGuestBill(String data) {
                                    try {
                                        JSONObject resp = new JSONObject(data);
                                        if (resp.getString("msg").equals("done")) {
                                            Toast.makeText(getContext(), "Guest Bill Generated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Bill didn't Generated, Error occurred in server!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void errorPrintGuestBill(VolleyError error) {
                                    Toast.makeText(getContext(), "Error Occurred! Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "There is nothing to print!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                tableCloseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // close table
                        if (kotNo != null) {
                            NetworkController netCtrl = new NetworkController(getContext());
                            netCtrl.closeGuestBill(kotNo, new CloseGuestBill() {
                                @Override
                                public void gotCloseGuestBill(String data) {
                                    // close kot and refresh table area
                                    orderInfo.dismiss();
                                    Intent recycle = getActivity().getIntent();
                                    getActivity().finish();
                                    startActivity(recycle);
                                }

                                @Override
                                public void errorCloseGuestBill(VolleyError error) {
                                    Toast.makeText(getContext(), "Something has gone wrong, try again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "There is nothing to close!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void errorOpenTableDetails(VolleyError error) {
                Snackbar.make(rootView, "Error Occurred! Check the Network Connectivity.", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // retry making network call
                        makeNetworkCall(tableId, tableH, rootView, inflater, position);
                    }
                }).setActionTextColor(Color.parseColor(SNACK_ACTION_COLOR)).show();
            }
        });
    }

    private void editKot(List<OrderBillItem> billItemsList) {
        final JSONArray arry = new JSONArray();
        for (OrderBillItem item : billItemsList) {
            arry.put(item.toJSONObject());
        }

        final NetworkController netCtrl = new NetworkController(getContext());
        netCtrl.getPosGuestDetails(Integer.parseInt(billItemsList.get(0).getGuestNo()), new PosGuestDetails() {
            @Override
            public void gotPosGuestDetails(String data) {
                try {
                    final JSONObject guestDetails = new JSONObject(data);

                    if (guestDetails.getInt("waiterNo") == userId || userType == 1) {
                        netCtrl.getRestaurantItems(restId, new RestaurantItems() {
                            @Override
                            public void gotRestaurantItems(String data) {
                                JSONObject genericObj = new JSONObject();

                                // set guestType
                                int guestType = GuestFragmentType.WALK_IN;

                                try {
                                    if (((!guestDetails.get("houseNo").equals(null)) ? guestDetails.getInt("houseNo") : 0) != 0) {
                                        guestType = GuestFragmentType.MANAGER_LIST;
                                    } else if (((!guestDetails.get("walkingGuest").equals(null)) ? guestDetails.getInt("walkingGuest") : 0) != 1) {
                                        guestType = GuestFragmentType.ROOM_LIST;
                                    }

                                    genericObj.put("currentUserId", userType);
                                    genericObj.put("guestType", guestType);
                                    genericObj.put("restaurantId", restId);
                                    genericObj.put("waiterId", userId);
                                    genericObj.put("waiterName", userName);
                                    //genericObj.put("mealId", mealNo);
                                    genericObj.put("mealId", (!guestDetails.get("mealNo").equals(null)) ? guestDetails.getInt("mealNo") :0);
                                    genericObj.put("roomId", (!guestDetails.get("roomNo").equals(null) && !guestDetails.getString("roomNo").equals("")) ? guestDetails.getString("roomNo") : "0");
                                    genericObj.put("tableId", (!guestDetails.get("tableNo").equals(null)) ? guestDetails.getInt("tableNo") : 0);
                                    genericObj.put("tableName", tableSignatures[2]);
                                    genericObj.put("guestFname", (!guestDetails.get("firstName").equals(null)) ? guestDetails.getString("firstName") : "");
                                    genericObj.put("guestLname", (!guestDetails.get("lastName").equals(null)) ? guestDetails.getString("lastName") : "");
                                    genericObj.put("adultsCount", (!guestDetails.get("adult").equals(null)) ? guestDetails.getInt("adult") : 0);
                                    genericObj.put("kidsCount", (!guestDetails.get("children").equals(null)) ? guestDetails.getInt("children") : 0);
                                    genericObj.put("confNo", (!guestDetails.get("confNo").equals(null)) ? guestDetails.getInt("confNo") : 0);
                                    genericObj.put("reservRoomNo", "0");
                                    genericObj.put("foodPackage", (!guestDetails.get("package1").equals(null)) ? guestDetails.getString("package1") : 0);
                                    genericObj.put("hAccountId", (!guestDetails.get("houseNo").equals(null)) ? guestDetails.getInt("houseNo") : 0);
                                    genericObj.put("hAccountName", "");

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                                // re-direct to item punching activity
                                Intent nxt = new Intent(getContext(), ItemPunching.class);
                                nxt.putExtra("restaurantItems", data);
                                nxt.putExtra("genericObj", genericObj.toString());
                                nxt.putExtra("billItemsList", arry + "");
                                startActivity(nxt);

                                // dismiss oderinfo popup
                                orderInfo.dismiss();

                                getActivity().finish();
                            }

                            @Override
                            public void errorRestaurantItems(VolleyError error) {

                            }
                        });
                    } else {
                        // notify user that u cannot edit this KOT
                        Toast.makeText(getContext(), "You don't have permission to edit this KOT!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorPosGuestDetails(VolleyError error) {

            }
        });
    }

    private void newKot(View rootView, LayoutInflater inflater, String tableH) {
        // disable tabs
        final LinearLayout tabPart = (LinearLayout) ((TabLayout) rootView.getRootView().findViewById(R.id.tabs)).getChildAt(0);
        for (int i = 0; i < tabPart.getChildCount(); i++) {
            tabPart.getChildAt(i).setEnabled(false);
        }

        // inflate overlay view
        final View selectGuestDisplay = inflater.inflate(R.layout.select_guest, (ViewGroup) rootView, false);

        ((ViewGroup) rootView).addView(selectGuestDisplay);

        selectGuestDisplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        });

        TextView tableName = (TextView) selectGuestDisplay.findViewById(R.id.tableHeader_selectGuest);
        closeBtn_selectGuest = (ImageButton) selectGuestDisplay.findViewById(R.id.closeBtn_selectGuest);
        selectGuestTabLayout = (TabLayout) selectGuestDisplay.findViewById(R.id.selectGuestTabs);
        selectGuestViewPager = (ViewPager) selectGuestDisplay.findViewById(R.id.selectGuestViewpager);

        tableName.setText(tableH);

        // bind tablayout and viewpager
        setupViewPager(selectGuestViewPager);
        selectGuestTabLayout.setupWithViewPager(selectGuestViewPager);

        closeBtn_selectGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) selectGuestDisplay.getParent()).removeView(selectGuestDisplay);

                // enable tabs
                for (int i = 0; i < tabPart.getChildCount(); i++) {
                    tabPart.getChildAt(i).setEnabled(true);
                }
            }
        });
    }

    // calculate total kot cost
    private float calculateKotTotal(List<OrderBillItem> list) {
        float total = 0;

        for (int i = 0; i < list.size(); i++) {
            total += Float.valueOf(list.get(i).getTaxPrice());
        }

        return (float) (Math.round(total * 100) / 100.00);
    }

    // setup select guest tablayout viewpager
    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter vpa = new ViewPagerAdapter(getFragmentManager());

        SelectGuestFragment walkIn = new SelectGuestFragment();
        walkIn.setFragmentType(GuestFragmentType.WALK_IN, tableSignatures);
        vpa.addFragment(walkIn, "Walk-In"); // Tab title hard coded

        inHouse = new SelectGuestFragment();
        inHouse.setFragmentType(GuestFragmentType.ROOM_LIST, tableSignatures);
        Bundle argsInHouse = new Bundle();
        argsInHouse.putString("roomList", reservationRoomList);
        inHouse.setArguments(argsInHouse);
        vpa.addFragment(inHouse, "In House"); // Tab title hard coded

        manager = new SelectGuestFragment();
        manager.setFragmentType(GuestFragmentType.MANAGER_LIST, tableSignatures);
        Bundle argsManager = new Bundle();
        argsManager.putString("managerList", houseAccList);
        manager.setArguments(argsManager);
        vpa.addFragment(manager, "Manager"); // Tab title hard coded

        vpa.notifyDataSetChanged();
        viewPager.setAdapter(vpa);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
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
    }

    private class TableAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private TextView tabName;
        private ImageView bookedLabel;

        TableAdapter(Context ctx) {
            mInflator = LayoutInflater.from(ctx);
        }

        public int getCount() {
            return tableConfigs.length();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            String tableName = null;
            int tableStatus = 0;
            try {
                JSONObject tmp = tableConfigs.getJSONObject(position);
                tableName = tmp.getString("tableName");
                tableStatus = tmp.getInt("tableStatus");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            convertView = mInflator.inflate(R.layout.table_item, parent, false);

            tabName = (TextView) convertView.findViewById(R.id.tableNameEdit);
            bookedLabel = (ImageView) convertView.findViewById(R.id.booked);

            if (tableStatus == 1) {
                bookedLabel.setVisibility(ImageView.VISIBLE);
            }
            tabName.setText(tableName);

            return convertView;
        }
    }
}