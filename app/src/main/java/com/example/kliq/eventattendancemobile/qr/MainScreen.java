package com.example.kliq.eventattendancemobile.qr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kliq.eventattendancemobile.R;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    Button scnbtn;
    TextView result;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 100;
    private List<EventItem> eventItems;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    public static final String URL_DATA = "http://192.168.8.104:9000/con";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        result = (TextView) findViewById(R.id.result);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventItems = new ArrayList<>();
        loadEvents();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadEvents() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Events..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                EventItem event = new EventItem(
                                        o.getString("eventName"),
                                        o.getString("eventDesc"),
                                        o.getString("eventId")
                                );

                                eventItems.add(event);
                            }

                            adapter = new MyAdapter(eventItems, getApplicationContext());
                            adapter = new MyAdapter(eventItems, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(EventItem item) {
                                    Intent intent = new Intent(MainScreen.this, Scan.class);
                                    intent.putExtra("eventIds",item.getId());
                                    intent.putExtra("eventNames",item.getHead());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
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
}
