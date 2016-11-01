package com.ites.pos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ites.pos.Models.OrderBillItem;
import com.ites.pos.main_activity.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 11/1/16.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private List<OrderBillItem> list = new ArrayList<>();

    public BillAdapter(List<OrderBillItem> list) {
        this.list = list;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);

        BillViewHolder vh = new BillViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.itemQtyD.setText(list.get(position).getQty().trim());
        holder.itemNameD.setText(list.get(position).getItemName().trim());
        holder.itemCost.setText(list.get(position).getUnitPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder{
        private TextView itemQtyD,itemNameD,itemCost;

        public BillViewHolder(View itemView) {
            super(itemView);

            itemQtyD = (TextView) itemView.findViewById(R.id.itemQty);
            itemNameD = (TextView) itemView.findViewById(R.id.itemName);
            itemCost = (TextView) itemView.findViewById(R.id.itemCost);
        }
    }
}
