package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioNameAlreadyExistsException extends RuntimeException {

    private String name;
    public PortfolioNameAlreadyExistsException(String name) {
        super("Portfolio creation failed. Portfolio with name \"" + name + "\" already exists for this user.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
