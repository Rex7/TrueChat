package com.example.homepage;

public class ChatDisplay implements Comparable<ChatDisplay> {
    private String message;


    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    private int messageID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String timestamp;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    private int UserId;

    public ChatDisplay(String name,String message, String timestamp,int messageID,int UserId) {
        this.setMessage(message);
        this.setTimestamp(timestamp);
        this.setName(name);
        this.setMessageID(messageID);
        this.setUserId(UserId);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(ChatDisplay s) {
      return timestamp.compareTo(s.getTimestamp());
    }
}
