package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOPortfoliosLevel;

import java.util.List;

public class UserDTOPortfoliosLevel {

    private String username;
    private List<PortfolioDTOPortfoliosLevel> portfolios;

    public UserDTOPortfoliosLevel(String username, List<PortfolioDTOPortfoliosLevel> portfolios) {
        this.username = username;
        this.portfolios = portfolios;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PortfolioDTOPortfoliosLevel> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDTOPortfoliosLevel> portfolios) {
        this.portfolios = portfolios;
    }
}
