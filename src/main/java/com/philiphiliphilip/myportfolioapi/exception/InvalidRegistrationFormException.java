package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.validation.BindingResult;

public class InvalidRegistrationFormException extends RuntimeException implements BindingResultException{

    private final BindingResult bindingResult;

    public InvalidRegistrationFormException(BindingResult bindingResult) {
        super("Invalid registration form.");
        this.bindingResult = bindingResult;
    }

    @Override
    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
