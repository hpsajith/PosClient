package com.ites.pos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ites.pos.Models.Item;
import com.ites.pos.main_activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 12/19/16.
 */

public class PunchedItemAdapter extends ArrayAdapter<Item> {
    private List<Item> items = new ArrayList<>();

    public PunchedItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.punched_item, null);
        }

        Item item = items.get(position);


        if (item != null) {

            TextView itemName = (TextView) v.findViewById(R.id.itemName);
            TextView itemQty = (TextView) v.findViewById(R.id.itemQty);
            TextView itemPrice = (TextView) v.findViewById(R.id.itemPrice);
            TextView itemTaxPrice = (TextView) v.findViewById(R.id.itemTaxPrice);

            if (itemName != null) {
                itemName.setText(item.getItemName());
            }
            if (itemQty != null) {
                itemQty.setText(item.getQty() + "");
            }
            if (itemPrice != null) {
                double unitPrice = Math.round(item.getTaxPrice()*100)/100.00;
                itemPrice.setText(unitPrice + "");
            }
            if (itemName != null) {
                double netVal = Math.round(item.getTaxPrice()*item.getQty()*100)/100.00;
                itemTaxPrice.setText(netVal + "");
            }
        }

        return v;
    }
}
