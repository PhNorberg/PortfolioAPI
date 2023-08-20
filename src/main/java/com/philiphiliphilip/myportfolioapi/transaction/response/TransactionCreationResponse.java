package com.philiphiliphilip.myportfolioapi.transaction.response;

public class TransactionCreationResponse {

    private final String message;

    public TransactionCreationResponse(String transactionType) {
        this.message = "Transaction of type '" +transactionType + "' successfully created.";
    }

    public String getMessage() {
        return message;
    }
}
