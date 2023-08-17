package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioNameNotAcceptedException extends RuntimeException {

    private String name;
    public PortfolioNameNotAcceptedException(String name) {
        super("Portfolio name \" " + name + "\" is not a valid portfolio creation name.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
