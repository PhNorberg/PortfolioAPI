package com.philiphiliphilip.myportfolioapi.user.dto;

import com.philiphiliphilip.myportfolioapi.portfolio.dto.PortfolioDTOUsernameLevel;

import java.util.List;

public class UserDTOUsernameLevel {

    private String username;
    private List<PortfolioDTOUsernameLevel> portfolios;

    public UserDTOUsernameLevel(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PortfolioDTOUsernameLevel> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDTOUsernameLevel> portfolios) {
        this.portfolios = portfolios;
    }
}
