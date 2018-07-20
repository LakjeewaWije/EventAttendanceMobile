package com.example.kliq.eventattendancemobile.scanner.RequestHandler;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface AttendUserOnResponse {
    void onAttendUserSuccess(JSONObject response);
    void onAttendUserError(VolleyError error);
}
