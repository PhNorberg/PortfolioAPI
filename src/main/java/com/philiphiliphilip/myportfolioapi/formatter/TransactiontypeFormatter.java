package com.philiphiliphilip.myportfolioapi.formatter;

import org.springframework.stereotype.Service;

@Service
public class TransactiontypeFormatter implements NameFormatter {

    @Override
    public String format(String transactionType) {
        return transactionType.toLowerCase();
    }
}
