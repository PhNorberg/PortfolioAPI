package com.philiphiliphilip.myportfolioapi.formatter;

import com.philiphiliphilip.myportfolioapi.utility.NameFormatter;
import org.springframework.stereotype.Service;

@Service
public class TickersymbolFormatter implements NameFormatter {

    @Override
    public String format(String name) {
        return name.toUpperCase();
    }
}
