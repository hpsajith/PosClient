package com.ites.pos;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ites.pos.Interfaces.SAMs.AllUsers;
import com.ites.pos.Interfaces.SAMs.CloseGuestBill;
import com.ites.pos.Interfaces.SAMs.HouseAccList;
import com.ites.pos.Interfaces.SAMs.OpenTableDetails;
import com.ites.pos.Interfaces.SAMs.OrderPlaced;
import com.ites.pos.Interfaces.SAMs.PosGuestDetails;
import com.ites.pos.Interfaces.SAMs.PrintGuestBill;
import com.ites.pos.Interfaces.SAMs.ReservationRoomList;
import com.ites.pos.Interfaces.SAMs.RestaurantItems;
import com.ites.pos.Interfaces.SAMs.TableConfigs;
import com.ites.pos.Interfaces.SAMs.UserAuth;
import com.ites.pos.Models.Item;
import com.ites.pos.Models.OrderDetails;
import com.ites.pos.Models.RestaurantItem;
import com.ites.pos.Models.VoidItem;

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
    private static final int REQUEST_TIMEOUT = 10000; // timeout 10000 mills
    private Context ctx;

    // Webservice URL endpoints
    String host = "10.1.1.66";
    //    String host = "173.243.120.226";
    String port = "8084";//http://itespms.com/POSservice/UserController/getAllUsers
    //String hostPort = "http://itespms.com"; //"http://"+host+":"+port+
    String contextPath = "/POSservice";//"";

    /*
    final String urlGetUsers = hostPort+contextPath+"/UserController/getAllUsers";
    final String urlAuthUser = hostPort+contextPath+"/UserController/validateLoginUser";
    final String urlGetTableConfigs = hostPort+contextPath+"/TableController/getAllTables";
    final String urlGetOpenTables = hostPort+contextPath+"/TableController/getOpenTableDetail";
    final String urlGetReservationRoomList = hostPort+contextPath+"/ReservationController/getReservationRooms";
    final String urlGetHouseAccList = hostPort+contextPath+"/ReservationController/getHouseAccList";
    final String urlSendGuestDetails = hostPort+contextPath+"/GuestController/setPosGuestDetails";
    final String urlPlaceOrder = hostPort+contextPath+"/OrderController/setNewOrderedItemList";
    final String urlGetRestaurantItems = hostPort+contextPath+"/GuestController/getRestaurantItem";
    final String urlGetPosGuestDetails = hostPort+contextPath+"/GuestController/getPosGuestDetail";
    final String urlPrintGuestBill = hostPort+contextPath+"/GuestController/printGuestBill";
    final String urlExitOrder = hostPort+contextPath+"/OrderController/setExitItemList";
    final String urlCloseGuestBill = hostPort+contextPath+"/TableController/closeGuestBill";
    */

    final String urlGetUsers = "http://" + host + ":" + port + contextPath + "/UserController/getAllUsers";
    final String urlAuthUser = "http://" + host + ":" + port + contextPath + "/UserController/validateLoginUser";
    final String urlGetTableConfigs = "http://" + host + ":" + port + contextPath + "/TableController/getAllTables";
    final String urlGetOpenTables = "http://" + host + ":" + port + contextPath + "/TableController/getOpenTableDetail";
    final String urlGetReservationRoomList = "http://" + host + ":" + port + contextPath + "/ReservationController/getReservationRooms";
    final String urlGetHouseAccList = "http://" + host + ":" + port + contextPath + "/ReservationController/getHouseAccList";
    final String urlSendGuestDetails = "http://" + host + ":" + port + contextPath + "/GuestController/setPosGuestDetails";
    final String urlPlaceOrder = "http://" + host + ":" + port + contextPath + "/OrderController/setNewOrderedItemList";
    final String urlGetRestaurantItems = "http://" + host + ":" + port + contextPath + "/GuestController/getRestaurantItem";
    final String urlGetPosGuestDetails = "http://" + host + ":" + port + contextPath + "/GuestController/getPosGuestDetail";
    final String urlPrintGuestBill = "http://" + host + ":" + port + contextPath + "/GuestController/printGuestBill";
    final String urlExitOrder = "http://" + host + ":" + port + contextPath + "/OrderController/setExitItemList";
    final String urlCloseGuestBill = "http://" + host + ":" + port + contextPath + "/TableController/closeGuestBill";
/*
     * for the Demo *
     * final String urlGetUsers = "http://10.1.1.21:8080/POSservice/UserController/getAllUsers";
     * final String urlAuthUser = "http://10.1.1.21:8080/POSservice/UserController/validateLoginUser";
     * final private String urlGetTableConfigs = "http://10.1.1.21:8080/POSservice/TableController/getAllTables";
     * final private String urlGetOpenTables = "http://10.1.1.21:8080/POSservice/TableController/getOpenTableDetail";
     * final String urlGetReservationRoomList = "http://10.1.1.21:8080/POSservice/ReservationController/getReservationRooms";
     * final String urlGetHouseAccList = "http://10.1.1.21:8080/POSservice/ReservationController/getHouseAccList";
     * final String urlSendGuestDetails = "http://10.1.1.21:8080/POSservice/GuestController/setPosGuestDetails";
     * final String urlPlaceOrder = "http://10.1.1.21:8080/POSservice/OrderController/setNewOrderedItemList";
     * final String urlGetRestaurantItems = "http://10.1.1.21:8080/POSservice/GuestController/getRestaurantItem";
     * final String urlGetPosGuestDetails = "http://10.1.1.21:8080/POSservice/GuestController/getPosGuestDetail";
     */

    public NetworkController(Context ctx) {
        this.ctx = ctx;
    }

    public void exitGuestOrder(final List<Item> billItems, final List<VoidItem> voidItems, final OrderDetails orderDetails, final OrderPlaced callBack) {
        StringRequest request = new StringRequest(Request.Method.POST, urlExitOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callBack.gotOrderPlaced(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorOrderPlaced(error);
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

                Iterator<VoidItem> vi = voidItems.iterator();
                JSONArray voidItemBucket = new JSONArray();

                while (vi.hasNext()) {
                    JSONObject voidItem = vi.next().toJSONObject();
                    voidItemBucket.put(voidItem);
                }

                JSONObject orderDetailsJson = orderDetails.toJSONObject();

                params.put("orderDetails", orderDetailsJson.toString());
                params.put("voidItemBucket", voidItemBucket.toString());
                params.put("itemBucket", itemBucket.toString());
                return params;
            }
        };

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void printGuestBill(int kotNo, int userId, int restId, String userName, final PrintGuestBill callBack) {
        String url = urlPrintGuestBill + "?kotNo=" + kotNo + "&" + "userId=" + userId + "&" + "restId=" + restId + "&" + "userName=" + userName;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.gotPrintGuestBill(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorPrintGuestBill(error);
            }
        });

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void closeGuestBill(String kotNo, final CloseGuestBill callBack) {
        String url = urlCloseGuestBill + "?kotNo=" + kotNo;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.gotCloseGuestBill(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorCloseGuestBill(error);
            }
        });

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getPosGuestDetails(int posGuestNo, final PosGuestDetails callBack) {
        String url = urlGetPosGuestDetails + "?guestNo=" + posGuestNo;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.gotPosGuestDetails(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorPosGuestDetails(error);
            }
        });

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getRestaurantItems(int restId, final RestaurantItems callBack) {
        String url = urlGetRestaurantItems + "?restId=" + restId;

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callBack.gotRestaurantItems(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorRestaurantItems(error);
            }
        });

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void placeGuestOrder(final List<Item> billItems, final List<VoidItem> voidItems, final OrderDetails orderDetails, final OrderPlaced callBack) {
        StringRequest request = new StringRequest(Request.Method.POST, urlPlaceOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callBack.gotOrderPlaced(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorOrderPlaced(error);
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

                Iterator<VoidItem> vi = voidItems.iterator();
                JSONArray voidItemBucket = new JSONArray();

                while (vi.hasNext()) {
                    JSONObject voidItem = vi.next().toJSONObject();
                    voidItemBucket.put(voidItem);
                }

                JSONObject orderDetailsJson = orderDetails.toJSONObject();

                params.put("orderDetails", orderDetailsJson.toString());
                params.put("voidItemBucket", voidItemBucket.toString());
                params.put("itemBucket", itemBucket.toString());

                return params;
            }
        };

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

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
                callBack.errorRestaurantItems(error);
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
                callBack.errorReservationRoomList(error);
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
                callBack.errorHouseAccList(error);
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
                callBack.errorAllUsers(error);
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
                        System.out.println("response  01 === " + response);
                        callBack.gotUserAuth(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.errorUserAuth(error);
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
                callBack.errorOpenTableDetails(error);
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void getAllTableConfigs(String restId, String userId, final TableConfigs callback) {
        String url = urlGetTableConfigs + "?id=" + restId + "&userId=" + userId;

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // callback
                callback.gotTableConfigs(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.errorTableConfigs(error);
            }
        });

        // request timeout
        RetryPolicy policy = new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void clearRequestQueue() {
        AppSingleton.getInstance(ctx).clearRequestQueue();
    }
}