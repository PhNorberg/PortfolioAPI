package com.philiphiliphilip.myportfolioapi.asset;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AssetConverter {

    public AssetDTOPortfolionameLevelSelf convertToPortfolionameLevelSelf(Asset asset){
        AssetDTOPortfolionameLevelSelf dto = new AssetDTOPortfolionameLevelSelf();

        dto.setTickerSymbol(asset.getTickerSymbol());
        dto.setAssetType(asset.getAssetType());
        dto.setQuantity(asset.getQuantity());
        dto.setPurchasePrice(asset.getPurchasePrice());
        dto.setCurrentPrice(asset.getCurrentPrice());
        dto.setTotalInvested(asset.getTotalInvested());
        dto.setValueNow(asset.getValueNow());
        dto.setTaxRate(asset.getTaxRate());
        dto.setProfitFactor(asset.getProfitFactor());
        dto.setGrossProfitDollars(asset.getGrossProfitDollars());
        dto.setNetProfitDollars(asset.getNetProfitDollars());

        return dto;
    }

    public AssetDTOPortfolionameLevelOther convertToPortfolionameLevelOther(Asset asset){
        AssetDTOPortfolionameLevelOther dto = new AssetDTOPortfolionameLevelOther();

        dto.setTickerSymbol(asset.getTickerSymbol());
        dto.setQuantity(asset.getQuantity());
        dto.setPurchasePrice(asset.getPurchasePrice());
        dto.setCurrentPrice(asset.getCurrentPrice());
        dto.setCurrentPrice(asset.getCurrentPrice());
        dto.setTotalInvested(asset.getTotalInvested());
        dto.setValueNow(asset.getValueNow());
        dto.setProfitFactor(asset.getProfitFactor());
        dto.setGrossProfitDollars(asset.getGrossProfitDollars());

        return dto;
    }
}

