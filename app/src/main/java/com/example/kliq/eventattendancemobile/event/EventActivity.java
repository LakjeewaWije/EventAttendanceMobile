package com.example.kliq.eventattendancemobile.event;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.example.kliq.eventattendancemobile.data.model.Event;
import com.example.kliq.eventattendancemobile.data.service.EventService;
import com.example.kliq.eventattendancemobile.data.service.UserService;
import com.example.kliq.eventattendancemobile.event.RequestHandler.LoadEventsOnResponse;
import com.example.kliq.eventattendancemobile.scanner.AttendanceScanActivity;
import com.example.kliq.eventattendancemobile.user.LoginActivity;
import com.example.kliq.eventattendancemobile.user.RequestHandler.LogoutOnResponse;
import com.example.kliq.eventattendancemobile.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements LogoutOnResponse,LoadEventsOnResponse{


    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 100;
    private List<Event> eventItems;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;


    // Variables to values Stored in Shred Preferences
    private String name; // Current Users Name
    private String authTok; // Current Users Auth Token



    public static final String URL_DATA = "http://192.168.8.101:9000/mob"; // URL for loading events route
    private  String URL_LOGOUT=""; // URL for log out User route
    // Declaring Shared Preferences
    private static final String SHARED_PREF_NAME = "sharedPref";

    //Intialising the Shred Preference Key Name

    private static final String KEY_FNAME = "fName"; // First Name
    private static final String KEY_AUTH_TOKEN = "authToken"; // Auth Token

    SharedPrefManager sharedpref;
    //Declare a private  RequestQueue variable
    private RequestQueue requestQueue;
    private static EventActivity mInstance;
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
        try {
            loadEvents();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Getting shred preferences for refered variable from the referenced key


        sharedpref= SharedPrefManager.getmInstance(getApplicationContext());
        String firstName =  sharedpref.retrieveFirstName();
        getSupportActionBar().setTitle("Welcome "+ firstName);

        /*menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = menaPref.edit();
        name = menaPref.getString(KEY_FNAME, "");*/ // getting name


        /*getSupportActionBar().setTitle("Welcome "+name); // setting the retrived name on the Acitivity Bar
        authTok = menaPref.getString(KEY_AUTH_TOKEN,"");*/ //retireving the Auth Token to logout the current user when needed

        URL_LOGOUT ="http://192.168.8.101:9000/user/log"; // Intialisng the Loggin OUt URL with the current users Auth Token


        mInstance= EventActivity.this;
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
                try {
                    logout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*sharedpref = SharedPrefManager.getmInstance(getApplicationContext());

                if(sharedpref.logout()) {

                }*/
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadEvents() throws JSONException {
        // Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events..");
        progressDialog.show();

        EventService eventService = new EventService();
        eventService.loadEvents(this);
        progressDialog.dismiss();

    }
    private void logout() throws JSONException{
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loggin Out...");
        //  progressDialog.show();
        UserService userService = new UserService();
        userService.logoutUser(this);
    }

    @Override
    public void onLogoutSucess(JSONObject response) {
        sharedpref = SharedPrefManager.getmInstance(getApplicationContext());
        sharedpref.logout();
        Intent intent = new Intent(EventActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(EventActivity.this, "Loggin Out", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLogoutError(VolleyError error) {
        Log.v("onErrorResponse", error.getLocalizedMessage());
        Toast.makeText(EventActivity.this, "UnAuthorized", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadEventsSucess(String response) {
        try {
            // creating a JSON object to wrap the response
            JSONObject jsonObject = new JSONObject(response);
//            Toast.makeText(EventActivity.this, " Loaded Events", Toast.LENGTH_SHORT).show();
            // adding the oject to ajson array
            JSONArray array = jsonObject.getJSONArray("data");


            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i); //initialising the JSON object using another JSON object
                // Initialising the EventItem class Object with event name and description and ID
                Event event = new Event(
                        o.getString("eventName"),
                        o.getString("eventDesc"),
                        o.getString("eventId")
                );

                eventItems.add(event);
            }

            //Initialising the Adapter
            adapter = new EventAdapter(eventItems, getApplicationContext());
            // Set Onclik to the adapter
            adapter = new EventAdapter(eventItems, new EventAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Event item) {
                    Intent intent = new Intent(EventActivity.this, AttendanceScanActivity.class); // creating an intent to the AttendanceScanActivity Page
                    intent.putExtra("eventIds",item.getId()); // passing the eventId to next Intent
                    startActivity(intent); //start the Intent
                }
            });
            recyclerView.setAdapter(adapter); //set the Adapter to the Recycler View
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadEventsError(VolleyError error) {
//         Toast.makeText(EventActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
        Toast.makeText(EventActivity.this, "Can't Load Events", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(EventActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
    }
}
