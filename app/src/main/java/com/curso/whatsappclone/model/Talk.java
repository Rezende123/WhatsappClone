package com.curso.whatsappclone.model;

public class Talk extends Message {

    private String name;

    public Talk() {
    }

    public Talk(String userId, String message, String name) {
        super(userId, message);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
