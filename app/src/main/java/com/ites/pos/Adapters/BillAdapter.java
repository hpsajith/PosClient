package com.ites.pos.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ites.pos.Models.OrderBillItem;
import com.ites.pos.main_activity.R;

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

        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.itemQtyD.setText(list.get(position).getQty().trim());
        holder.itemNameD.setText(list.get(position).getItemName().trim());

        double unitPrice = Math.round(Double.valueOf(list.get(position).getUnitPrice())*100)/100.00;
        holder.itemCost.setText(unitPrice+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        private TextView itemQtyD, itemNameD, itemCost;

        BillViewHolder(View itemView) {
            super(itemView);

            itemQtyD = (TextView) itemView.findViewById(R.id.itemQty);
            itemNameD = (TextView) itemView.findViewById(R.id.itemName);
            itemCost = (TextView) itemView.findViewById(R.id.itemCost);
        }
    }
}