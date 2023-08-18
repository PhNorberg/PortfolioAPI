package com.philiphiliphilip.myportfolioapi.exception;

public class AssetAlreadyExistsException extends RuntimeException {

    private String tickerSymbol;

    public AssetAlreadyExistsException(String tickerSymbol) {
        super("Asset with ticker symbol " + tickerSymbol + " already exists.");
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }
}
