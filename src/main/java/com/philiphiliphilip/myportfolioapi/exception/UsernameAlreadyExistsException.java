package com.philiphiliphilip.myportfolioapi.exception;

public class UsernameAlreadyExistsException extends RuntimeException{

    private String username;

    public UsernameAlreadyExistsException(String username) {
        super("User with username " + username + " failed to register. Username already taken.");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
