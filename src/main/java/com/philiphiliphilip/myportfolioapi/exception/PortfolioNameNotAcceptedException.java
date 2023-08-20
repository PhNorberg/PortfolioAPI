package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioNameNotAcceptedException extends RuntimeException {

    private String toUser;

    public PortfolioNameNotAcceptedException(String name) {
        super("Portfolio creation with name \" " + name + "\" failed. Not a valid portfolio creation name.");
        this.toUser = "Invalid portfolio creation name. No special characters, spaces at the end nor beginning, " +
                "or double spaces allowed.";
    }

    public String getToUser() {
        return toUser;
    }
}
