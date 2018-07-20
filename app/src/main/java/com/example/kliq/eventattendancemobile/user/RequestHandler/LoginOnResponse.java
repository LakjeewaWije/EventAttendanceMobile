package com.example.kliq.eventattendancemobile.user;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface LoginOnResponse {

    void onLoginSucess(JSONObject response);

    void onLoginError(VolleyError error);

}
