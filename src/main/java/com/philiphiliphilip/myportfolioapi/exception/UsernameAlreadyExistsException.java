package com.philiphiliphilip.myportfolioapi.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username) {
        super("User with username " + username + " failed to register. Username already taken.");
    }

}
