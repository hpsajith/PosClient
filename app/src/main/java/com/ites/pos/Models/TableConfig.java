package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 10/25/16.
 */

public class TableConfig {
    private String configid;
    private String restrauntId;
    private String room_Id;
    private String table_Id;
    private String tableName;
    private String roomName;
    private int tableStatus;

    public TableConfig() {
    }

    public TableConfig(String configid, String restrauntId, String room_Id, String table_Id, String tableName, String roomName, int tableStatus) {
        this.configid = configid;
        this.restrauntId = restrauntId;
        this.room_Id = room_Id;
        this.table_Id = table_Id;
        this.tableName = tableName;
        this.roomName = roomName;
        this.tableStatus = tableStatus;
    }

    public TableConfig(JSONObject jObj) {
        try {
            this.configid = jObj.get("configid").toString();
            this.restrauntId = jObj.get("restrauntId").toString();
            this.room_Id = jObj.get("roomId").toString();
            this.table_Id = jObj.get("tableId").toString();
            this.tableName = jObj.get("tableName").toString();
            this.roomName = jObj.get("restRoomName").toString();
            this.tableStatus = (int)jObj.get("tableStatus");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getConfigid() {
        return configid;
    }

    public void setConfigid(String configid) {
        this.configid = configid;
    }

    public String getRestrauntId() {
        return restrauntId;
    }

    public void setRestrauntId(String restrauntId) {
        this.restrauntId = restrauntId;
    }

    public String getRoom_Id() {
        return room_Id;
    }

    public void setRoom_Id(String room_Id) {
        this.room_Id = room_Id;
    }

    public String getTable_Id() {
        return table_Id;
    }

    public void setTable_Id(String table_Id) {
        this.table_Id = table_Id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(int tableStatus) {
        this.tableStatus = tableStatus;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("configid", configid);
            obj.put("restrauntId", restrauntId);
            obj.put("room_Id", room_Id);
            obj.put("table_Id", table_Id);
            obj.put("tableName", tableName);
            obj.put("roomName", roomName);
            obj.put("tableStatus", tableStatus);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    // convert JSON object to java bean
    public TableConfig jsonToJavaBean(JSONObject obj) {
        return new TableConfig(obj);
    }

    @Override
    public String toString() {
        return "TableConfig{" +
                "configid='" + configid + '\'' +
                ", restrauntId='" + restrauntId + '\'' +
                ", room_Id='" + room_Id + '\'' +
                ", table_Id='" + table_Id + '\'' +
                ", tableName='" + tableName + '\'' +
                ", roomName='" + roomName + '\'' +
                ", tableStatus='" + tableStatus + '\'' +
                '}';
    }

}
