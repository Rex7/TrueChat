package com.example.homepage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolders> {
    private final ArrayList<Message> chatMessages;
    private final int SELF = 100;
    private Context ctx;
    SessionManage sessionManage;

    public ChatAdapter(ArrayList<Message> chatMessages, Context mainActivity) {
        this.chatMessages = chatMessages;
        this.ctx = mainActivity;
        sessionManage = new SessionManage(ctx.getApplicationContext());


    }

    @Override
    public int getItemViewType(int position) {
        if (Integer.parseInt(chatMessages.get(position).getReceiverID()) == Integer.parseInt(sessionManage.getUserDetail().get("userId"))) {
            return SELF;
        } else {
            return position;
        }

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        // view type is to identify where to render the chat message
        // left or right
        if (i == SELF) {
            // self message
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_self, viewGroup, false);
        } else {
            // others message
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.other_chat, viewGroup, false);
        }


        return new ViewHolders(itemView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolders viewHolders, int i) {
        Message message = chatMessages.get(i);

        viewHolders.message.setText(message.getMessage());
        viewHolders.timestamp.setText("- " + message.getName());


    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ViewHolders extends RecyclerView.ViewHolder {
        TextView message, timestamp;

        public ViewHolders(View view) {
            super(view);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}



