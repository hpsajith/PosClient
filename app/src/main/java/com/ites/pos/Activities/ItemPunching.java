package com.ites.pos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.ites.pos.Models.RestaurantItem;
import com.ites.pos.main_activity.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemPunching extends AppCompatActivity {
    private Map<String, Set<String>> familyMasterMap = new HashMap<>();
    private Map<String, List<RestaurantItem>> masterItemMap = new HashMap<>();
    private List<String> currentFamilyList = new ArrayList<>();
    private List<String> currentMasterList = new ArrayList<>();
    private List<RestaurantItem> currentItemList = new ArrayList<>();
    private View previousTouchedViewFamily, previousTouchedViewMaster, previousTouchedViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_punching);

        Intent i = getIntent();
        String restItemsStr = i.getStringExtra("restaurantItems");

        try {
            JSONArray restItemsSet = new JSONArray(restItemsStr);
            for (int j = 0; j < restItemsSet.length(); j++) {

                RestaurantItem item = new RestaurantItem(restItemsSet.getJSONObject(j));

                if (!familyMasterMap.containsKey(item.getFamilyName())) {
                    Set<String> set = new HashSet<>();
                    set.add(item.getMasterName());

                    familyMasterMap.put(item.getFamilyName(), set);
                } else {
                    familyMasterMap.get(item.getFamilyName()).add(item.getMasterName());
                }

                if (!masterItemMap.containsKey(item.getMasterName())) {
                    List<RestaurantItem> list = new ArrayList<>();
                    list.add(item);

                    masterItemMap.put(item.getMasterName(), list);
                } else {
                    masterItemMap.get(item.getMasterName()).add(item);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        ListView family = (ListView) findViewById(R.id.family);
        final ListView master = (ListView) findViewById(R.id.master);
        final ListView items = (ListView) findViewById(R.id.item);

        currentFamilyList.addAll(familyMasterMap.keySet());
        Collections.sort(currentFamilyList);
        family.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, currentFamilyList));

        family.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // remove items view
                items.setVisibility(View.INVISIBLE);

                currentMasterList.clear();
                currentMasterList.addAll(familyMasterMap.get(currentFamilyList.get(i)));
                Collections.sort(currentMasterList);
                master.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, currentMasterList));

                view.setBackgroundColor(Color.GRAY);                
                master.setVisibility(View.VISIBLE);

                if (previousTouchedViewFamily != null) {
                    previousTouchedViewFamily.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
                previousTouchedViewFamily = view;
            }
        });

        master.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentItemList.clear();
                currentItemList.addAll(masterItemMap.get(currentMasterList.get(i)));

                Iterator ptr = currentItemList.iterator();
                List<String> currentItemListStr = new ArrayList<>();
                
                while(ptr.hasNext()){
                    currentItemListStr.add(((RestaurantItem)ptr.next()).getItemName());
                }

                Collections.sort(currentItemListStr);
                items.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, currentItemListStr));

                view.setBackgroundColor(Color.GRAY);
                items.setVisibility(View.VISIBLE);

                if (previousTouchedViewMaster != null) {
                    previousTouchedViewMaster.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
                previousTouchedViewMaster = view;
            }
        });

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.GRAY);

                if (previousTouchedViewItem != null) {
                    previousTouchedViewItem.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
                previousTouchedViewItem = view;

                final AlertDialog.Builder builder = new AlertDialog.Builder(ItemPunching.this);

                builder.setView(R.layout.amount_input);

                builder.setTitle(currentItemList.get(i).getItemName());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog alert = builder.create();
                alert.show();
                alert.getWindow().setLayout(300, 190);
            }
        });
    }

    // disable back button
    @Override
    public void onBackPressed() {
    }
}
