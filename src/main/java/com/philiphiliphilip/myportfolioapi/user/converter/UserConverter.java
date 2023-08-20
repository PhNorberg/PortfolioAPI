package com.philiphiliphilip.myportfolioapi.user.converter;

import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsernameLevel;
import com.philiphiliphilip.myportfolioapi.user.dto.UserDTOUsersLevel;
import com.philiphiliphilip.myportfolioapi.user.model.User;
import com.philiphiliphilip.myportfolioapi.portfolio.converter.PortfolioConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    private final PortfolioConverter portfolioConverter;

    public UserConverter(PortfolioConverter portfolioConverter) {
        this.portfolioConverter = portfolioConverter;
    }

    public UserDTOUsersLevel convertToUsersLevel(User user){

        UserDTOUsersLevel dto = new UserDTOUsersLevel();

        dto.setUsername(user.getUsername());
        dto.setPortfolios(user.getPortfolio().stream().map(portfolioConverter::convertToUsersLevel)
                .collect(Collectors.toList()));

        return dto;
    }

    public UserDTOUsernameLevel convertToUsernameLevel(User user){

        UserDTOUsernameLevel dto = new UserDTOUsernameLevel();

        dto.setUsername(user.getUsername());
        dto.setPortfolios(user.getPortfolio().stream().map(portfolioConverter::convertToUsernameLevel)
                .collect(Collectors.toList()));

        return dto;
    }
}
