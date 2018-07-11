package com.example.kliq.eventattendancemobile.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.kliq.eventattendancemobile.EventItem;
import com.example.kliq.eventattendancemobile.MainActivity;
import com.example.kliq.eventattendancemobile.MyAdapter;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.SharedPrefManager;
import com.example.kliq.eventattendancemobile.register.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.8.101:9000/user/login";
    public static final String kEY_EMAIL = "email";
    public static final String kEY_PASS = "password";
    private static Context mctx;
    private EditText email;
    private EditText password;
    private TextView registerHere;

    private Button loginButton;

    public SharedPrefManager shrd;
    public static LoggedInUser user;

    SharedPreferences menaPref;
    SharedPreferences.Editor editor;

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_AUTH_TOKEN = "authToken";



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
                    Log.v("loginButton.onClick", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = menaPref.edit();
    }

    private void loginUser() throws JSONException {

        JSONObject registerUserRequestBody = new JSONObject();
        registerUserRequestBody.put(kEY_EMAIL, email.getText().toString());
        registerUserRequestBody.put(kEY_PASS, password.getText().toString());

        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, registerUserRequestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("Successful", response.toString());



                try {

                    JSONObject obj = response.getJSONObject("data");

                    int userId  = obj.getInt("userId");
                    String fName = obj.getString("fName");
                    String lName = obj.getString("lName");
                    String authToken = obj.getString("authToken");

                    user = new LoggedInUser(userId,fName,lName,authToken);



                    editor = menaPref.edit();
                    editor.putString(KEY_FNAME, fName);
                    editor.putString(KEY_AUTH_TOKEN, authToken);
                    editor.apply();


//                    getSupportActionBar().setTitle("Hi"+user.getfName());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("onErrorResponse", error.getLocalizedMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(registerUserRequest);

    }
    /*public void sharedPref(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");

                            for (int i=0; i<array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                EventItem event = new EventItem(
                                        o.getString("eventName"),
                                        o.getString("eventDesc")
                                );

                                eventItems.add(event);
                            }

                            adapter = new MyAdapter(eventItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    }*/
}
