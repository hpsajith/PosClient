package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

/**
 * Created by wannix on 12/28/16.
 */

public interface AllUsers {
    void gotAllUsers(String data);
    void errorAllUsers(VolleyError error);
}
