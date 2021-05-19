package com.example.weconnxt.model;

public class PostsModel {
    private String date_posted;
    private String id;
    private String uid;
    private String url;
    private String message;

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PostsModel() {
    }

    public PostsModel(String date_posted, String id, String uid, String url, String message) {
        this.date_posted = date_posted;
        this.id = id;
        this.uid = uid;
        this.url = url;
        this.message = message;
    }






}
