package com.philiphiliphilip.myportfolioapi.portfolio;

public class PortfolioCreationRequest {

    private String name;

    public PortfolioCreationRequest(String name) {
        this.name = name;
    }

    public PortfolioCreationRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
