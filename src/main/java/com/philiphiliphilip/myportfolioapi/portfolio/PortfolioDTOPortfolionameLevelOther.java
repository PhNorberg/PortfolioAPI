package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.asset.AssetDTOPortfolionameLevelOther;
import com.philiphiliphilip.myportfolioapi.asset.AssetDTOPortfolionameLevelSelf;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDTOPortfolionameLevelOther implements PortfolioDTO{

    private String name;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private List<AssetDTOPortfolionameLevelOther> assets;

    public PortfolioDTOPortfolionameLevelOther(String name, BigDecimal valueNow, List<AssetDTOPortfolionameLevelOther> assets) {
        this.name = name;
        this.valueNow = valueNow;
        this.assets = assets;
    }

    public PortfolioDTOPortfolionameLevelOther(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public BigDecimal getValueNow() {
        return valueNow;
    }

    public void setValueNow(BigDecimal valueNow) {
        this.valueNow = valueNow;
    }

    public BigDecimal getProfitFactor() {
        return profitFactor;
    }

    public void setProfitFactor(BigDecimal profitFactor) {
        this.profitFactor = profitFactor;
    }

    public BigDecimal getGrossProfitDollars() {
        return grossProfitDollars;
    }

    public void setGrossProfitDollars(BigDecimal grossProfitDollars) {
        this.grossProfitDollars = grossProfitDollars;
    }

    public List<AssetDTOPortfolionameLevelOther> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDTOPortfolionameLevelOther> assets) {
        this.assets = assets;
    }
}
