package com.example.homepage;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAppMessageScreen extends AppCompatActivity implements VolleyCallback {
    RecyclerView chatDisplayRecycler;
    ChatMessageScreenAdapter chatMessageScreenAdapter;
    Toolbar toolbar;
    ArrayList<ChatDisplay> chatMessages;
    SessionManage sessionManage;
    String TAG = "ChatAppMessageScreen";
    String receiverID;
    ArrayList<User> userArrayList;
    ArrayList<ChatDisplay> chatDisplayArrayList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app_message_screen);
        sessionManage = new SessionManage(getApplicationContext());
        chatDisplayArrayList = new ArrayList<>();
        userArrayList = getIntent().getParcelableArrayListExtra("userList");
        receiverID = sessionManage.getUserDetail().get("userId");
        toolbar = findViewById(R.id.toolbar_chatApp);
        chatDisplayRecycler = findViewById(R.id.chatDisplay);
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
        chatMessageScreenAdapter = new ChatMessageScreenAdapter(ChatAppMessageScreen.this, chatDisplays, receiverID);
        chatDisplayRecycler.addItemDecoration(new DividerItemDecoration(chatDisplayRecycler.getContext(), DividerItemDecoration.VERTICAL));
        chatDisplayRecycler.setItemAnimator(new DefaultItemAnimator());
        chatDisplayRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatDisplayRecycler.setAdapter(chatMessageScreenAdapter);


    }

    private void setupChatDisplay() {
        RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rexmyshop.000webhostapp.com/chat/getMessageForUser.php",
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Log.v(TAG, "Length" + jsonArray.length());
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);
                                String receiverId = jobject.getString("receiverID");
                                String userIdString = jobject.getString("UserId");
                                String name;
                                if (sessionManage.getUserDetail().get("userId").equals(receiverId)) {
                                    int indexOfName = Integer.parseInt(jobject.getString("UserId"));
                                    name = userArrayList.get(indexOfName - 1).getName();
                                } else {
                                    int indexOfName = Integer.parseInt(jobject.getString("receiverID"));
                                    Log.v(TAG, " else" + receiverId + " " + jobject.getInt("UserId"));
                                    name = userArrayList.get(indexOfName - 1).getName();
                                }
                                if (Objects.requireNonNull(sessionManage.getUserDetail().get("userId")).equals(userIdString)) {
                                    int newUserId = jobject.getInt("receiverID");
                                    chatMessages.add(new ChatDisplay(name, jobject.getString("message"),
                                            jobject.getString("created"), jobject.getInt("messageId"), newUserId));
                                } else {
                                    chatMessages.add(new ChatDisplay(name, jobject.getString("message"),
                                            jobject.getString("created"), jobject.getInt("messageId"), jobject.getInt("UserId")));
                                }
                            }
                            Log.v(TAG, "ArraySize" + chatMessages.size());
                            chatMessageScreenAdapter = new ChatMessageScreenAdapter(ChatAppMessageScreen.this, chatMessages, receiverID);
                            chatDisplayRecycler.setVisibility(View.VISIBLE);
                            chatDisplayRecycler.setAdapter(chatMessageScreenAdapter);
                            chatMessageScreenAdapter.notifyDataSetChanged();
                            chatDisplayRecycler.smoothScrollToPosition(chatMessageScreenAdapter.getItemCount() - 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("receiverID", receiverID);
                return data;
            }
        };
        myRequestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callBackForData();
    }

    public void callBackForData() {

        if (chatMessageScreenAdapter != null && chatMessages.size() == 0) {
            Toast.makeText(getApplicationContext(), "first  call data" + chatMessages.size(), Toast.LENGTH_LONG).show();
            setupChatDisplay();
        } else {
            Toast.makeText(getApplicationContext(), "check for change in db " + chatMessages.get(0).getMessageID(), Toast.LENGTH_LONG).show();
            SendRequestToServer sendRequestToServer = new SendRequestToServer();
            sendRequestToServer.checkForChangeInDb(userArrayList, chatMessages.get(0).getMessageID(), receiverID, sessionManage, this::getAllChats);
            Log.v(TAG, "MessageId old" + chatMessages.get(0).getMessageID() + "new message ID" + chatDisplayArrayList.size());
        }
    }


    @Override
    public void getAllChats(ArrayList<ChatDisplay> chatDisplay) {
        this.chatDisplayArrayList = chatDisplay;
        Log.v(TAG, "MessageId old" + chatMessages.get(0).getMessageID() + "nchat display" + chatDisplay.get(0).getMessageID());
        chatMessages.clear();
        chatMessageScreenAdapter = new ChatMessageScreenAdapter(ChatAppMessageScreen.this, chatDisplay, receiverID);
        chatDisplayRecycler.setAdapter(chatMessageScreenAdapter);


    }


}



