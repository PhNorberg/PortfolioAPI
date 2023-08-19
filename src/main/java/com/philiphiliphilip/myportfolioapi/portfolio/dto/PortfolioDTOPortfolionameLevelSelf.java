package com.philiphiliphilip.myportfolioapi.portfolio.dto;

import com.philiphiliphilip.myportfolioapi.asset.dto.AssetDTOPortfolionameLevelSelf;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDTOPortfolionameLevelSelf implements PortfolioDTO {

    private String name;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private BigDecimal netProfitDollars;
    private List<AssetDTOPortfolionameLevelSelf> assets;

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

    public BigDecimal getNetProfitDollars() {
        return netProfitDollars;
    }

    public void setNetProfitDollars(BigDecimal netProfitDollars) {
        this.netProfitDollars = netProfitDollars;
    }

    public List<AssetDTOPortfolionameLevelSelf> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDTOPortfolionameLevelSelf> assets) {
        this.assets = assets;
    }
}
