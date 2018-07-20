package com.example.kliq.eventattendancemobile.user.RequestHandler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RegisterOnResponse {
    void onRegisterSucess(JSONObject response);

    void onRegisterError(VolleyError error);
}
