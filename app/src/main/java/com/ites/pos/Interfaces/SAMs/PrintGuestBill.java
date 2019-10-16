package com.ites.pos.Interfaces.SAMs;

import com.android.volley.VolleyError;

/**
 * Created by wannix on 2/2/17.
 */

public interface PrintGuestBill {
    void gotPrintGuestBill(String data);
    void errorPrintGuestBill(VolleyError error);
}
