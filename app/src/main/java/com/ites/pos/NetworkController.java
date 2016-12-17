package com.ites.pos;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ites.pos.Activities.Login;
import com.ites.pos.Activities.LoginSuccess;
import com.ites.pos.Models.ReservationRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wannix on 10/25/16.
 */

public class NetworkController {
    private Context ctx;

    // Webservice URL endpoints
//    final String urlGetUsers = "http://10.1.1.66:8084/UserController/getAllUsers";
        final String urlGetUsers = "http://192.168.43.178:8080/UserController/getAllUsers";
//    final String urlAuthUser = "http://10.1.1.66:8084/UserController/validateLoginUser";
        final String urlAuthUser = "http://192.168.43.178:8080/UserController/validateLoginUser";
//    final String urlGetTableConfigs = "http://10.1.1.66:8084/TableController/getAllTables";
        final private String urlGetTableConfigs = "http://192.168.43.178:8080/TableController/getAllTables";
//    final String urlGetOpenTables = "http://10.1.1.66:8084/TableController/getOpenTableDetail";
        final private String urlGetOpenTables = "http://192.168.43.178:8080/TableController/getOpenTableDetail";
//    final String urlGetReservationRoomList = "http://10.1.1.66:8084/ReservationController/getReservationRooms";
    final String urlGetReservationRoomList = "http://192.168.43.178:8080/ReservationController/getReservationRooms";
//    final String urlGetHouseAccList = "http://10.1.1.66:8084/ReservationController/getHouseAccList";
    final String urlGetHouseAccList = "http://192.168.43.178:8080/ReservationController/getHouseAccList";
//    final String urlSendGuestDetails = "http://10.1.1.66:8084/GuestController/setPosGuestDetails";
    final String urlSendGuestDetails = "http://192.168.43.178:8080/GuestController/setPosGuestDetails";

    public NetworkController(Context ctx) {
        this.ctx = ctx;
    }

    public void sendGuestDetails(final JSONObject genericObj, final ResponseCallBack callBack) {
        StringRequest request = new StringRequest(Request.Method.POST, urlSendGuestDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callBack.gotRestaurantItems(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Debug", "Error: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Iterator<?> keys = genericObj.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    try {
                        String val = genericObj.getString(key);
                        params.put(key, val);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }
        };

        Log.d("dispatched", "Request dispatched!");

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getReservationRoomList(final ResponseCallBack callBack) {

        JsonArrayRequest request = new JsonArrayRequest(urlGetReservationRoomList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // callback
                        callBack.gotReservationRoomList(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Debug", "Error: " + error.getMessage());
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getHouseAccListList(final ResponseCallBack callBack) {

        JsonArrayRequest request = new JsonArrayRequest(urlGetHouseAccList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // callback
                        callBack.gotHouseAccList(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Debug", "Error: " + error.getMessage());
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getAllUsers(final ResponseCallBack callBack) {

        JsonArrayRequest request = new JsonArrayRequest(urlGetUsers,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // callback
                        callBack.gotAllUsers(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Debug", "Error: " + error.getMessage());
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void authenticateUser(String username, String password, final ResponseCallBack callBack) {
        String urlAuth = urlAuthUser + "?username=" + username + "&password=" + password;

        JsonArrayRequest request = new JsonArrayRequest(urlAuth,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // response callback
                        callBack.gotUserAuth(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Debug", "Error: " + error.getMessage());
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getOpenTableDetails(String tableId, final ResponseCallBack callBack) {
        String url = urlGetOpenTables + "?id=" + tableId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // callback
                callBack.gotOpenTableDetails(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ctx, "Error Occured! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getAllTableConfigs(String restId, final ResponseCallBack callback) {
        String url = urlGetTableConfigs + "?id=" + restId;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // callback
                callback.gotTableConfigs(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Error Occured! Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void clearRequestQueue() {
        AppSingleton.getInstance(ctx).clearRequestQueue();
    }
}