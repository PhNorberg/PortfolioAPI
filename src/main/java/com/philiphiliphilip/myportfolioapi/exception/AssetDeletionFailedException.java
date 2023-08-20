package com.philiphiliphilip.myportfolioapi.exception;

public class AssetDeletionFailedException extends RuntimeException {

    private String toUser;

    public AssetDeletionFailedException(String tickersymbol) {
        super("Failed to delete asset with tickersymbol \"" + tickersymbol + "\". Perhaps " +
                "some race candition.");
        this.toUser = "Failed to delete asset with tickersymbol \"" + tickersymbol + "\".";
    }

    public String getToUser() {
        return toUser;
    }
}
