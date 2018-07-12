package com.example.kliq.eventattendancemobile.qr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResultScreen extends AppCompatActivity {
    TextView eventID; //eventname Text view
    TextView eventName; //eventid Text View
    TextView barcodestring; // barcode Text View
    public String barcodeToString=""; //barcode object value to String
    public String evntnameb=""; //event name to save eventname taken from barcode json
    public String eventidb=""; //event id to save eventid taken from barcode json
    public String eventNamei=""; //event name to save eventname taken from previos intent
    public String eventIdi=""; //event id to save eventid taken from previos intent
    public static JSONObject jsonObject=null;
    public static final String URL_DATA = "http://192.168.8.104:9000/ed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        eventID = (TextView) findViewById(R.id.eventid);
        eventName = (TextView) findViewById(R.id.eventname);
        barcodestring = (TextView) findViewById(R.id.barcodestring);
        Intent intent = getIntent();
        eventIdi= intent.getStringExtra("eventId");
        eventNamei=intent.getStringExtra("eventName");
        final  Barcode barcode = intent.getParcelableExtra("barcode");
            barcodeToString = barcode.displayValue;
        barcodestring.post(new Runnable() {
            @Override
            public void run() {
               barcodestring.setText(barcode.displayValue);
                eventID.setText(eventidb.toString());
                eventName.setText(eventIdi);
            }
        });
        try {
           jsonObject = new JSONObject(barcodeToString);
                eventidb=jsonObject.getString("eventId");
                evntnameb=jsonObject.getString("eventName");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        attendUser();

    }

    public void attendUser(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        if (eventidb.equals(eventIdi)){
            progressDialog.setMessage("mathces");
            progressDialog.show();

            JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, URL_DATA, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

//                    Log.v("onresponse",response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.v("onErrorResponse",error.getLocalizedMessage());
                }
            });
            queue.add(registerUserRequest);

        }else {
            progressDialog.setMessage("No");
            progressDialog.show();
        }
        progressDialog.cancel();


        }
}
