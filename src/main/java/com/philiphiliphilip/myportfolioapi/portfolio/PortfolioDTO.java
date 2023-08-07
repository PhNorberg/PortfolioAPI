package com.philiphiliphilip.myportfolioapi.portfolio;

import com.philiphiliphilip.myportfolioapi.asset.Asset;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDTO {

    private Integer id;
    private String name;
    private BigDecimal totalInvested;
    private BigDecimal valueNow;
    private BigDecimal profitFactor;
    private BigDecimal grossProfitDollars;
    private BigDecimal netProfitDollars;
    private List<Asset> assets;

    public PortfolioDTO(Integer id, String name, BigDecimal totalInvested, BigDecimal valueNow, BigDecimal profitFactor, BigDecimal grossProfitDollars, BigDecimal netProfitDollars, List<Asset> assets) {
        this.id = id;
        this.name = name;
        this.totalInvested = totalInvested;
        this.valueNow = valueNow;
        this.profitFactor = profitFactor;
        this.grossProfitDollars = grossProfitDollars;
        this.netProfitDollars = netProfitDollars;
        this.assets = assets;
    }

    public PortfolioDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
