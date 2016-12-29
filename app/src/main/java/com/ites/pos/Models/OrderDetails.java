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
                ", tableName='" + tableName + '\'' +
                ", waiterName='" + waiterName + '\'' +
                ", guestFName='" + guestFName + '\'' +
                ", guestLName='" + guestLName + '\'' +
                ", remark='" + remark + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
