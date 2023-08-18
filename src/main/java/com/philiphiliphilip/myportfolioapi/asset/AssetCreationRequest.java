package com.philiphiliphilip.myportfolioapi.asset;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AssetCreationRequest {

    @NotEmpty(message = "Ticker symbol is required.")
    private String tickerSymbol;
    @NotEmpty(message = "Asset type is required.")
    @Pattern(regexp = "^(crypto|stock)$", message = "Asset type must be either 'crypto' or 'stock'.")
    private String assetType;
    @NotNull(message = "Tax rate is required.")
    @Digits(integer = 2, fraction = 1, message = "Tax rate should be in the format 'X.X' or 'XX.X'.")
    @DecimalMin(value = "0.0", message = "Tax rate should be non-negative.")
    private BigDecimal taxRate;


    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
