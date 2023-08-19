package com.philiphiliphilip.myportfolioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AssetNotFoundException extends RuntimeException {

    public AssetNotFoundException(String tickersymbol) {
        super("Asset with tickersymbol \"" + tickersymbol +"\" not found.");
    }
}
