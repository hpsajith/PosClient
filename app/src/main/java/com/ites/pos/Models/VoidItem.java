package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 2/15/17.
 */

public class VoidItem {
    private int itemCode = 0;
    private int voidReasonCode = 0;
    private int qty = 0;

    public VoidItem(int itemCode, int voidReasonCode, int qty) {
        this.itemCode = itemCode;
        this.voidReasonCode = voidReasonCode;
        this.qty = qty;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public int getVoidReasonCode() {
        return voidReasonCode;
    }

    public void setVoidReasonCode(int voidReasonCode) {
        this.voidReasonCode = voidReasonCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("itemCode", itemCode);
            obj.put("voidNo", voidReasonCode);
            obj.put("qty", qty);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "VoidItem{" +
                "itemCode=" + itemCode +
                ", voidReasonCode=" + voidReasonCode +
                ", qty=" + qty +
                '}';
    }
}
