package com.example.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManage {
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;
    private static final String app = "Session";
    private static final String IS_LOGIN = "is_login";
    private static final String USERNAME = "name";
    private static final String PASSWORD = "password";
    private static final String EMAILID = "emailId";
    private static final String PHONENO = "phoneNo";
    private static final String USER_ID = "userId";


    public SessionManage(Context context) {
        this.context = context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(app, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String password, String username, String userId) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);

        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public void checkLogin() {
        if (!this.isLogedIn()) {
            context.startActivity(new Intent(context, LoginPage.class));
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(PHONENO, sharedPreferences.getString(PHONENO, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(EMAILID, sharedPreferences.getString(EMAILID, null));
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        return user;

    }

    public void Logout() {
        editor.clear();
        editor.apply();
        Intent myIntent = new Intent(context, LoginPage.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

    public boolean isLogedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

}