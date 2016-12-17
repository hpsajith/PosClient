package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 12/15/16.
 */

public class RestaurantItem {
    private int familyNno;
    private String familyName;
    private int masterNo;
    private String masterName;
    private int itemCode;
    private String itemName;

    public RestaurantItem() {
    }

    public RestaurantItem(JSONObject jObj){
        try {
            this.familyNno = jObj.getInt("familyNno");
            this.familyName = ((String) jObj.get("familyName")).trim();
            this.masterNo = jObj.getInt("masterNo");
            this.masterName = ((String)jObj.get("masterName")).trim();
            this.itemCode = jObj.getInt("itemCode");
            this.itemName = ((String) jObj.get("itemName")).trim();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public int getFamilyNno() {
        return familyNno;
    }

    public void setFamilyNno(int familyNno) {
        this.familyNno = familyNno;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getMasterNo() {
        return masterNo;
    }

    public void setMasterNo(int masterNo) {
        this.masterNo = masterNo;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("familyNno", familyNno);
            obj.put("familyName", familyName);
            obj.put("masterNo", masterNo);
            obj.put("masterName", masterName);
            obj.put("itemCode", itemCode);
            obj.put("itemName", itemName);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // convert JSON object to java bean
    public RestaurantItem jsonToJavaBean(JSONObject obj) {
        return new RestaurantItem(obj);
    }

    @Override
    public String toString() {
        return "RestaurantItem{" +
                "familyNno=" + familyNno +
                ", familyName='" + familyName + '\'' +
                ", masterNo=" + masterNo +
                ", masterName='" + masterName + '\'' +
                ", itemCode=" + itemCode +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}