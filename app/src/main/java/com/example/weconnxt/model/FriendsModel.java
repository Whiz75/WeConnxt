package com.example.weconnxt.model;

public class FriendsModel {

    String friendUsername;
    String status;

    public FriendsModel() {
    }

    public FriendsModel(String friendUsername, String status) {
        this.friendUsername = friendUsername;
        this.status = status;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
