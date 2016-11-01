package com.ites.pos.Controllers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by wannix on 10/25/16.
 */

public class NetworkController {
    private Context ctx;

    // Webservice URL endpoints
    final private String urlGetTableConfigs = "http://10.1.1.66:8080/TableController/getAllTables";
    final private String urlGetOpenTables = "http://10.1.1.66:8080/TableController/getOpenTableDetail";

    public NetworkController(Context ctx) {
        this.ctx = ctx;
    }

    public void getOpenTableDetails(String tableId, final ResponseCallBack callBack){
        String url = urlGetOpenTables+"?id="+tableId;

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

    public void getAllTableConfigs(String restId, final ResponseCallBack callback){
        String url = urlGetTableConfigs+"?id="+restId;

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

//                Snackbar snackbar = Snackbar
//                        .make(ctx, "Message is deleted", Snackbar.LENGTH_LONG)
//                        .setAction("UNDO", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Snackbar snackbar1 = Snackbar.make(view, "Message is restored!", Snackbar.LENGTH_SHORT);
//                                snackbar1.show();
//                            }
//                        });
//
//                snackbar.show();
            }
        });

        AppSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public void clearRequestQueue(){
        AppSingleton.getInstance(ctx).clearRequestQueue();
    }
}