package com.ites.pos.Controllers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wannix on 10/25/16.
 */

class AppSingleton {
    private static AppSingleton instance;
    private Context ctx;
    private RequestQueue reqQueue;

    private AppSingleton(Context ctx) {
        this.ctx = ctx;
        reqQueue = getRequestQ();
    }

    private RequestQueue getRequestQ() {
        if (reqQueue == null) {
            reqQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return reqQueue;
    }

    static synchronized AppSingleton getInstance(Context ctx) {
        if (instance == null) {
            instance = new AppSingleton(ctx);
        }
        return instance;
    }

    <T> void addToRequestQueue(Request<T> request) {
        reqQueue.add(request);
    }

    void clearRequestQueue() {
        reqQueue.cancelAll(this);
    }
}
