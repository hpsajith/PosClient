package com.ites.pos.Activities.rooms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ites.pos.Activities.NewBill;
import com.ites.pos.BillAdapter;
import com.ites.pos.Controllers.NetworkController;
import com.ites.pos.Controllers.ResponseCallBack;
import com.ites.pos.Models.OrderBillItem;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 10/24/16.
 */

public class Room5 extends Fragment {
    private JSONArray tableConfigs;
    private PopupWindow orderInfo;
    private ImageButton closeBtn;
    private TextView tableHeader, guestNoD, roomNoD, timeStampD, usernameD, kotTotalD;
    private Spinner kotNoD;
    private RecyclerView billItems;
    private Button newBtn, editBtn, guestBtn, tableCloseBtn;

    public Room5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        try {
            tableConfigs = new JSONArray(args.getString("tableConfigs"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_room5, container, false);

        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        TableAdapter tableAdapter = new TableAdapter(getContext());

        gridview.setAdapter(tableAdapter);

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String tableH = null, tableId = null;
                NetworkController netCtrl;

                netCtrl = new NetworkController(getContext());
                View orderInfoDisplay = inflater.inflate(R.layout.order_info, parent, false);

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

                RecyclerView.LayoutManager mgr = new LinearLayoutManager(getContext());
                billItems.setLayoutManager(mgr);

                try {
                    JSONObject tmp = tableConfigs.getJSONObject(position);
                    tableH = tmp.getString("tableName");
                    tableId = tmp.getString("table_Id");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                // set the table name UI
                tableHeader.setText(tableH);

                final String finalTableH = tableH;
                final String finalTableId = tableId;
                netCtrl.getOpenTableDetails(tableId, new ResponseCallBack() {
                    @Override
                    public void gotTableConfigs(String data) {
                    }

                    @Override
                    public void gotOpenTableDetails(String data) {
                        final List<OrderBillItem> billItemsListAll = new ArrayList<>();
                        final List<OrderBillItem> billItemsList = new ArrayList<>();
                        final List<String> KOTNums = new ArrayList<>();

                        try {
                            JSONArray oderBillItems = new JSONArray(data);

                            for (int i = 0; i < oderBillItems.length(); i++) {
                                JSONObject tmp = oderBillItems.getJSONObject(i);
                                tmp.put("tableName", finalTableH);

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
                            kotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            kotNoD.setAdapter(kotAdapter);

                            kotNoD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    billItemsList.clear();
                                    String KotNo = KOTNums.get(position);

                                    for (int i = 0; i < billItemsListAll.size(); i++) {

                                        if (billItemsListAll.get(i).getKotNo().equals(KotNo)) {
                                            billItemsList.add(billItemsListAll.get(i));
                                        }
                                    }

                                    // date format convert
                                    Timestamp stamp = new Timestamp(Long.valueOf(billItemsList.get(0).getSystemDate()));

                                    // set the common values in the UI
                                    guestNoD.setText(billItemsList.get(0).getGuestNo());
                                    roomNoD.setText(billItemsList.get(0).getRoomNo());
                                    timeStampD.setText(stamp.toString());
                                    usernameD.setText(billItemsList.get(0).getUserName());

                                    RecyclerView.Adapter adapter = new BillAdapter(billItemsList);

                                    // set parameters
                                    ViewGroup.LayoutParams params = billItems.getLayoutParams();

                                    if (billItemsList.size() > 7) {
                                        params.height = 310;
                                    } else {
                                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    }

                                    billItems.setLayoutParams(params);
                                    billItems.setAdapter(adapter);

                                    kotTotalD.setText(Float.toString(calculateKotTotal(billItemsList)));

                                    // button action listeners
                                    newBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // go to new bill activity
                                            Intent nxt = new Intent(getActivity(), NewBill.class);
                                            nxt.putExtra("tableId", finalTableId);
                                            startActivity(nxt);
                                        }
                                    });

                                    editBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // go to edit bill activity
                                            Toast.makeText(getContext(), "Edit Order", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    guestBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // generate guest bill
                                            Toast.makeText(getContext(), "Guest Bill", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    tableCloseBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // close table
                                            Toast.makeText(getContext(), "Close Table", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // skip
                                }
                            });

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                orderInfo = new PopupWindow(orderInfoDisplay, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                if (Build.VERSION.SDK_INT >= 21) {
                    orderInfo.setElevation(5.0f);
                }

                orderInfo.showAtLocation(gridview, Gravity.CENTER, 0, 0);

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderInfo.dismiss();
                    }
                });

                return false;
            }
        });

        return rootView;
    }

    public float calculateKotTotal(List<OrderBillItem> list) {
        float total = 0;

        for (int i = 0; i < list.size(); i++) {
            total = total + Float.valueOf(list.get(i).getQty()) * Float.valueOf(list.get(i).getUnitPrice());
        }

        return total;
    }

    public class TableAdapter extends BaseAdapter {
        private Context ctx;
        private LayoutInflater mInflator;
        private TextView tabName;

        public TableAdapter(Context ctx) {
            this.ctx = ctx;
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
            try {
                JSONObject tmp = tableConfigs.getJSONObject(position);
                tableName = tmp.getString("tableName");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            convertView = mInflator.inflate(R.layout.table_item, parent, false);

            tabName = (TextView) convertView.findViewById(R.id.tableName);
            tabName.setText(tableName);

            return convertView;
        }
    }
}