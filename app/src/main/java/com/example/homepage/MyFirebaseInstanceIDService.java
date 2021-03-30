package com.example.homepage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String deviceToken=FirebaseInstanceId.getInstance().getToken();
        Log.v("tokenDevice",deviceToken);
    }
}
