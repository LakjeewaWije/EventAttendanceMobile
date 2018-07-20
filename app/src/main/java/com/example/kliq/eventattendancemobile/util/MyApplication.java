package com.example.kliq.eventattendancemobile.util;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();


        requestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }



}

