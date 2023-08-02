package com.philiphiliphilip.myportfolioapi.asset;

import com.philiphiliphilip.myportfolioapi.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class AssetDTO {

    private Integer id;
    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private BigDecimal profitPercentage;
    private Integer taxRate;
    private String portfolio;
    private List<Transaction> transactions;

    public AssetDTO(Integer id, String tickerSymbol, BigDecimal quantity, BigDecimal purchasePrice, BigDecimal profitPercentage, Integer taxRate, String portfolio, List<Transaction> transactions) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.profitPercentage = profitPercentage;
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

    public BigDecimal getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(BigDecimal profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
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
