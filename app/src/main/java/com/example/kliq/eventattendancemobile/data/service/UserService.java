package com.example.kliq.eventattendancemobile.data.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.kliq.eventattendancemobile.user.RequestHandler.LoginOnResponse;
import com.example.kliq.eventattendancemobile.user.RequestHandler.LogoutOnResponse;
import com.example.kliq.eventattendancemobile.user.RequestHandler.RegisterOnResponse;
import com.example.kliq.eventattendancemobile.util.RequestHandler;
import com.example.kliq.eventattendancemobile.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String LOGIN_URL = "http://192.168.8.101:9000/user/login"; // URL for user Login route

    SharedPrefManager sharedPref;

    //Declaring  Shared Preferences


  //  SharedPrefManager sharedPref;
   // public String authTok  = sharedPref.retrieveTokAsString();


    private static final String REGISTER_URL = "http://192.168.8.101:9000/user"; //URl to register user route

    private String URL_LOGOUT = "http://192.168.8.101:9000/user/log";


    public void loginUser(JSONObject jsonobj, final LoginOnResponse loginOnResponse) throws JSONException {

        JsonObjectRequest loginUserRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, jsonobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loginOnResponse.onLoginSucess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loginOnResponse.onLoginError(error);
            }
        });

        RequestHandler.getInstance((Context) loginOnResponse).addToRequestQueue(loginUserRequest);

    }

    public void registerUser(JSONObject jsonobj, final RegisterOnResponse registerOnResponse) throws JSONException {

         JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, jsonobj, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 registerOnResponse.onRegisterSucess(response);
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 registerOnResponse.onRegisterError(error);
             }
         });

        RequestHandler.getInstance((Context) registerOnResponse).addToRequestQueue(registerUserRequest);

    }


    public void logoutUser(final LogoutOnResponse logoutOnResponse) {
         JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                URL_LOGOUT, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logoutOnResponse.onLogoutSucess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logoutOnResponse.onLogoutError(error);
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();

                sharedPref = SharedPrefManager.getmInstance((Context)logoutOnResponse);
                String auth = sharedPref.retrieveTokAsString();
                Log.v("TOKEN",auth);
                headers.put("X-AUTH-TOKEN", auth);
                return headers;
            }
        };
         RequestHandler.getInstance((Context) logoutOnResponse).addToRequestQueue(jsonObjReq);
        //MainScreen.getInstance().addToRequestQueue(jsonObjReq,"headerRequest");
    }


}
