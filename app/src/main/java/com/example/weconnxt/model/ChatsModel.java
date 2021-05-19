package com.example.weconnxt.model;

public class ChatsModel {

    String messageId;
    String message;
    String senderId;

    public ChatsModel() {
    }

    public ChatsModel(String messageId, String message, String senderId) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
