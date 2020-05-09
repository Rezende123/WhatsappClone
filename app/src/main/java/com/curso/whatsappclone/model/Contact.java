package com.curso.whatsappclone.model;

public class Contact extends User {

    private String userId;

    public Contact() {

    }

    public Contact(String name, String email, String userId) {
        setName(name);
        setEmail(email);
        setUserId(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
