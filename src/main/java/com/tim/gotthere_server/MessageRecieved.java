package com.tim.gotthere_server;

public class MessageRecieved {
    
    private String name;

    public MessageRecieved() {
        
    }

    public MessageRecieved(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
