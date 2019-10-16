package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by wannix on 12/19/16.
 */

public class Item implements Serializable {
    private String itemName;
    private int qty;
    private Double itemPrice;
    private Double taxPrice;
    private String comment = "";
    private int itemCode = 0;
    private int taxClass = 0;
    private int currencyId = 0;

    public Item() {
    }

    public Item(JSONObject jObj) {
        try {
            this.itemName = jObj.getString("itemName");
            this.itemPrice = jObj.getDouble("itemPrices");
            this.taxPrice = jObj.getDouble("taxPrice");
            this.comment = jObj.getString("itemComment");
            this.qty = jObj.getInt("qty");
            this.itemCode = jObj.getInt("itemCode");
            this.taxClass = jObj.getInt("taxClass");
            this.currencyId = jObj.getInt("currencyId");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(Double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public int getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(int taxClass) {
        this.taxClass = taxClass;
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
            obj.put("itemName", itemName);
            obj.put("qty", qty);
            obj.put("itemPrice", itemPrice);
            obj.put("taxPrice", taxPrice);
            obj.put("itemComment", comment);
            obj.put("itemCode", itemCode);
            obj.put("taxClass", taxClass);
            obj.put("currencyId", currencyId);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", qty=" + qty +
                ", itemPrice=" + itemPrice +
                ", taxPrice=" + taxPrice +
                ", comment='" + comment + '\'' +
                ", itemCode=" + itemCode +
                ", taxClass=" + taxClass +
                ", currencyId=" + currencyId +
                '}';
    }
}