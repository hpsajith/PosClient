package com.ites.pos.Activities.rooms;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ites.pos.BillAdapter;
import com.ites.pos.Controllers.NetworkController;
import com.ites.pos.Controllers.ResponseCallBack;
import com.ites.pos.Models.OrderBillItem;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 10/24/16.
 */

public class Room4 extends Fragment {
    private JSONArray tableConfigs;
    private PopupWindow orderInfo;
    private ImageButton closeBtn;
    private TextView tableHeader,guestNoD,roomNoD,kotNoD,timeStampD,usernameD,kotTotalD;
    private RecyclerView billItems;

    public Room4() {
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
        View rootView = inflater.inflate(R.layout.room4, container, false);

        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);

        TableAdapter tableAdapter = new TableAdapter(getContext());

        gridview.setAdapter(tableAdapter);

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String tableH=null,tableId=null;
                NetworkController netCtrl;

                netCtrl = new NetworkController(getContext());
                View orderInfoDisplay = inflater.inflate(R.layout.order_info, parent, false);

                closeBtn = (ImageButton) orderInfoDisplay.findViewById(R.id.closeBtn);
                tableHeader = (TextView) orderInfoDisplay.findViewById(R.id.tableHeader);
                guestNoD = (TextView) orderInfoDisplay.findViewById(R.id.guestVal);
                roomNoD = (TextView) orderInfoDisplay.findViewById(R.id.roomNoVal);
                kotNoD = (TextView) orderInfoDisplay.findViewById(R.id.kotNoVal);
                timeStampD = (TextView) orderInfoDisplay.findViewById(R.id.timeStampVal);
                usernameD = (TextView) orderInfoDisplay.findViewById(R.id.userVal);
                billItems = (RecyclerView) orderInfoDisplay.findViewById(R.id.billItems);
                kotTotalD = (TextView) orderInfoDisplay.findViewById(R.id.kotTotal);

                RecyclerView.LayoutManager mgr = new LinearLayoutManager(getContext());
                billItems.setLayoutManager(mgr);

                try {
                    JSONObject tmp = tableConfigs.getJSONObject(position);
                    tableH = tmp.getString("tableName");
                    tableId = tmp.getString("tableId");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                // set the table name UI
                tableHeader.setText(tableH);

                final String finalTableH = tableH;
                netCtrl.getOpenTableDetails(tableId, new ResponseCallBack() {
                    @Override
                    public void gotTableConfigs(String data) {
                    }

                    @Override
                    public void gotOpenTableDetails(String data) {
                        List<OrderBillItem> billItemsList = new ArrayList<OrderBillItem>();

                        try{
                            JSONArray oderBillItems = new JSONArray(data);

                            for(int i = 0; i<oderBillItems.length(); i++){
                                JSONObject tmp = oderBillItems.getJSONObject(i);
                                tmp.put("tableName", finalTableH);

                                OrderBillItem billItem = new OrderBillItem(tmp);

                                billItemsList.add(billItem);
                            }

                            // set the common values in the UI
                            guestNoD.setText(billItemsList.get(0).getGuestNo());
                            roomNoD.setText(billItemsList.get(0).getRoomNo());
                            kotNoD.setText(billItemsList.get(0).getKotNo());
                            timeStampD.setText(billItemsList.get(0).getSystemDate());
                            usernameD.setText(billItemsList.get(0).getUserName());
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }

                        RecyclerView.Adapter adapter = new BillAdapter(billItemsList);

                        billItems.setAdapter(adapter);

                        kotTotalD.setText(Float.toString(calculateKotTotal(billItemsList)));
                    }
                });

                orderInfo = new PopupWindow(orderInfoDisplay, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

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

    public float calculateKotTotal(List<OrderBillItem> list){
        float total=0;

        Log.d("Lsit >>",list.toString());

        for(int i = 0; i<list.size(); i++){
            total = total + Float.valueOf(list.get(i).getQty())*Float.valueOf(list.get(i).getUnitPrice());

            Log.d("Current Total", Float.valueOf(total)+"");
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