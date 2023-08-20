package com.philiphiliphilip.myportfolioapi.transaction.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TransactionCreationRequest {

    //@NotEmpty(message = "Transaction type is required.")
    @Size(min = 1, message = "Transaction type field can't be empty.")
    @Pattern(regexp = "^(buy|sell)$", message = "Transact type must be either 'buy' or 'sell'.")
    private String transactionType;
    @NotNull(message = "Quantity is required.")
    @DecimalMin(value = "0.001", message = "Quantity should be greater than 0.001.")
    private BigDecimal quantity;
    // purchaseDate to be implemented later.
    //private LocalDateTime purchaseDate;
    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.001", message = "Price should be greater than 0.001.")
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
