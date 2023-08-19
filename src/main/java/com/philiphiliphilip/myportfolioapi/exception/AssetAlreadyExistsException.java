package com.philiphiliphilip.myportfolioapi.exception;

public class AssetAlreadyExistsException extends RuntimeException {


    public AssetAlreadyExistsException(String tickerSymbol) {
        super("Asset with ticker symbol " + tickerSymbol + " already exists.");
    }

}
