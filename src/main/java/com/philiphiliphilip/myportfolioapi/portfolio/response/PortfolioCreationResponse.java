package com.philiphiliphilip.myportfolioapi.portfolio.response;

public class PortfolioCreationResponse {

    private final String message;

    public PortfolioCreationResponse(String portfolioName) {
        this.message = "You created a new portfolio with the name " + portfolioName + ".";
    }

    public String getMessage() {
        return message;
    }
}
