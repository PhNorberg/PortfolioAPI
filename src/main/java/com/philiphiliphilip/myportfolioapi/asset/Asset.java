package com.philiphiliphilip.myportfolioapi.asset;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import com.philiphiliphilip.myportfolioapi.transaction.Transaction;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tickerSymbol"
)
@Entity
public class Asset {
    @Id
    @GeneratedValue
    private Integer id;
    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;
    private Integer taxRate;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE)
    private List<Transaction> transactions;

    public Asset(Integer id, String tickerSymbol, BigDecimal quantity, BigDecimal purchasePrice, Integer taxRate,
                 Portfolio portfolio, List<Transaction> transactions) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.taxRate = taxRate;
        this.portfolio = portfolio;
        this.transactions = transactions;
    }

    public Asset() {
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }


    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", taxRate=" + taxRate +
                ", portfolio=" + portfolio +
                ", transactions=" + transactions +
                '}';
    }
}
