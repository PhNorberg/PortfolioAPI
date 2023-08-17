package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.asset.AssetDTOPortfoliosLevel;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDTOPortfoliosLevel {

    private String name;
    private BigDecimal valueNow;
    private List<AssetDTOPortfoliosLevel> assets;

    public PortfolioDTOPortfoliosLevel(String name, BigDecimal valueNow, List<AssetDTOPortfoliosLevel> assets) {
        this.name = name;
        this.valueNow = valueNow;
        this.assets = assets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValueNow() {
        return valueNow;
    }

    public void setValueNow(BigDecimal valueNow) {
        this.valueNow = valueNow;
    }

    public List<AssetDTOPortfoliosLevel> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDTOPortfoliosLevel> assets) {
        this.assets = assets;
    }
}
