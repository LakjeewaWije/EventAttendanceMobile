package com.example.kliq.eventattendancemobile.data.model;

public class User {

    private String fName;



    private String AuthTok;

    public User( String fName, String authTok) {
        this.fName = fName;
        AuthTok = authTok;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getAuthTok() {
        return AuthTok;
    }

    public void setAuthTok(String authTok) {
        AuthTok = authTok;
    }
}
