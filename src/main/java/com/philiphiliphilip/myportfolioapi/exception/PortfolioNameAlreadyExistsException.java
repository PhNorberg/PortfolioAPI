package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioNameAlreadyExistsException extends RuntimeException {

    private String toUser;

    public PortfolioNameAlreadyExistsException(String name) {
        super("Portfolio creation failed. Portfolio with name \"" + name + "\" already exists for this user.");
        this.toUser = "Portfolio creation failed. You already have a portfolio with the name \"" + name +"\"";
    }

    public String getToUser() {
        return toUser;
    }
}
