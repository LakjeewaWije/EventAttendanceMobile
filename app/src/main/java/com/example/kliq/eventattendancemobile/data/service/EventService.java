
package com.example.kliq.eventattendancemobile.data.service;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.data.model.Event;
import com.example.kliq.eventattendancemobile.event.EventActivity;
import com.example.kliq.eventattendancemobile.event.EventAdapter;
import com.example.kliq.eventattendancemobile.event.RequestHandler.LoadEventsOnResponse;
import com.example.kliq.eventattendancemobile.scanner.AttendanceScanActivity;
import com.example.kliq.eventattendancemobile.scanner.RequestHandler.AttendUserOnResponse;
import com.example.kliq.eventattendancemobile.user.LoginActivity;
import com.example.kliq.eventattendancemobile.user.RequestHandler.RegisterOnResponse;
import com.example.kliq.eventattendancemobile.util.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventService {

    private RequestQueue requestQueue;
    private static EventActivity mInstance;


    //public static final int PERMISSION_REQUEST = 100;
    private List<Event> eventItems;
    private RecyclerView.Adapter adapter;

    private String name; // Current Users Name
    private String authTok; // Current Users Auth Token

    // URL for loading events route
    // URL for log out User route
    // Declaring Shared Preferences
    private static final String SHARED_PREF_NAME = "sharedPref";

    //Intialising the Shred Preference Key Name

    private static final String KEY_FNAME = "fName"; // First Name
    private static final String KEY_AUTH_TOKEN = "authToken"; // Auth Token

    public static final String URL_DATA = "http://192.168.8.104:9000/mob";


    public void loadEvents( final LoadEventsOnResponse loadEventsOnResponse) throws JSONException {

        StringRequest loadEventsRequest = new StringRequest(Request.Method.GET, URL_DATA,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadEventsOnResponse.onLoadEventsSucess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadEventsOnResponse.onLoadEventsError(error);
            }
        });

        RequestHandler.getInstance((Context) loadEventsOnResponse).addToRequestQueue(loadEventsRequest);

    }

    public void attendUser(final AttendUserOnResponse attendUserOnResponse , JSONObject registerUserRequestBody) throws JSONException {
        JsonObjectRequest attendUserrequest = new JsonObjectRequest(Request.Method.POST, URL_DATA,registerUserRequestBody , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                attendUserOnResponse.onAttendUserSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                attendUserOnResponse.onAttendUserError(error);
            }
        });
        RequestHandler.getInstance((Context) attendUserOnResponse).addToRequestQueue(attendUserrequest);
    }
}






