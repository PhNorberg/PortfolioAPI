package com.philiphiliphilip.myportfolioapi.formatter;

import com.philiphiliphilip.myportfolioapi.utility.NameFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PortfolionameFormatter implements NameFormatter {

    @Override
    public String format(String name) {
        return StringUtils.capitalize(name.replace(" ", "-").toLowerCase());
    }
}
