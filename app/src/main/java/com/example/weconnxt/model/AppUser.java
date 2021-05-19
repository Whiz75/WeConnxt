package com.example.weconnxt.model;

public class AppUser {
    public String name;
    public String lastName;
    public String email;
    public String id;

    public AppUser(String name, String email, String id, String lastName) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.id = id;

    }

    public AppUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
