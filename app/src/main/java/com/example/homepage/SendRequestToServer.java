package com.example.homepage;

import android.content.Context;
import android.util.Log;
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
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

public class SendRequestToServer {

    private RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
    private Context ctx;
    private SessionManage sessionManage;
    private String userInput;
    ArrayList<ChatDisplay> messageDb = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> chatMessages;
    private String sender, receiver;

    public SendRequestToServer() {
    }

    public SendRequestToServer(Context ctx, SessionManage sessionManage, String userInput,
                               RecyclerView recyclerView, ChatAdapter chatAdapter, ArrayList<Message> chatMessages, String sender, String receiver) {
        this.ctx = ctx;
        this.sessionManage = sessionManage;
        this.userInput = userInput;
        this.chatMessages = chatMessages;
        this.recyclerView = recyclerView;
        this.chatAdapter = chatAdapter;
        this.sender = sender;
        this.receiver = receiver;

    }

    public void sendToServer(final int dataCount, final String chatRoomName, final String token) {
        Log.v("ResponseServer", "token" + token);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rexmyshop.000webhostapp.com/chat/noty.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v("ResponseServer", "data" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            Log.v("ResponseServer", jsonObject.getString("sqlresult"));
                            int userid = Integer.parseInt(sessionManage.getUserDetail().get("userId"));
                            if (jsonObject.get("sqlresult").equals("successful") && jsonObject.get("fcm").equals("successful")) {
                                Log.v("ResponseServer", "called");

                                chatMessages.add((new Message(userid, userInput, sessionManage.getUserDetail().get("name"), sender)));
                                Log.v("ResponseServer", "receiverId" + chatMessages.get(chatMessages.size() - 1).getReceiverID() + "userId" + userid);
                                chatAdapter = new ChatAdapter(chatMessages, ctx);
                                recyclerView.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Toast.makeText(ctx.getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> data = new HashMap<>();
                data.put("message", userInput);
                data.put("sender", receiver);
                data.put("receiver", sender);
                data.put("token", token);
                //data.put("reciverID",sessionManage.getUserDetail().get("userId"));
                return data;

            }
        };
        myRequestQueue.add(stringRequest);


    }

    public void checkForChangeInDb(ArrayList<User> userArrayList,
                                   int lastMessageId, String receiverID, SessionManage localSession, final VolleyCallback volleyCallback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://rexmyshop.000webhostapp.com/chat/getMessageForUser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            String name;
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    String receiverId = jobject.getString("receiverID");
                                    String userIdString = jobject.getString("UserId");
                                    Log.v(this.getClass().getName(), "UserId" + userIdString + "receiver" + receiverId + "main user id from local" + localSession.getUserDetail().get("userId"));
                                    if (localSession.getUserDetail().get("userId").equals(receiverId)) {
                                        int indexOfName = Integer.parseInt(jobject.getString("UserId"));
                                        name = userArrayList.get(indexOfName - 1).getName();
                                    } else {
                                        int indexOfName = Integer.parseInt(jobject.getString("receiverID"));
                                        name = userArrayList.get(indexOfName - 1).getName();
                                    }
                                    if (Objects.requireNonNull(localSession.getUserDetail().get("userId")).equals(userIdString)) {
                                        int newUserId = jobject.getInt("receiverID");
                                        messageDb.add(new ChatDisplay(name, jobject.getString("message"),
                                                jobject.getString("created"), jobject.getInt("messageId"), newUserId));
                                    } else {
                                        messageDb.add(new ChatDisplay(name, jobject.getString("message"),
                                                jobject.getString("created"), jobject.getInt("messageId"), jobject.getInt("UserId")));
                                    }
                                }
                            }
                            volleyCallback.getAllChats(messageDb);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(this.getClass().getName(), " its a checked error" + error.getMessage());
            }
        }) {
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
