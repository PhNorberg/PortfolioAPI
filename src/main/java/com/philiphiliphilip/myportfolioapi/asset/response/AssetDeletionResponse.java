package com.philiphiliphilip.myportfolioapi.asset.response;

public class AssetDeletionResponse {

    private String message;

    public AssetDeletionResponse(String tickerSymbol) {
        this.message = "Asset with name " + tickerSymbol + " succesfully deleted from portfolio.";
    }

    public String getMessage() {
        return message;
    }
}
