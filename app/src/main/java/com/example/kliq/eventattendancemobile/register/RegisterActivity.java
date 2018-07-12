package com.example.kliq.eventattendancemobile.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.login.LoginActivity;


import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://192.168.8.101:9000/user";
    public static final String kEY_FNAME = "fName";
    public static final String kEY_LNAME = "lName";
    public static final String kEY_EMAIL = "eMail";
    public static final String kEY_PASS = "password";

    private EditText fName;
    private EditText lName;
    private EditText eMail;
    private EditText password;
    private TextView loginHere;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fName = (EditText) findViewById(R.id.firstName);
        lName = (EditText) findViewById(R.id.lastName);
        eMail = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginHere = (TextView) findViewById(R.id.messageTwo);

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (JSONException e) {
                    Log.v("registerButton.onClick",e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }

     private void registerUser() throws JSONException {
        final String firstName = fName.getText().toString().trim();
        final String lastName = lName.getText().toString().trim();
        final String email = eMail.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        JSONObject registerUserRequestBody = new JSONObject();
        registerUserRequestBody.put(kEY_FNAME,firstName);
        registerUserRequestBody.put(kEY_LNAME,lastName);
        registerUserRequestBody.put(kEY_EMAIL,email);
        registerUserRequestBody.put(kEY_PASS,pass);



        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, registerUserRequestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("onresponse",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("onErrorResponse",error.getLocalizedMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerUserRequest);



    }

}

