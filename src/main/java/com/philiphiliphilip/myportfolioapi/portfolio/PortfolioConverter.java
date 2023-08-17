package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.asset.AssetConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PortfolioConverter {

    private AssetConverter assetConverter;

    public PortfolioConverter(AssetConverter assetConverter) {
        this.assetConverter = assetConverter;
    }

    public PortfolioDTOUsersLevel convertToUsersLevel(Portfolio portfolio){
        return new PortfolioDTOUsersLevel(portfolio.getName());
    }

    public PortfolioDTOUsernameLevel convertToUsernameLevel(Portfolio portfolio){
        return new PortfolioDTOUsernameLevel(portfolio.getName(), portfolio.getValueNow());
    }

    public PortfolioDTOPortfolionameLevelSelf convertToPortfolionameLevelSelf(Portfolio portfolio){
        PortfolioDTOPortfolionameLevelSelf dto = new PortfolioDTOPortfolionameLevelSelf();

        dto.setName(portfolio.getName());
        dto.setTotalInvested(portfolio.getTotalInvested());
        dto.setValueNow(portfolio.getValueNow());
        dto.setProfitFactor(portfolio.getProfitFactor());
        dto.setGrossProfitDollars(portfolio.getGrossProfitDollars());
        dto.setNetProfitDollars(portfolio.getNetProfitDollars());
        dto.setAssets(portfolio.getAssets().stream().map(asset -> assetConverter.convertToPortfolionameLevelSelf(asset))
                .collect(Collectors.toList()));

        return dto;
    }

    public PortfolioDTOPortfolionameLevelOther convertToPortfolionameLevelOther(Portfolio portfolio){
        PortfolioDTOPortfolionameLevelOther dto = new PortfolioDTOPortfolionameLevelOther();

        dto.setName(portfolio.getName());
        dto.setTotalInvested(portfolio.getTotalInvested());
        dto.setValueNow(portfolio.getValueNow());
        dto.setProfitFactor(portfolio.getProfitFactor());
        dto.setGrossProfitDollars(portfolio.getGrossProfitDollars());
        dto.setAssets(portfolio.getAssets().stream().map(asset -> assetConverter.convertToPortfolionameLevelOther(asset))
                .collect(Collectors.toList()));

        return dto;
    }
}
