package com.example.kliq.eventattendancemobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL_DATA = "http://192.168.8.101:9000/con";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<EventItem> eventItems;
    private String name;
    private String authTok;
    private long id;
    SharedPreferences menaPref;
    SharedPreferences.Editor editor;

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_USERID = "userId";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String URL_LOGOUT="http://192.168.8.101:9000/user/8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventItems = new ArrayList<>();
        loadEvents();
        menaPref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = menaPref.edit();
        name = menaPref.getString(KEY_FNAME, "");
        getSupportActionBar().setTitle("Welcome "+name);
        authTok = menaPref.getString(KEY_AUTH_TOKEN,"");
        id = menaPref.getInt(KEY_USERID,0);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show();
                logout();
                editor = menaPref.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadEvents(){
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

    private void logout(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events..");
        progressDialog.show();


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
}