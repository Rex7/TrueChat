package com.example.homepage;

public class ChatRoom {
    private int chatroomId;
    String name;

    public ChatRoom(int chatroomId, String name) {
        this.chatroomId = chatroomId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }
}
