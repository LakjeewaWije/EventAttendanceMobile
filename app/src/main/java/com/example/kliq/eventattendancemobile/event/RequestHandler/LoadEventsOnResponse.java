package com.example.kliq.eventattendancemobile.event.RequestHandler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface LoadEventsOnResponse {
    void onLoadEventsSucess(String response);

    void onLoadEventsError(VolleyError error);
}
