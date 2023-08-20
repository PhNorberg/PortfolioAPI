package com.philiphiliphilip.myportfolioapi.exception;

public class UserDeletionFailedException extends RuntimeException {

    private String toUser;

    public UserDeletionFailedException(String username) {
        super("Failed to delete user with username " + username + ". Couldn't be found in database. Might be some race " +
                "condition.");
        this.toUser = "Failed to delete user. Try again.";
    }

    public String getToUser() {
        return toUser;
    }
}
