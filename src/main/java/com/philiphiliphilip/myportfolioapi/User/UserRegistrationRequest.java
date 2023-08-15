package com.philiphiliphilip.myportfolioapi.User;

import jakarta.validation.constraints.Size;
public class UserRegistrationRequest {

    @Size(min = 2, message = "Username needs to be at least 2 characters long")
    private String username;
    @Size(min = 8, message = "Password needs to be at least 8 characters long")
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
