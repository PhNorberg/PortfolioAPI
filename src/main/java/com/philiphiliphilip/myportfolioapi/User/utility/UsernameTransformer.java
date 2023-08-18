package com.philiphiliphilip.myportfolioapi.User.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UsernameTransformer {

    public String transform(String username){
        return StringUtils.capitalize(username.toLowerCase());
    }
}
