package com.philiphiliphilip.myportfolioapi.portfolio.response;

public class PortfolioDeletionResponse {

    private String name;

    public PortfolioDeletionResponse(String name) {
        this.name = "You deleted the portfolio with the name " + name;
    }

    public String getName() {
        return name;
    }
}
