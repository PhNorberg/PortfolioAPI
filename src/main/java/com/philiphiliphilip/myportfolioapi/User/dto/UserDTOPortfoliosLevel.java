package com.philiphiliphilip.myportfolioapi.User.dto;

import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOPortfolionameLevelSelf;

import java.util.List;

public class UserDTOPortfoliosLevel {

    private String username;
    private List<PortfolioDTOPortfolionameLevelSelf> portfolios;

    public UserDTOPortfoliosLevel(String username, List<PortfolioDTOPortfolionameLevelSelf> portfolios) {
        this.username = username;
        this.portfolios = portfolios;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PortfolioDTOPortfolionameLevelSelf> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDTOPortfolionameLevelSelf> portfolios) {
        this.portfolios = portfolios;
    }
}
