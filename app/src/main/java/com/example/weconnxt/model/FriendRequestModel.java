package com.example.weconnxt.model;

public class FriendRequestModel {

    String from;
    String to;
    String status;

    public FriendRequestModel() {
    }

    public FriendRequestModel(String from, String to, String status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
