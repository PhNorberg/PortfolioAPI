package com.philiphiliphilip.myportfolioapi.portfolio.response;

public class PortfolioCreationResponse {

    private String name;

    public PortfolioCreationResponse(String name) {
        this.name = "You created a new portfolio with the name " + name;
    }

    public String getName() {
        return name;
    }
}
