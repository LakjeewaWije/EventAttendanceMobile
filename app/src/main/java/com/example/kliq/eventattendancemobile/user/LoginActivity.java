package com.example.kliq.eventattendancemobile.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.data.model.User;
import com.example.kliq.eventattendancemobile.event.EventActivity;
import com.facebook.stetho.Stetho;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.8.104:9000/user/login"; // URL for user Login route
    public static final String kEY_EMAIL = "email";
    public static final String kEY_PASS = "password";
    private static final String CHECKLOG_URL = "http://192.168.8.104:9000/user/check";


    // XML attributes
    private EditText email;
    private EditText password;
    private TextView registerHere;
    private Button loginButton;

    // Reference for LoggedInUser class Object
    public static User user;

    //Declaring  Shared Preferences
    private String authTok;
    SharedPreferences menaPref;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "sharedPref";

    //Shared Preferences Variables
    private static final String KEY_FNAME = "fName"; // First Name for shared preference
    private static final String KEY_AUTH_TOKEN = "authToken"; // Auth token for shared preference



    static boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Stetho.initializeWithDefaults(this);

        // mapping XML elements with class attributes
        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerHere = (TextView) findViewById(R.id.messageTwo);


        // Register link Onclick
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Login Button Onclick
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginUser();
                    Log.v("gggggggggggg",authTok);
                } catch (Exception e) {
                    Log.v("loginButton.onClick", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });

        // Initialising the Shred Preferences
        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String auth = menaPref.getString(KEY_AUTH_TOKEN,"");

        if(!auth.isEmpty()){
            Intent intent = new Intent(LoginActivity.this, EventActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void loginUser() throws JSONException {

        // Creating an JSON object to pass as the request body in login request

        JSONObject registerUserRequestBody = new JSONObject();

        // initialising the JSON object
        registerUserRequestBody.put(kEY_EMAIL, email.getText().toString()); // email of the user taken from the text field
        registerUserRequestBody.put(kEY_PASS, password.getText().toString()); // password of the user taken from the text field

        // the request for login

        JsonObjectRequest registerUserRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, registerUserRequestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("Successful", response.toString());
                try {

                    // get the response to a JSON object
                    JSONObject obj = response.getJSONObject("data");

                    // referencing the obj JSON object to a new JSON object
                    JSONObject o = obj.getJSONObject("user");

                    //Retrieving the o JSON object data to few variables

                    int userId = o.getInt("userId"); // current User ID
                    String fName = o.getString("fName"); // current User First Name
                    String lName = o.getString("lName"); // current User last Name
                    String authToken = o.getString("authToken"); // current User Auth Token

                    user = new User(userId, fName, lName, authToken); // current user initialised

                    // saving values in shared preferences
                    editor = menaPref.edit();
                    editor.putString(KEY_FNAME, user.getfName());
                    editor.putString(KEY_AUTH_TOKEN, authToken);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, EventActivity.class);
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
                Toast.makeText(LoginActivity.this, "UnAuthorized", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(registerUserRequest);
    }
}
