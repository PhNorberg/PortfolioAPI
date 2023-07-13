package com.philiphiliphilip.myportfolioapi.asset;

import com.philiphiliphilip.myportfolioapi.portfolio.Portfolio;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Asset {
    @Id
    @GeneratedValue
    private Integer id;
    private String tickerSymbol;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private LocalDateTime purchaseDate;
    private Integer taxRate;
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Asset(Integer id, String tickerSymbol, Integer quantity, BigDecimal purchasePrice, LocalDateTime purchaseDate, Integer taxRate,
                 Portfolio portfolio) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.taxRate = taxRate;
        this.portfolio = portfolio;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
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
                ", purchaseDate=" + purchaseDate +
                ", taxRate=" + taxRate +
                ", portfolio=" + portfolio +
                '}';
    }
}
