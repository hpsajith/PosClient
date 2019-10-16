package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 11/1/16.
 */

public class OrderBillItem {
    private String tableNo;
    private String tableName;
    private String guestNo;
    private String kotNo;
    private String systemDate;
    private String itemNo;
    private String itemCode;
    private String itemName;
    private String unitName;
    private String qty;
    private String unitPrice;
    private String taxPrice;
    private String roomNo;
    private String userName;
    private String itemComment;
    private Double itemPrices;
    private int taxClass;
    private int currencyId;

    public OrderBillItem() {
    }

    public OrderBillItem(JSONObject jObj) {
        try {
            this.tableNo = jObj.get("tableNo").toString().trim();
            this.tableName = jObj.get("tableName").toString().trim();
            this.guestNo = jObj.get("posGuestno").toString().trim();
            this.kotNo = jObj.get("kotNo").toString().trim();
            this.systemDate = jObj.get("systemDate").toString().trim();
            this.itemNo = jObj.get("itemNo").toString().trim();
            this.itemCode = jObj.get("itemCode").toString().trim();
            this.itemName = jObj.get("itemName").toString().trim();
            this.unitName = jObj.get("unitName").toString().trim();
            this.qty = jObj.get("qty").toString().trim();
            this.unitPrice = jObj.get("unitPrice").toString().trim();
            this.taxPrice = jObj.get("itemPrices").toString().trim();
            this.roomNo = jObj.get("roomNo").toString().trim();
            this.userName = jObj.get("userName").toString().trim();
            this.itemComment = jObj.getString("itemcomment").trim();
            this.itemPrices = jObj.getDouble("itemPrices");
            this.taxClass = jObj.getInt("taxClass");
            this.currencyId = jObj.getInt("currencyId");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(String guestNo) {
        this.guestNo = guestNo;
    }

    public String getKotNo() {
        return kotNo;
    }

    public void setKotNo(String kotNo) {
        this.kotNo = kotNo;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getItemComment() {
        return itemComment;
    }

    public void setItemComment(String itemComment) {
        this.itemComment = itemComment;
    }

    public Double getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(Double itemPrices) {
        this.itemPrices = itemPrices;
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
            obj.put("tableNo", tableNo);
            obj.put("tableName", tableName);
            obj.put("guestNo", guestNo);
            obj.put("kotNo", kotNo);
            obj.put("systemDate", systemDate);
            obj.put("itemNo", itemNo);
            obj.put("itemCode", itemCode);
            obj.put("itemName", itemName);
            obj.put("unitName", unitName);
            obj.put("qty", qty);
            obj.put("unitPrice", unitPrice);
            obj.put("taxPrice", taxPrice);
            obj.put("roomNo", roomNo);
            obj.put("userName", userName);
            obj.put("itemComment", itemComment);
            obj.put("itemPrices", itemPrices);
            obj.put("taxClass", taxClass);
            obj.put("currencyId", currencyId);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // convert JSON object to java bean
    public OrderBillItem jsonToJavaBean(JSONObject obj) {
        return new OrderBillItem(obj);
    }

    @Override
    public String toString() {
        return "OrderBillItem{" +
                "tableNo='" + tableNo + '\'' +
                ", tableName='" + tableName + '\'' +
                ", guestNo='" + guestNo + '\'' +
                ", kotNo='" + kotNo + '\'' +
                ", systemDate='" + systemDate + '\'' +
                ", itemNo='" + itemNo + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", qty='" + qty + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", taxPrice='" + taxPrice + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", userName='" + userName + '\'' +
                ", itemComment='" + itemComment + '\'' +
                ", itemPrices=" + itemPrices +
                ", taxClass=" + taxClass +
                ", currencyId=" + currencyId +
                '}';
    }
}