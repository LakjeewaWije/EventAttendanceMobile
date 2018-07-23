package com.example.kliq.eventattendancemobile.user.RequestHandler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface LogoutOnResponse {

    void onLogoutSucess(JSONObject response);

    void onLogoutError(VolleyError error);
}
