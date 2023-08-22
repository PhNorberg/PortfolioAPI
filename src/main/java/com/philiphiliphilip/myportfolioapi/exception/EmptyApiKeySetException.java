package com.philiphiliphilip.myportfolioapi.exception;

public class EmptyApiKeySetException extends RuntimeException{

    private String toUser;

    public EmptyApiKeySetException() {
        super("TwelveData API key fetching from AWS Secrets Manager failed. Look through the readApiKey() method " +
                "and your Secrets Manager on AWS.");
        this.toUser = "Fetching real-time stock/crypto data failed. Try again or contact the developer";
    }

    public String getToUser() {
        return toUser;
    }
}
