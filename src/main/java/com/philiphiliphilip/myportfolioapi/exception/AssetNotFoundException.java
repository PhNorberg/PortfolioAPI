package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
public class AssetNotFoundException extends RuntimeException {

    private String toUser;

    public AssetNotFoundException(String tickersymbol) {
        super("Asset with tickersymbol \"" + tickersymbol +"\" not found. Doesn't belong to user's portfolio.");
        this.toUser = "No asset named " + tickersymbol + " found in your portfolio. Try again.";
    }

    public String getToUser() {
        return toUser;
    }
}
