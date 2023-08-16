package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsernameLevel;

import java.util.List;

public class UserDTOUsernameLevel {

    private String name;
    private List<PortfolioDTOUsernameLevel> portfolios;

    public UserDTOUsernameLevel(String name, List<PortfolioDTOUsernameLevel> portfolios) {
        this.name = name;
        this.portfolios = portfolios;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PortfolioDTOUsernameLevel> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDTOUsernameLevel> portfolios) {
        this.portfolios = portfolios;
    }
}
