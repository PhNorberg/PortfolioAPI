package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class InvalidLoginFormException extends RuntimeException implements BindingResultException{

    private final BindingResult bindingResult;

    public InvalidLoginFormException(BindingResult bindingResult) {
        super("Invalid login form.");
        this.bindingResult = bindingResult;
    }

    @Override
    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
