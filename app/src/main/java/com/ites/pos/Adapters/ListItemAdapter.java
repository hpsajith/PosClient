package com.ites.pos.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ites.pos.Interfaces.ListItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 12/21/16.
 */

public class ListItemAdapter extends ArrayAdapter<String> {
    private static final String[] COLORS = new String[]{"#1099F5", "#BA5CD1", "#FD4B42", "#FFA204", "#FED204", "#40BE1F"};
    private int adapterType;
    private List<String> items = new ArrayList<>();

    public ListItemAdapter(Context context, int resource, int type, List<String> items) {
        super(context, resource, items);
        this.adapterType = type;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (adapterType == ListItemType.FAMILY) {
            v.setBackgroundColor(Color.parseColor(COLORS[position]));
        }
        return v;
    }
}