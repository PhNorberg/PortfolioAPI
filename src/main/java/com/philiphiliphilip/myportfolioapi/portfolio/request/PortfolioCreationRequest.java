package com.philiphiliphilip.myportfolioapi.portfolio.request;

import jakarta.validation.constraints.NotEmpty;

public class PortfolioCreationRequest {

    @NotEmpty(message = "You have to enter something into portfolio name field")
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
