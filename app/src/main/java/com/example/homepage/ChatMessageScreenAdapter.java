package com.example.homepage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatMessageScreenAdapter extends RecyclerView.Adapter<ChatMessageScreenAdapter.ViewHolders> {
    private ArrayList<ChatDisplay> chatDisplayArrayList;
    private Context ctx;
    private  String receiverID;

    ChatMessageScreenAdapter(Context ctx, ArrayList<ChatDisplay> chatDisplayArrayList,String receiverID) {
        this.ctx = ctx;
        this.chatDisplayArrayList = chatDisplayArrayList;
        this.receiverID=receiverID;
    }

    @NonNull
    @Override
    public ChatMessageScreenAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_design, parent, false);
        return new ViewHolders(view, ctx,chatDisplayArrayList,receiverID);

    }


    @Override
    public void onBindViewHolder(@NonNull ChatMessageScreenAdapter.ViewHolders holder, int position) {
        holder.name.setText(chatDisplayArrayList.get(position).getName());
      holder.message.setText(chatDisplayArrayList.get(position).getMessage());
      holder.timestamp.setText(chatDisplayArrayList.get(position).getTimestamp().substring(11,16));

    }


    @Override
    public int getItemCount() {
        return chatDisplayArrayList.size();
    }


   static class ViewHolders extends RecyclerView.ViewHolder {
        Context ctx;
        TextView message, timestamp,name;
        CardView layout;

        ViewHolders(@NonNull View itemView, Context ctx, ArrayList<ChatDisplay> chatDisplayArrayList, String receiverID) {
            super(itemView);
            this.ctx = ctx;
            name=itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp_message);
            layout=itemView.findViewById(R.id.cardview_message_design);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent chatMessage=new Intent(ctx,ChatGroup.class);
                   String userId= String.valueOf(chatDisplayArrayList.get(getAdapterPosition()).getUserId());
                    chatMessage.putExtra("sender",userId);
                    chatMessage.putExtra("receiver",receiverID);
                    ctx.startActivity(chatMessage);
                    Log.v("ChatAppMessageScreen","sender"+userId);
                    Log.v("ChatAppMessageScreen","receiver"+receiverID);
                }
            });
        }
    }
}




