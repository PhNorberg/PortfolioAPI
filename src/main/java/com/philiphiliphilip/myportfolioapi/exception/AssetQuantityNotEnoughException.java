package com.philiphiliphilip.myportfolioapi.exception;

import java.math.BigDecimal;

public class AssetQuantityNotEnoughException extends RuntimeException {
    public AssetQuantityNotEnoughException(BigDecimal quantity, String uppercaseTickersymbol) {
        super("You can't sell that much " + uppercaseTickersymbol + ". Your portfolio only has " + quantity + " of them.");
    }
}
