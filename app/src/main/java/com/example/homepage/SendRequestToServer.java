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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

public class SendRequestToServer {

    private RequestQueue myRequestQueue = VolleySingle.getInstance().getRequestQueue();
    private Context ctx;
    private SessionManage sessionManage;
    private String userInput;
     private RecyclerView recyclerView;
    private  ChatAdapter chatAdapter;
   private ArrayList<Message> chatMessages;
    private String sender,receiver;

public SendRequestToServer(){}
    public SendRequestToServer(Context ctx , SessionManage sessionManage, String userInput,
                               RecyclerView recyclerView, ChatAdapter chatAdapter, ArrayList<Message> chatMessages, String sender,String receiver) {
       this.ctx=ctx;
       this.sessionManage=sessionManage;
       this.userInput=userInput;
       this.chatMessages=chatMessages;
       this.recyclerView=recyclerView;
       this.chatAdapter=chatAdapter;
       this.sender=sender;
       this.receiver=receiver;

    }

   public void sendToServer(final int dataCount, final String chatRoomName){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rexmyapp.000webhostapp.com/not2.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             // Log.v("ResponseServer",response);
              int userid= Integer.parseInt(sessionManage.getUserDetail().get("userId"));
              Log.v("payloadData",""+chatMessages.size());
              if(dataCount==0){
                  chatMessages.add((new Message(userid,userInput,sessionManage.getUserDetail().get("name"),"")));
                  chatAdapter=new ChatAdapter(chatMessages,ctx);
                  recyclerView.setAdapter(chatAdapter);
                  chatAdapter.notifyDataSetChanged();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx.getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("message",userInput);
                data.put("sender",sender);
                data.put("receiver",receiver);
                //data.put("reciverID",sessionManage.getUserDetail().get("userId"));
                return data;

            }
        };
        myRequestQueue.add(stringRequest);


    }
}
