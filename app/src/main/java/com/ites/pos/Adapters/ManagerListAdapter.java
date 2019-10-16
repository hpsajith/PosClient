package com.ites.pos.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ites.pos.Models.HouseAccount;
import com.ites.pos.main_activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 12/1/16.
 */

public class ManagerListAdapter extends RecyclerView.Adapter<ManagerListAdapter.ManagerViewHolder> {
    private List<HouseAccount> managerList = new ArrayList<>();
    protected static View previousTouchedView;
    protected static HouseAccount selectedHouseAccount;
    private static ManagerListAdapter mInstance;

    public ManagerListAdapter(List<HouseAccount> managerList) {
        this.managerList = managerList;
    }

    public static synchronized ManagerListAdapter getInstance(List<HouseAccount> managerList){
        if(mInstance == null){
            mInstance = new ManagerListAdapter(managerList);
        }
        return mInstance;
    }

    @Override
    public void onViewAttachedToWindow(ManagerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // initialize and reset selected val
        this.selectedHouseAccount = null;
    }

    @Override
    public ManagerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_item, parent, false);

        return new ManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManagerViewHolder holder, int position) {
        holder.accNo.setText(managerList.get(position).getAccountNo().trim()+"");
        holder.mName.setText(managerList.get(position).getEmployeeName().trim()+"");
        holder.bind(managerList.get(position));
    }

    @Override
    public int getItemCount() {
        return managerList.size();
    }

    public HouseAccount getSelectedHouseAccount(){
        return selectedHouseAccount;
    }

    static class ManagerViewHolder extends RecyclerView.ViewHolder {
        private TextView accNo, mName;

        ManagerViewHolder(View itemView) {
            super(itemView);
            accNo = (TextView) itemView.findViewById(R.id.accNo);
            mName = (TextView) itemView.findViewById(R.id.mName);
        }

        public void bind(final HouseAccount item) {
            itemView.setBackgroundColor(Color.parseColor("#00ffffff")); // avoid multiple selections in scroll
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    v.setBackgroundColor(Color.parseColor("#55387ef4"));
                    if ((previousTouchedView != null) && (previousTouchedView != v)) {
                        previousTouchedView.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                    previousTouchedView = v;
                    selectedHouseAccount = item;
                }
            });
        }
    }
}