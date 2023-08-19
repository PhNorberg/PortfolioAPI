package com.philiphiliphilip.myportfolioapi.asset.response;

public class AssetCreationResponse {

    private String message;

    public AssetCreationResponse(String tickerSymbol) {
        this.message = "Asset with name " + tickerSymbol + " successfully created and added to portfolio";
    }

    public String getMessage() {
        return message;
    }
}
