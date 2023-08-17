package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PortfolioNotFoundException extends RuntimeException {

    private String message;
    public PortfolioNotFoundException(String message) {
        super("Portfolio \"" + message + "\" not found.");
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
