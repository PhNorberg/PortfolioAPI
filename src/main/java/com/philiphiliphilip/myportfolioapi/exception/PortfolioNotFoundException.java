package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PortfolioNotFoundException extends RuntimeException {

    public PortfolioNotFoundException(String name) {
        super("Portfolio \"" + name + "\" not found.");

    }


}
