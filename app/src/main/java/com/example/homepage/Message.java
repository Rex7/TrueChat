package com.example.homepage;

public class Message {
    private int userId;
    private String message;

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    private String receiverID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Message(int userId, String message, String name ,String receiverID){
        this.userId = userId;
        this.message = message;
        this.name=name;
        this.setReceiverID(receiverID);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
