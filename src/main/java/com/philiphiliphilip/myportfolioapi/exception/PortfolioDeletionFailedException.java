package com.philiphiliphilip.myportfolioapi.exception;

public class PortfolioDeletionFailedException extends RuntimeException {

    private String toUser;

    public PortfolioDeletionFailedException(String name) {
        super("Portfolio with name \"" + name + "\" failed deletion. Might be some race condition, because" +
                " this portfolio belongs to the user.");
        this.toUser = "Portfolio deletion failed. Try again.";
    }

    public String getToUser() {
        return toUser;
    }
}
