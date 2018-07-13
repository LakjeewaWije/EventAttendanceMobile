package com.example.kliq.eventattendancemobile.qr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.kliq.eventattendancemobile.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreen extends AppCompatActivity {


    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 100;
    private List<EventItem> eventItems;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    SharedPreferences menaPref;
    SharedPreferences.Editor editor;

    // Variables to values Stored in Shred Preferences
    private String name; // Current Users Name
    private String authTok; // Current Users Auth Token



    public static final String URL_DATA = "http://192.168.8.103:9000/con"; // URL for loading events route
    private  String URL_LOGOUT=""; // URL for log out User route
    // Declaring Shared Preferences
    private static final String SHARED_PREF_NAME = "sharedPref";

    //Intialising the Shred Preference Key Name

    private static final String KEY_FNAME = "fName"; // First Name
    private static final String KEY_AUTH_TOKEN = "authToken"; // Auth Token

    //Declare a private  RequestQueue variable
    private  RequestQueue requestQueue;
    private static MainScreen mInstance;
    static boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Mapping RecyclerView from the XML
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // ReDealring the Event Items List from a new Array List
        eventItems = new ArrayList<>();

        // Calling Load Event Method in order to load events
        loadEvents();

        // Getting shred preferences for refered variable from the referenced key
        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = menaPref.edit();
        name = menaPref.getString(KEY_FNAME, ""); // getting name
        getSupportActionBar().setTitle("Welcome "+name); // setting the retrived name on the Acitivity Bar
        authTok = menaPref.getString(KEY_AUTH_TOKEN,""); //retireving the Auth Token to logout the current user when needed

        URL_LOGOUT ="http://192.168.8.103:9000/user/log"; // Intialisng the Loggin OUt URL with the current users Auth Token


        mInstance=MainScreen.this;
        // Validating Camera Permission for the Visio API

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
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



    public static synchronized MainScreen getInstance()
    {
        return mInstance;
    }
    /*
Create a getRequestQueue() method to return the instance of
RequestQueue.This kind of implementation ensures that
the variable is instatiated only once and the same
instance is used throughout the application
 */
    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
            requestQueue= Volley.newRequestQueue(getApplicationContext());

        return requestQueue;
    }
    /*
         public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request,String tag)
    {
        request.setTag(tag);
        getRequestQueue().add(request);

    }
    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    /**
     * Method to directing to logout user method in Activity Bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show();
                logout();

               // menaPref = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
               // menaPref.edit().remove("authToken").commit();

                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();



                Intent intent = new Intent(MainScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load Event method
     */
    private void loadEvents() {

        // Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            // creating a JSON object to wrap the response

                            JSONObject jsonObject = new JSONObject(s);

                            // adding the oject to ajson array
                            JSONArray array = jsonObject.getJSONArray("data");


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i); //initialising the JSON object using another JSON object
                                // Initialising the EventItem class Object with event name and description and ID
                                EventItem event = new EventItem(
                                        o.getString("eventName"),
                                        o.getString("eventDesc"),
                                        o.getString("eventId")
                                );

                                eventItems.add(event);
                            }

                            //Initialising the Adapter
                            adapter = new MyAdapter(eventItems, getApplicationContext());
                            // Set Onclik to the adapter
                            adapter = new MyAdapter(eventItems, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(EventItem item) {
                                    Intent intent = new Intent(MainScreen.this, Scan.class); // creating an intent to the Scan Page
                                    intent.putExtra("eventIds",item.getId()); // passing the eventId to next Intent
                                    startActivity(intent); //start the Intent
                                }
                            });
                            recyclerView.setAdapter(adapter); //set the Adapter to the Recycler View
                            progressDialog.dismiss(); // Dismiss the progress Dialog
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

        //Initialising the request Que
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Logout method to Remove the current User
     */
    private void logout() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loggin Out...");
        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                URL_LOGOUT, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

        {

            /**
             * Passing some request headers*
             */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-AUTH-TOKEN", authTok);
                return headers;
            }
        };
    }



    private void authVal(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                URL_LOGOUT, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

        {

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-AUTH-TOKEN", authTok);
                return headers;
            }
        };
// Adding the request to the queue along with a unique string tag
        MainScreen.getInstance().addToRequestQueue(jsonObjReq,"headerRequest");
    }
}
