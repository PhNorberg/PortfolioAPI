package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioDeletionFailedException extends RuntimeException {

    private String name;

    public PortfolioDeletionFailedException(String name) {
        super("Portfolio with name \"" + name + "\" failed deletion.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
