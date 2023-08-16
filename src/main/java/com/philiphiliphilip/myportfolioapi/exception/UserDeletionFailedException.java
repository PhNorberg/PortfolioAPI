package com.philiphiliphilip.myportfolioapi.exception;

public class UserDeletionFailedException extends RuntimeException {

    private String username;
    public UserDeletionFailedException(String username) {
        super("Failed to delete user with username " + username + ". Couldn't be found in database.");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
