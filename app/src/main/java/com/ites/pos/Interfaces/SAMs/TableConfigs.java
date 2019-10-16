package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

/**
 * Created by wannix on 12/28/16.
 */

public interface TableConfigs {
    void gotTableConfigs(String data);
    void errorTableConfigs(VolleyError error);
}
