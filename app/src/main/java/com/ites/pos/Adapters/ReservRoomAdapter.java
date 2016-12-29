package com.ites.pos.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ites.pos.Models.ReservationRoom;
import com.ites.pos.main_activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wannix on 12/1/16.
 */

public class ReservRoomAdapter extends RecyclerView.Adapter<ReservRoomAdapter.ReservRoomViewHolder> {
    private List<ReservationRoom> reservationRoomList = new ArrayList<>();
    protected static View previousTouchedView;
    protected static ReservationRoom selectedRoom;
    private static ReservRoomAdapter mInstance;

    public ReservRoomAdapter(List<ReservationRoom> reservationRoomList) {
        this.reservationRoomList = reservationRoomList;
    }

    public static synchronized ReservRoomAdapter getInstance(List<ReservationRoom> reservationRoomList){
        if (mInstance == null){
            mInstance = new ReservRoomAdapter(reservationRoomList);
        }
        return mInstance;
    }

    @Override
    public void onViewAttachedToWindow(ReservRoomViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // initialize and reset selected val
        this.selectedRoom = null;
    }

    @Override
    public ReservRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserv_room_list_item, parent, false);

        return new ReservRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservRoomViewHolder holder, int position) {
        holder.fName.setText(reservationRoomList.get(position).getfName() + "");
        holder.comments.setText("N/A");
        holder.roomNo.setText(reservationRoomList.get(position).getRoomNo() + "");
        holder.pakage.setText(reservationRoomList.get(position).getPakage() + "");
        holder.bind(reservationRoomList.get(position));
    }

    @Override
    public int getItemCount() {
        return reservationRoomList.size();
    }

    public ReservationRoom getSelectedRoom(){
        return selectedRoom;
    }

    static class ReservRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView fName, roomNo, pakage, comments;

        public ReservRoomViewHolder(View itemView) {
            super(itemView);

            fName = (TextView) itemView.findViewById(R.id.fName);
            roomNo = (TextView) itemView.findViewById(R.id.roomNo);
            pakage = (TextView) itemView.findViewById(R.id.pakage);
            comments = (TextView) itemView.findViewById(R.id.comments);
        }

        public void bind(final ReservationRoom item) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(Color.parseColor("#55387ef4"));
                    if ((previousTouchedView != null) && (previousTouchedView != v)) {
                        previousTouchedView.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                    previousTouchedView = v;
                    selectedRoom = item;
                }
            });
        }
    }
}