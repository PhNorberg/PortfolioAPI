package com.philiphiliphilip.myportfolioapi.transaction;

public class TransactionCreationResponse {

    private String message;

    public TransactionCreationResponse(String transactionType) {
        this.message = "Transaction of type '" +transactionType + "' successfully created.";
    }

    public String getMessage() {
        return message;
    }
}
