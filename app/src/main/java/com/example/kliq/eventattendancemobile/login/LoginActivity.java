package com.example.kliq.eventattendancemobile.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.8.101:9000/user/login";
    public static final String kEY_EMAIL = "email";
    public static final String kEY_PASS = "password";

    private EditText email;
    private EditText password;
    private TextView registerHere;

    private Button loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerHere = (TextView) findViewById(R.id.messageTwo);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginUser();
                } catch (JSONException e) {
                    Log.v("loginButton.onClick",e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginUser() throws JSONException {

        JSONObject registerUserRequestBody = new JSONObject();
        registerUserRequestBody.put(kEY_EMAIL,email.getText().toString());
        registerUserRequestBody.put(kEY_PASS,password.getText().toString());



        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, registerUserRequestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("Sucessfull",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("onErrorResponse",error.getLocalizedMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(registerUserRequest);



    }

}
