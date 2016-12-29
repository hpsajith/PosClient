package com.ites.pos;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ites.pos.Interfaces.SAMs.AllUsers;
import com.ites.pos.Interfaces.SAMs.HouseAccList;
import com.ites.pos.Interfaces.SAMs.OpenTableDetails;
import com.ites.pos.Interfaces.SAMs.ReservationRoomList;
import com.ites.pos.Interfaces.SAMs.RestaurantItems;
import com.ites.pos.Interfaces.SAMs.TableConfigs;
import com.ites.pos.Interfaces.SAMs.UserAuth;
import com.ites.pos.Models.Item;
import com.ites.pos.Models.OrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wannix on 10/25/16.
 */

public class NetworkController {
    private Context ctx;

    // Webservice URL endpoints
    final String urlGetUsers = "http://10.1.1.66:8084/UserController/getAllUsers";
    //        final String urlGetUsers = "http://192.168.43.178:8080/UserController/getAllUsers";
    final String urlAuthUser = "http://10.1.1.66:8084/UserController/validateLoginUser";
    //        final String urlAuthUser = "http://192.168.43.178:8080/UserController/validateLoginUser";
    final String urlGetTableConfigs = "http://10.1.1.66:8084/TableController/getAllTables";
    //        final private String urlGetTableConfigs = "http://192.168.43.178:8080/TableController/getAllTables";
    final String urlGetOpenTables = "http://10.1.1.66:8084/TableController/getOpenTableDetail";
    //        final private String urlGetOpenTables = "http://192.168.43.178:8080/TableController/getOpenTableDetail";
    final String urlGetReservationRoomList = "http://10.1.1.66:8084/ReservationController/getReservationRooms";
    //    final String urlGetReservationRoomList = "http://192.168.43.178:8080/ReservationController/getReservationRooms";
    final String urlGetHouseAccList = "http://10.1.1.66:8084/ReservationController/getHouseAccList";
    //    final String urlGetHouseAccList = "http://192.168.43.178:8080/ReservationController/getHouseAccList";
    final String urlSendGuestDetails = "http://10.1.1.66:8084/GuestController/setPosGuestDetails";
    //    final String urlSendGuestDetails = "http://192.168.43.178:8080/GuestController/setPosGuestDetails";
    final String urlPlaceOrder = "http://10.1.1.66:8084/OrderController/setNewOrderedItemList";

    public NetworkController(Context ctx) {
        this.ctx = ctx;
    }

    public void placeGuestOrder(final List<Item> billItems, final OrderDetails orderDetails) {
        StringRequest request = new StringRequest(Request.Method.POST, urlPlaceOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        callBack.gotResponse(response);
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

                Iterator<Item> i = billItems.iterator();
                JSONArray itemBucket = new JSONArray();

                while (i.hasNext()) {
                    JSONObject item = i.next().toJSONObject();
                    itemBucket.put(item);
                }

                JSONObject orderDetailsJson = orderDetails.toJSONObject();

                params.put("orderDetails", orderDetailsJson.toString());
                params.put("itemBucket", itemBucket.toString());

                return params;
            }
        };

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void sendGuestDetails(final JSONObject genericObj, final RestaurantItems callBack) {
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

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getReservationRoomList(final ReservationRoomList callBack) {

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

    public void getHouseAccListList(final HouseAccList callBack) {

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

    public void getAllUsers(final AllUsers callBack) {

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

    public void authenticateUser(String username, String password, final UserAuth callBack) {
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

    public void getOpenTableDetails(String tableId, final OpenTableDetails callBack) {
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

    public void getAllTableConfigs(String restId, final TableConfigs callback) {
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