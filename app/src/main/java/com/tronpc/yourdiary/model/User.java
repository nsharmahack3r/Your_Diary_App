package com.tronpc.yourdiary.model;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private String userId;
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public User(String name, String email, String userId, String uri) {
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.uri = uri;
    }

    public User() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
