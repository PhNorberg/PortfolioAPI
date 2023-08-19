package com.philiphiliphilip.myportfolioapi.asset.dto;

import java.math.BigDecimal;

public class AssetDTOPortfolionameLevelOther {

    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;


    public AssetDTOPortfolionameLevelOther() {
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
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

    public BigDecimal getGrossProfitDollars() {
        return grossProfitDollars;
    }

    public void setGrossProfitDollars(BigDecimal grossProfitDollars) {
        this.grossProfitDollars = grossProfitDollars;
    }
}
