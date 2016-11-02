package com.ites.pos.Controllers;

/**
 * Created by root on 10/26/16.
 */

public interface ResponseCallBack {
    void gotTableConfigs(String data);

    void gotOpenTableDetails(String data);
}
