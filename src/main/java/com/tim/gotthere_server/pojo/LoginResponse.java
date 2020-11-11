package com.tim.gotthere_server.pojo;

public class LoginResponse {
    
    private boolean accepted;

    public LoginResponse(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
