package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 10/25/16.
 */

public class User {
    private String userId;
    private String userName;
    private Integer userType;
    private Integer userStatus;

    public User(String userId, String userName, Integer userType, Integer userStatus) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public User(JSONObject jObj){
        try {
            this.userId = jObj.getString("userId");
            this.userName = jObj.getString("userName");
            this.userType = jObj.getInt("userType");
            this.userStatus = jObj.getInt("active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public JSONObject toJSONobject(){
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("userId", userId);
            jObj.put("userName", userName);
            jObj.put("userType", userType);
            jObj.put("userStatus", userStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", userStatus=" + userStatus +
                '}';
    }
}
