package com.example.kliq.eventattendancemobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static com.example.kliq.eventattendancemobile.util.SharedPrefManager mInstance;
    private static Context mctx;

    private static final String SHARED_PREF_NAME = "sharedPref";
    private static final String KEY_FNAME = "fName";
    private static final String KEY_AUTH_TOKEN = "authToken";



    private SharedPrefManager(Context mCtx) {
        this.mctx = mCtx;
    }

    public static synchronized com.example.kliq.eventattendancemobile.util.SharedPrefManager getmInstance(Context context) {
        if(mInstance == null) {
            mInstance = new com.example.kliq.eventattendancemobile.util.SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(String fName, String authToken) {

        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FNAME, fName);
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_FNAME, null) != null) {
            return true;
        }
        return false;


    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }
    public Boolean retrieveTok(){
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String auth = sharedPreferences.getString(KEY_AUTH_TOKEN,"");
        if(auth.equals("") || auth ==null){
            return true;
        }
        else{
            return false;
            }
    }
    public String retrieveTokAsString(){
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String auth = sharedPreferences.getString(KEY_AUTH_TOKEN,"");
        return auth;
    }


    public String retrieveFirstName(){
        SharedPreferences sharedPreferences = mctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(KEY_FNAME,"");
        if(firstName!=null){
            return firstName;
        }
        return null;
    }
}
