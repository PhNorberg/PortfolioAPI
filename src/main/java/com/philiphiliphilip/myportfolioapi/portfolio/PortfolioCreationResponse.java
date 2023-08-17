package com.philiphiliphilip.myportfolioapi.portfolio;

public class PortfolioCreationResponse {

    private String message;

    public PortfolioCreationResponse(String message) {
        this.message = "You created a new portfolio with the name " + message;
    }

    public String getMessage() {
        return message;
    }
}
