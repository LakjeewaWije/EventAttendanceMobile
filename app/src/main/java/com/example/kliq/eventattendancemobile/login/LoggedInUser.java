package com.example.kliq.eventattendancemobile.login;

/**
 * Created by ajmal on 7/11/18.
 */

public class LoggedInUser {
    private int userId;

    private String fName;

    private String lName;

    private String AuthTok;

    public LoggedInUser(int userId, String fName, String lName, String authTok) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        AuthTok = authTok;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getAuthTok() {
        return AuthTok;
    }

    public void setAuthTok(String authTok) {
        AuthTok = authTok;
    }
}
