package com.example.kliq.eventattendancemobile.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.RequestHandler;
import com.example.kliq.eventattendancemobile.SharedPrefManager;
import com.example.kliq.eventattendancemobile.register.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.8.101:9000/user/login";

    private EditText email;
    private EditText password;
    private TextView registerHere;
    private ProgressDialog progressDialog;
    private Button loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerHere = (TextView) findViewById(R.id.messageTwo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading events..");



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
                    Log.v("loginButton.onClick", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginUser() throws JSONException {
        final String emailAddress = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")) {
                        SharedPrefManager.getmInstance(getApplicationContext()).userLogin(
                                obj.getString("firstName"),
                                obj.getString("authToken"));

                        Toast.makeText(
                                getApplicationContext(),
                                "Login Successful" ,
                                Toast.LENGTH_LONG
                        ).show();

                    }else {
                        Toast.makeText(
                                getApplicationContext(),
                                obj.getString("message") ,
                                Toast.LENGTH_LONG
                        ).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage() ,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(emailAddress, emailAddress);
                params.put(pass, pass);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);


    }
}


