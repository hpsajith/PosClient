package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

/**
 * Created by wannix on 12/28/16.
 */

public interface OpenTableDetails {
    void gotOpenTableDetails(String data);
    void errorOpenTableDetails(VolleyError error);
}
