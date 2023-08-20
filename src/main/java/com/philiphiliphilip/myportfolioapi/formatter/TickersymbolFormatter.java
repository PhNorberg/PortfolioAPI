package com.philiphiliphilip.myportfolioapi.formatter;

import org.springframework.stereotype.Service;

@Service
public class TickersymbolFormatter implements NameFormatter {

    @Override
    public String format(String name) {
        return name.toUpperCase();
    }
}
