package com.philiphiliphilip.myportfolioapi.portfolio;

import java.math.BigDecimal;

public class PortfolioDTOUsersLevel {

    private String name;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;

    public PortfolioDTOUsersLevel(String name, BigDecimal valueNow, BigDecimal profitFactor) {
        this.name = name;
        this.valueNow = valueNow;
        this.profitFactor = profitFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValueNow() {
        return valueNow;
    }

    public void setValueNow(BigDecimal valueNow) {
        this.valueNow = valueNow;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitFactor) {
        this.profitFactor = profitFactor;
    }
}
