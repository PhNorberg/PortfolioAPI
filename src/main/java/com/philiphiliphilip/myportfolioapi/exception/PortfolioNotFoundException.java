package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
public class PortfolioNotFoundException extends RuntimeException {

    private String toUser;

    public PortfolioNotFoundException(String name) {
        super("Portfolio \"" + name + "\" not found. Might exist, but does not belong to this user.");
        this.toUser = "Portfolio \"" + name + "\" not found. Try again.";
    }

    public String getToUser() {
        return toUser;
    }
}
