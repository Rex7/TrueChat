package com.example.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatGroup extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    ArrayList<Message> chatMessages = new ArrayList<>();
    String chatRoom;
    Button send;
    EditText enterMessage;
    SessionManage sessionManage;
    BroadcastReceiver broadcastReceiver;
    Toolbar toolbar;
    int dataCount;
    String chatRoomName;
    String sender,receiver;
    TextView title_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        recyclerView = findViewById(R.id.recycleChat);
        send = findViewById(R.id.send);
        enterMessage = findViewById(R.id.enterMessage);
        toolbar = findViewById(R.id.toolbarChatGroup);
        title_text=findViewById(R.id.title_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sender = getIntent().getExtras().getString("sender");
        receiver=getIntent().getExtras().getString("receiver");
        String chatUserName=getIntent().getExtras().getString("name");
        sessionManage = new SessionManage(getApplicationContext());
        send.setOnClickListener(this);
        title_text.setText(chatUserName);


        //setting toolbar
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //setting a empty adapter
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rexmyapp.000webhostapp.com/getChatMessageByUserId.php",
                new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                String message;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.v("ChatGroup", "Length" + jsonArray.length());
                    dataCount = jsonArray.length();
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jobject = jsonArray.getJSONObject(i);
                            chatMessages.add(new Message(jobject.getInt("UserId"),
                                    jobject.getString("message"), jobject.getString("name"),jobject.getString("receiverID")));
                        }
                        Log.v("HomePageDemo", "ArraySize" + chatMessages.size());


                        chatAdapter = new ChatAdapter(chatMessages, ChatGroup.this);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("sender", sender);
                data.put("receiver",receiver);


                return data;

            }
        };
        myRequestQueue.add(stringRequest);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handlePush(intent);
            }
        };
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("pushNotification"));


    }

    @Override
    public void onClick(View v) {
        Log.v("payloadData", "chat messages" + chatMessages.size());
        SendRequestToServer sendRequestToServer = new SendRequestToServer(getApplicationContext(), sessionManage,
                enterMessage.getText().toString(), recyclerView, chatAdapter, chatMessages, sender,receiver);
        sendRequestToServer.sendToServer(dataCount, chatRoomName);
        enterMessage.setText("");


    }

    private void handlePush(Intent intent) {
        String message = intent.getExtras().getString("message");
        String title = intent.getExtras().getString("title");
        String name = intent.getExtras().getString("name");
        if (dataCount != 0) {
            int userId = Integer.parseInt(title);
            chatMessages.add(new Message(userId, message, name,""));
            chatAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            Toast.makeText(getApplicationContext(), "message" + message, Toast.LENGTH_LONG).show();
        }


    }
}
