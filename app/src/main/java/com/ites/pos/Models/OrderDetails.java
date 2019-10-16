package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 12/28/16.
 */

public class OrderDetails {
    private int kotNo;
    private int adultCount;
    private int kidsCount;
    private int roomNo;
    private int restId;
    private int waiterId;
    private int guestNo;
    private int guestType;
    private String tableName;
    private String waiterName;
    private String guestFName;
    private String guestLName;
    private String remark;
    private String date;

    public int getKotNo() {
        return kotNo;
    }

    public void setKotNo(int kotNo) {
        this.kotNo = kotNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getKidsCount() {
        return kidsCount;
    }

    public void setKidsCount(int kidsCount) {
        this.kidsCount = kidsCount;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getGuestFName() {
        return guestFName;
    }

    public void setGuestFName(String guestFName) {
        this.guestFName = guestFName;
    }

    public String getGuestLName() {
        return guestLName;
    }

    public void setGuestLName(String guestLName) {
        this.guestLName = guestLName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getGuestType() {
        return guestType;
    }

    public void setGuestType(int guestType) {
        this.guestType = guestType;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public int getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(int guestNo) {
        this.guestNo = guestNo;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("kotNo", kotNo);
            obj.put("adultCount", adultCount);
            obj.put("kidsCount", kidsCount);
            obj.put("tableName", tableName);
            obj.put("waiterName", waiterName);
            obj.put("guestFName", guestFName);
            obj.put("guestLName", guestLName);
            obj.put("remark", remark);
            obj.put("date", date);
            obj.put("roomNo", roomNo);
            obj.put("guestType", guestType);
            obj.put("restId", restId);
            obj.put("waiterId", waiterId);
            obj.put("guestNo", guestNo);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "kotNo=" + kotNo +
                ", adultCount=" + adultCount +
                ", kidsCount=" + kidsCount +
                ", roomNo=" + roomNo +
                ", restId=" + restId +
                ", waiterId=" + waiterId +
                ", guestNo=" + guestNo +
                ", guestType=" + guestType +
                ", tableName='" + tableName + '\'' +
                ", waiterName='" + waiterName + '\'' +
                ", guestFName='" + guestFName + '\'' +
                ", guestLName='" + guestLName + '\'' +
                ", remark='" + remark + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}