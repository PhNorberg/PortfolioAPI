package com.philiphiliphilip.myportfolioapi.exception;

public class AssetDeletionFailedException extends RuntimeException {
    public AssetDeletionFailedException(String tickersymbol) {
        super("Asset with tickersymbol \"" + tickersymbol + "\" was called to delete but failed.");
    }
}
