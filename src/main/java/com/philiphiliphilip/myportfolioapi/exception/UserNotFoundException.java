package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends RuntimeException {

    private String username;

    public UserNotFoundException(String username) {
        super("User with username " + username + " does not exist.");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
