package com.philiphiliphilip.myportfolioapi.transaction.model;

import com.philiphiliphilip.myportfolioapi.asset.model.Asset;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Integer id;
    private String transactionType;
    private BigDecimal quantity;
    private LocalDateTime purchaseDate;
    private BigDecimal purchasePrice;
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    public Transaction(String transactionType, BigDecimal quantity, BigDecimal purchasePrice) {
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    public Transaction(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionType='" + transactionType + '\'' +
                ", quantity=" + quantity +
                ", purchaseDate=" + purchaseDate +
                ", purchasePrice=" + purchasePrice +
                ", asset=" + asset +
                '}';
    }
}
