package com.example.weconnxt.model;

public class CommentsModel {
    private String Id;
    private String Comment;
    private String SenderId;

    public CommentsModel() {
    }

    public CommentsModel(String id, String comment, String senderId) {
        Id = id;
        Comment = comment;
        SenderId = senderId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
}
