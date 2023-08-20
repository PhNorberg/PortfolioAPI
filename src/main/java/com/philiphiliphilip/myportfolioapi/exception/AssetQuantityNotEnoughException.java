package com.philiphiliphilip.myportfolioapi.exception;

import java.math.BigDecimal;
public class AssetQuantityNotEnoughException extends RuntimeException {

    private String toUser;

    public AssetQuantityNotEnoughException(BigDecimal quantity, String uppercaseTickersymbol) {
        super("Sell transaction failed. User does not have that many shares of " + uppercaseTickersymbol + ".");
        this.toUser = "You can't sell that much " + uppercaseTickersymbol + ". Your portfolio has " +
                "" + quantity + " of them.";
    }

    public String getToUser() {
        return toUser;
    }
}
