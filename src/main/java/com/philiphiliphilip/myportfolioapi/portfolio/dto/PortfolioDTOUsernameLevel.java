package com.philiphiliphilip.myportfolioapi.portfolio.dto;

import java.math.BigDecimal;

public class PortfolioDTOUsernameLevel {

    private String name;
    private BigDecimal valueNow;

    public PortfolioDTOUsernameLevel(String name, BigDecimal valueNow) {
        this.name = name;
        this.valueNow = valueNow;
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
}
