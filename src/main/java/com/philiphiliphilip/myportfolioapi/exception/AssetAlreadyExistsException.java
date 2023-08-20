package com.philiphiliphilip.myportfolioapi.exception;

public class AssetAlreadyExistsException extends RuntimeException {

    private String toUser;

    public AssetAlreadyExistsException(String tickerSymbol) {
        super("Asset with ticker symbol " + tickerSymbol + " already exists in users portfolio.");
        this.toUser = "Asset with ticker symbol " + tickerSymbol + " already exists in your portfolio." +
                " If you wish to add transactions to this asset, make a POST request to " +
                "/users/{username}/portfolios/{portfolioname}/{tickersymbol}/transactions .";
    }

    public String getToUser() {
        return toUser;
    }
}
