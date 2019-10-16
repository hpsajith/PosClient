package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * Created by wannix on 1/25/17.
 */

public interface PosGuestDetails {
    void gotPosGuestDetails(String data);
    void errorPosGuestDetails(VolleyError error);
}
