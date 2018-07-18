package com.example.kliq.eventattendancemobile.data.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.event.MainScreen;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultScreen extends AppCompatActivity {
    TextView eventID; //eventname Text view
    TextView eventName; //eventid Text View
    TextView barcodestring; // barcode Text View
    ImageView statusimg; // Succes image View
    Button goback; // Go back Button

    public String barcodeToString=""; //barcode object value to String
    public String evntnameb=""; //event name to save eventname taken from barcode json
    public String eventidb=""; //event id to save eventid taken from barcode json
    public String eventNamei=""; //event name to save eventname taken from previos intent
    public String eventIdi=""; //event id to save eventid taken from previos intent

    public static JSONObject registerUserRequestBody = new JSONObject();
    public String browsertokenb="";
    public String uuidb="";
    public String authtoken;

    //----------------------------------------------------------------------
    SharedPreferences menaPref;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_AUTH_TOKEN = "authToken";
    //----------------------------------------------------------------------
    public static JSONObject jsonObject=null;
    public static final String URL_DATA = "http://192.168.8.104:9000/ed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        eventID = (TextView) findViewById(R.id.eventid);
        eventName = (TextView) findViewById(R.id.eventname);
        barcodestring = (TextView) findViewById(R.id.barcodestring);
        statusimg = (ImageView) findViewById(R.id.successtatus);
        goback = (Button) findViewById(R.id.goback);

        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = menaPref.edit();
        authtoken = menaPref.getString(KEY_AUTH_TOKEN,"");
        final Intent intent = getIntent();
        eventIdi= intent.getStringExtra("eventId");
        eventNamei=intent.getStringExtra("eventName");
        final Barcode barcode = intent.getParcelableExtra("barcode");
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
            uuidb=jsonObject.getString("UUID");
            browsertokenb=jsonObject.getString("browserToken");


            registerUserRequestBody.put("eventId",eventidb);
            registerUserRequestBody.put("eventName",evntnameb);
            registerUserRequestBody.put("UUID",uuidb);
            registerUserRequestBody.put("browserToken",browsertokenb);
            registerUserRequestBody.put("authToken",authtoken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        attendUser();
        // Register link Onclick
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(com.example.kliq.eventattendancemobile.data.service.ResultScreen.this, MainScreen.class);
                startActivity(intent1);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(com.example.kliq.eventattendancemobile.data.service.ResultScreen.this,MainScreen.class);
        startActivity(intent2);
        finish();
    }

    public void attendUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        if (eventidb.equals(eventIdi)){
            try {
                JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, URL_DATA,registerUserRequestBody , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"U got Registered",Toast.LENGTH_SHORT).show();
//                        statusimg.setImageResource(R.drawable.tick);
//                        finish();
//                    Log.v("onresponse",response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        statusimg.setImageResource(R.drawable.cross);
                        Toast.makeText(getApplicationContext(),"Error on Response",Toast.LENGTH_SHORT).show();
//                    Log.v("onErrorResponse",error.getLocalizedMessage());
                    }
                });
                queue.add(registerUserRequest);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Error on catch",Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(getApplicationContext(),"Events Don't Match",Toast.LENGTH_SHORT).show();
            statusimg.setImageResource(R.drawable.unrelatedqr);
        }

    }
}
