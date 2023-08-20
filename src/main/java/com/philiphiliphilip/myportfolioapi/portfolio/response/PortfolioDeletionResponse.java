package com.philiphiliphilip.myportfolioapi.portfolio.response;

public class PortfolioDeletionResponse {

    private final String message;

    public PortfolioDeletionResponse(String portfolioName) {
        this.message = "You deleted the portfolio with the name " + portfolioName + ".";
    }

    public String getMessage() {
        return message;
    }
}
