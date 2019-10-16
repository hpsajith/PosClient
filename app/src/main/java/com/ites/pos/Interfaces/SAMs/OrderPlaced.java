package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

/**
 * Created by wannix on 1/25/17.
 */

public interface OrderPlaced {
    void gotOrderPlaced(String data);
    void errorOrderPlaced(VolleyError error);
}
