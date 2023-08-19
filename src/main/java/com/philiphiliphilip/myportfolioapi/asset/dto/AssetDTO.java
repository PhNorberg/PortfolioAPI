package com.philiphiliphilip.myportfolioapi.asset.dto;

import com.philiphiliphilip.myportfolioapi.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class AssetDTO {

    private Integer id;
    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private BigDecimal netProfitDollars;
    private BigDecimal taxRate;
    private String portfolio;
    private List<Transaction> transactions;

    public AssetDTO(Integer id, String tickerSymbol, BigDecimal quantity, BigDecimal purchasePrice, BigDecimal currentPrice,
                    BigDecimal totalInvested, BigDecimal valueNow, BigDecimal profitFactor, BigDecimal grossProfitDollars, BigDecimal netProfitDollars,
                    BigDecimal taxRate, String portfolio, List<Transaction> transactions) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.currentPrice = currentPrice;
        this.profitFactor = profitFactor;
        this.totalInvested = totalInvested;
        this.valueNow = valueNow;
        this.grossProfitDollars = grossProfitDollars;
        this.netProfitDollars = netProfitDollars;
        this.taxRate = taxRate;
        this.portfolio = portfolio;
        this.transactions = transactions;
    }

    public AssetDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitFactor) {
        this.profitFactor = profitFactor;
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

    public BigDecimal getGrossProfitDollars() {
        return grossProfitDollars;
    }

    public void setGrossProfitDollars(BigDecimal grossProfitDollars) {
        this.grossProfitDollars = grossProfitDollars;
    }

    public BigDecimal getNetProfitDollars() {
        return netProfitDollars;
    }

    public void setNetProfitDollars(BigDecimal netProfitDollars) {
        this.netProfitDollars = netProfitDollars;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
