package com.ites.pos;

/**
 * Created by wannix on 10/26/16.
 */

public interface ResponseCallBack {
    void gotAllUsers(String data);

    void gotUserAuth(String data);

    void gotTableConfigs(String data);

    void gotOpenTableDetails(String data);

    void gotReservationRoomList(String data);

    void gotHouseAccList(String data);

    void gotRestaurantItems(String data);
}
