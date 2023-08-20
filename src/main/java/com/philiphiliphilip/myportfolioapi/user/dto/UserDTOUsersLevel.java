package com.philiphiliphilip.myportfolioapi.user.dto;

import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsersLevel;

import java.util.List;

public class UserDTOUsersLevel {

    private String username;
    private List<PortfolioDTOUsersLevel> portfolios;

    public UserDTOUsersLevel(){}

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
