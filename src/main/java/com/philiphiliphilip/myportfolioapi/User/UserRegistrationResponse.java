package com.philiphiliphilip.myportfolioapi.User;

public class UserRegistrationResponse {

    private String message;

    public UserRegistrationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
