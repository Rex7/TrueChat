package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginPage extends AppCompatActivity {
    RequestQueue requestQueue;
    Button login;
    EditText username, password;
    String TAG = "LoginPage";
    SessionManage sessionManage;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        toolbar=findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password_login);
        sessionManage = new SessionManage(getApplicationContext());
        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            requestQueue = VolleySingle.getInstance().getRequestQueue();
            ArrayList<User> userArrayList = new ArrayList<>();
            if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                Log.v(TAG, "not Empty");
                Log.v("PayloadData","stringRequest");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rexmyshop.000webhostapp.com/chat/login.php",
                        response -> {
                            String message = "";
                            JSONObject jsonObject = null;
                            try {
                                Log.v("PayloadData", "stringRequest");
                                jsonObject = new JSONObject(response);
                                message = jsonObject.getString("status");
                                JSONArray userList = jsonObject.getJSONArray("data");

                                for (int i = 0; i < userList.length(); i++) {
                                    userArrayList.add(new User(userList.getJSONObject(i).getString("UserId"), userList.getJSONObject(i).getString("name")));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                Log.v(TAG, response);
                                if (message.equals("successful")) {
                                    sessionManage.createSession(password.getText().toString(), username.getText().toString(),
                                            jsonObject.getString("userId"));

                                    Intent intent = new Intent(getApplicationContext(), ChatAppMessageScreen.class);
                                    Bundle args = new Bundle();
                                    intent.putParcelableArrayListExtra("userList", userArrayList);
                                    Log.v(TAG, "length of userArrayList" + userArrayList.size());
                                    startActivity(intent);


                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Sorry UserName is not registered" + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.v(TAG, e.getMessage());
                            }


                        }, error -> Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("name", username.getText().toString().trim());
                        data.put("password", password.getText().toString().trim());


                        return data;

                    }
                };
                requestQueue.add(stringRequest);

            }

        });
    }

}