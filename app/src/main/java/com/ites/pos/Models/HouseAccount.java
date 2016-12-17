package com.ites.pos.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wannix on 12/1/16.
 */

public class HouseAccount {
    private String accountNo;
    private int hAccountId;
    private String status;
    private String employeeName;
    private String employeeNo;

    public HouseAccount() {
    }

    public HouseAccount(JSONObject jObj) {
        try {
            this.accountNo = ((String) jObj.get("accountNo")).trim();
            this.hAccountId = jObj.getInt("haccountID");
            this.status = ((String) jObj.get("status")).trim();
            this.employeeName = ((String) jObj.get("employeeName")).trim();
            this.employeeNo = ((String) jObj.get("employeeNo")).trim();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int gethAccountId() {
        return hAccountId;
    }

    public void sethAccountId(int hAccountId) {
        this.hAccountId = hAccountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    // convert java bean to JSON object
    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("accountNo", accountNo);
            obj.put("hAccountId", hAccountId);
            obj.put("status", status);
            obj.put("employeeName", employeeName);
            obj.put("employeeNo", employeeNo);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    // convert JSON object to java bean
    public HouseAccount jsonToJavaBean(JSONObject obj) {
        return new HouseAccount(obj);
    }

    @Override
    public String toString() {
        return "HouseAccount{" +
                "accountNo='" + accountNo + '\'' +
                ", hAccountId=" + hAccountId +
                ", status='" + status + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                '}';
    }
}
