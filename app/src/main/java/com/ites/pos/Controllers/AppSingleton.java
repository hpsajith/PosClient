package com.ites.pos.Controllers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wannix on 10/25/16.
 */

public class AppSingleton {
    private static AppSingleton instance;
    private static Context ctx;
    private RequestQueue reqQueue;

    public AppSingleton(Context ctx) {
        this.ctx = ctx;
        reqQueue = getRequestQ();
    }

    public RequestQueue getRequestQ(){
        if(reqQueue == null){
            reqQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return reqQueue;
    }

    public static synchronized AppSingleton getInstance(Context ctx){
        if(instance == null){
            instance = new AppSingleton(ctx);
        }
        return instance;
    }

    public<T> void addToRequestQueue(Request<T> request){
        reqQueue.add(request);
    }

    public void clearRequestQueue(){
        reqQueue.cancelAll(this);
    }
}
