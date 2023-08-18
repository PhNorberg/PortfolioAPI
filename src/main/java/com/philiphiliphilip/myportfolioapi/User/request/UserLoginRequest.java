package com.philiphiliphilip.myportfolioapi.User.request;

import jakarta.validation.constraints.NotEmpty;

public class UserLoginRequest {

    @NotEmpty(message = "You have to enter something into username field")
    private String username;
    @NotEmpty(message = "You have to enter something into password field")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
