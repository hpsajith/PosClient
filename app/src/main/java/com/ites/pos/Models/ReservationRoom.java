package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 12/1/16.
 */

public class ReservationRoom {
    private String fName;
    private String lName;
    private int guestNo;
    private int confNo;
    private int guestStatus;
    private String roomNo;
    private int noOfAdults;
    private int noChild;
    private String pakage;
    private int reservationNo;

    public ReservationRoom() {
    }

    public ReservationRoom(JSONObject jObj) {
        try {
            this.fName = jObj.get("fName").toString().trim();
            this.lName = jObj.get("lName").toString().trim();
            this.guestNo = jObj.getInt("guestNo");
            this.confNo = jObj.getInt("confNo");
            this.guestStatus = jObj.getInt("guestStatus");
            this.roomNo = jObj.get("roomNo").toString().trim();
            this.noOfAdults = jObj.getInt("noOfAdults");
            this.noChild = jObj.getInt("noChild");
            this.pakage = jObj.get("pakage").toString().trim();
            this.reservationNo = jObj.getInt("reservationNo");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(int guestNo) {
        this.guestNo = guestNo;
    }

    public int getConfNo() {
        return confNo;
    }

    public void setConfNo(int confNo) {
        this.confNo = confNo;
    }

    public int getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(int guestStatus) {
        this.guestStatus = guestStatus;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getNoOfAdults() {
        return noOfAdults;
    }

    public void setNoOfAdults(int noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public int getNoChild() {
        return noChild;
    }

    public void setNoChild(int noChild) {
        this.noChild = noChild;
    }

    public String getPakage() {
        return pakage;
    }

    public void setPakage(String pakage) {
        this.pakage = pakage;
    }

    public int getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(int reservationNo) {
        this.reservationNo = reservationNo;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("fName", fName);
            obj.put("lName", lName);
            obj.put("guestNo", guestNo);
            obj.put("confNo", confNo);
            obj.put("guestStatus", guestStatus);
            obj.put("roomNo", roomNo);
            obj.put("noOfAdults", noOfAdults);
            obj.put("noChild", noChild);
            obj.put("pakage", pakage);
            obj.put("reservationNo", reservationNo);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // convert JSON object to java bean
    public ReservationRoom jsonToJavaBean(JSONObject obj) {
        return new ReservationRoom(obj);
    }

    @Override
    public String toString() {
        return "ReservationRoom{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", guestNo=" + guestNo +
                ", confNo=" + confNo +
                ", guestStatus=" + guestStatus +
                ", roomNo='" + roomNo + '\'' +
                ", noOfAdults=" + noOfAdults +
                ", noChild=" + noChild +
                ", pakage='" + pakage + '\'' +
                ", reservationNo=" + reservationNo +
                '}';
    }
}
