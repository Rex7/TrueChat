package com.example.homepage;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolders>  {
    ArrayList<ChatRoom> chatRooms;
    Context ctx;
    public RecyclerAdapter(ArrayList<ChatRoom> chatRooms, MainActivity mainActivity) {
        this.chatRooms=chatRooms;
        this.ctx=mainActivity;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item,viewGroup,false);
        return new ViewHolders(v, ctx);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolders viewHolders, int i) {
        viewHolders.name.setText(chatRooms.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }
    public class ViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        Context ct;

        public ViewHolders(View itemView, Context ct) {
            super(itemView);
            this.ct = ct;
            name=itemView.findViewById(R.id.chatroomName);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final int pos = getAdapterPosition();

            //MedicineActivity medicineActivity = (MedicineActivity) mContext;
            FirebaseMessaging.getInstance().subscribeToTopic(chatRooms.get(pos).getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ctx,"Success"+chatRooms.get(pos).getName()+"chatroomId"+chatRooms.get(pos).getChatroomId(), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ctx,ChatGroup.class);
                    intent.putExtra("chatRoom",chatRooms.get(pos).getName());
                    intent.putExtra("name",chatRooms.get(pos).getChatroomId());
                    intent.putExtra("chatroomId",chatRooms.get(pos).getChatroomId());
                    ctx.startActivity(intent);
                }
            });


           // bottomSheet.show(medicineActivity.getSupportFragmentManager(), bottomSheet.getTag());
          //  Toast.makeText(ct, " " + medNames.get(pos).getDrugName() + "\n Price " + medNames.get(pos).getPrice(), Toast.LENGTH_LONG).show();


        }
    }
}
