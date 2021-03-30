package com.example.homepage;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAppMessageScreen extends AppCompatActivity {
    RecyclerView chatDisplay;
    ChatMessageScreenAdapter chatMessageScreenAdapter;
    Toolbar toolbar;
    ArrayList<ChatDisplay> chatMessages;
    SessionManage sessionManage;
    String TAG = "ChatAppMessageScreen";
    String receiverID;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app_message_screen);
        sessionManage = new SessionManage(getApplicationContext());
        receiverID = sessionManage.getUserDetail().get("userId");
        toolbar = findViewById(R.id.toolbar_chatApp);
        chatDisplay = findViewById(R.id.chatDisplay);
        chatMessages = new ArrayList<>();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TruChat");
        } else {
            toolbar.setTitle("TruChat");
            toolbar.setTitleTextColor(getColor(R.color.white));
            setSupportActionBar(toolbar);
        }
        ArrayList<ChatDisplay> chatDisplays = new ArrayList<>();
        //Collections.sort(chatDisplays);
        chatMessageScreenAdapter = new ChatMessageScreenAdapter(getApplicationContext(), chatDisplays,receiverID);
        chatDisplay.addItemDecoration(new DividerItemDecoration(chatDisplay.getContext(), DividerItemDecoration.VERTICAL));
        chatDisplay.setItemAnimator(new DefaultItemAnimator());
        chatDisplay.setLayoutManager(new LinearLayoutManager(this));
        chatDisplay.setAdapter(chatMessageScreenAdapter);
        setupChatDisplay();

    }

    private void setupChatDisplay() {
        RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rexmyapp.000webhostapp.com/getMessageForUser.php",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.v(TAG, "Length" + jsonArray.length());
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);
                                chatMessages.add(new ChatDisplay(jobject.getString("name"), jobject.getString("message"),
                                        jobject.getString("created"), jobject.getInt("messageId"), jobject.getInt("UserId")));
                            }
                            Log.v(TAG, "ArraySize" + chatMessages.size());
                            chatMessageScreenAdapter = new ChatMessageScreenAdapter(ChatAppMessageScreen.this, chatMessages,receiverID);
                            chatDisplay.setVisibility(View.VISIBLE);
                            chatDisplay.setAdapter(chatMessageScreenAdapter);
                            chatMessageScreenAdapter.notifyDataSetChanged();
                            chatDisplay.smoothScrollToPosition(chatMessageScreenAdapter.getItemCount() - 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("receiverID", receiverID);
                return data;
            }
        };
        myRequestQueue.add(stringRequest);
    }

}



