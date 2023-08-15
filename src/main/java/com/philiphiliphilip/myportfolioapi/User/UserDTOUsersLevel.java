package com.philiphiliphilip.myportfolioapi.User;

import com.philiphiliphilip.myportfolioapi.portfolio.PortfolioDTOUsersLevel;

import java.util.List;

public class UserDTOUsersLevel {

    private String username;
    private List<PortfolioDTOUsersLevel> portfolios;

    public UserDTOUsersLevel(String username, List<PortfolioDTOUsersLevel> portfolios) {
        this.username = username;
        this.portfolios = portfolios;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PortfolioDTOUsersLevel> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDTOUsersLevel> portfolios) {
        this.portfolios = portfolios;
    }
}
