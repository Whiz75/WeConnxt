package com.example.weconnxt.model;

import java.util.Date;

public class CommentModel {

    private String message, user_id, comment_id;
    private Date timestamp;

    public CommentModel() {
        //empty constructor
    }

    public CommentModel(String message, String user_id, Date timestamp, String comment_id) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.comment_id = comment_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTime_stamp() {
        return timestamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.timestamp = time_stamp;
    }
}
