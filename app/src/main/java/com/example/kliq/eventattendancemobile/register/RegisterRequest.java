package com.example.kliq.eventattendancemobile.register;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ajmal on 7/5/18.
 */

public class RegisterRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "";
    private Map<String, String> params;

    public RegisterRequest(String fName, String lName, String email, String pass, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("fName", fName);
        params.put("lName", lName + "");
        params.put("email", email);
        params.put("pass", pass);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
