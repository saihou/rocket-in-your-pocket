package com.propulsion.rocketjobs;

public class ChatItem {
    private String from;
    private String message;

    public ChatItem(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
    public String getSender() {
        return this.from;
    }
}