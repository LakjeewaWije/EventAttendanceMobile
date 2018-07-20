package com.example.kliq.eventattendancemobile.user.RequestHandler;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface LoginOnResponse {

    void onLoginSucess(JSONObject response);

    void onLoginError(VolleyError error);

}
