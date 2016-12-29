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
    private int mTaxClass;
    private int dtaxClass;
    private Double dUnitPrice;
    private Double mUnitPrice;
    private Double mTaxPrice;
    private Double dTaxPrice;
    private int currencyId;

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
            this.mTaxClass = jObj.getInt("mTaxClass");
            this.dtaxClass = jObj.getInt("dtaxClass");
            this.dUnitPrice = jObj.getDouble("dUnitPrice");
            this.mUnitPrice = jObj.getDouble("mUnitPrice");
            this.mTaxPrice = jObj.getDouble("mTaxPrice");
            this.dTaxPrice = jObj.getDouble("dTaxPrice");
            this.currencyId = jObj.getInt("currencyId");
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

    public int getmTaxClass() {
        return mTaxClass;
    }

    public void setmTaxClass(int mTaxClass) {
        this.mTaxClass = mTaxClass;
    }

    public int getDtaxClass() {
        return dtaxClass;
    }

    public void setDtaxClass(int dtaxClass) {
        this.dtaxClass = dtaxClass;
    }

    public Double getdUnitPrice() {
        return dUnitPrice;
    }

    public void setdUnitPrice(Double dUnitPrice) {
        this.dUnitPrice = dUnitPrice;
    }

    public Double getmUnitPrice() {
        return mUnitPrice;
    }

    public void setmUnitPrice(Double mUnitPrice) {
        this.mUnitPrice = mUnitPrice;
    }

    public Double getmTaxPrice() {
        return mTaxPrice;
    }

    public void setmTaxPrice(Double mTaxPrice) {
        this.mTaxPrice = mTaxPrice;
    }

    public Double getdTaxPrice() {
        return dTaxPrice;
    }

    public void setdTaxPrice(Double dTaxPrice) {
        this.dTaxPrice = dTaxPrice;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
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
            obj.put("mTaxClass", mTaxClass);
            obj.put("dtaxClass", dtaxClass);
            obj.put("dUnitPrice", dUnitPrice);
            obj.put("mUnitPrice", mUnitPrice);
            obj.put("mTaxPrice", mTaxPrice);
            obj.put("dTaxPrice", dTaxPrice);
            obj.put("currencyId", currencyId);
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
                ", mTaxClass=" + mTaxClass +
                ", dtaxClass=" + dtaxClass +
                ", dUnitPrice=" + dUnitPrice +
                ", mUnitPrice=" + mUnitPrice +
                ", mTaxPrice=" + mTaxPrice +
                ", dTaxPrice=" + dTaxPrice +
                ", currencyId=" + currencyId +
                '}';
    }
}